package com.be.custom.api.register;

import com.be.base.dto.ServerResponse;
import com.be.custom.dto.login.LoginAdminDto;
import com.be.custom.dto.request.SaveUserDto;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterApi {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ServerResponse> register(@ModelAttribute SaveUserDto saveUserDto) {
        return ResponseEntity.ok(ServerResponse.success(userService.fromSaveDto(saveUserDto)));
    }
}
