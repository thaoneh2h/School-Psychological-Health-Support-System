package com.be.custom.api.program_registration;

import com.be.base.dto.ServerResponse;
import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.entity.ProgramRegistrationEntity;
import com.be.custom.service.program_registration.ProgramRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/program-registration")
@PreAuthorize("@authorizationService.isLogin()")
@RequiredArgsConstructor
public class ProgramRegistrationApi {

    private final ProgramRegistrationService programRegistrationService;

    @GetMapping("/get-program-registration-user")
    public ResponseEntity<List<ProgramRegistrationEntity>> getProgramRegistrationOfUser(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(programRegistrationService.getAllProgramRegistrationByUserId(userId));
    }

    @PostMapping("/register-student")
    public ResponseEntity<ServerResponse> registerSupportProgramFormStudent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                            @RequestParam Long programId) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(programRegistrationService.saveProgramRegistration(userId, programId));
    }

    @PostMapping("/register-form-parent")
    public ResponseEntity<ServerResponse> registerSupportProgramFormParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                           @RequestParam Long studentId,
                                                                           @RequestParam Long programId) {
        Long parentId = userDetails.getUserId();
        return ResponseEntity.ok(programRegistrationService.saveProgramRegistrationFromParent(parentId, studentId, programId));
    }
}
