package com.be.custom.api.user;

import com.be.base.dto.ServerResponse;
import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.entity.UserEntity;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserApi {

    private final UserService userService;

    @PostMapping("/get-student-of-parent")
    public ResponseEntity<List<UserEntity>> register(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long parentId = userDetails.getUserId();
        List<UserEntity> listStudentOfParent = userService.getListStudentOfParent(parentId);
        return ResponseEntity.ok(listStudentOfParent);
    }
}
