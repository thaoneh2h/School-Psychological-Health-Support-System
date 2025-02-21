package com.be.custom.api.user;

import com.be.base.dto.ServerResponse;
import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.dto.request.UpdateProfileRequest;
import com.be.custom.entity.UserEntity;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @PostMapping("/get-student-of-parent")
    public ResponseEntity<List<UserEntity>> getListStudentOfParent(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long parentId = userDetails.getUserId();
        List<UserEntity> listStudentOfParent = userService.getListStudentOfParent(parentId);
        return ResponseEntity.ok(listStudentOfParent);
    }

    @GetMapping("/get-list-psychologist")
    public ResponseEntity<List<UserEntity>> getListPsychologist() {
        List<UserEntity> listPsychologist = userService.getListPsychologist();
        return ResponseEntity.ok(listPsychologist);
    }

    @GetMapping("/get-profile")
    public ResponseEntity<UserEntity> getProfileOfUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(userService.findUserById(userId).orElse(null));
    }

    @PutMapping("/update-profile")
    public ResponseEntity<ServerResponse> updateProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestPart("profile") UpdateProfileRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            Long userId = userDetails.getUserId();
            return ResponseEntity.ok(userService.updateProfile(userId, request, file));
        } catch (Exception e) {
            return ResponseEntity.ok(ServerResponse.ERROR);
        }
    }
}
