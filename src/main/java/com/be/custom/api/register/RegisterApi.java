package com.be.custom.api.register;

import com.be.base.dto.ServerResponse;
import com.be.custom.dto.login.LoginAdminDto;
import com.be.custom.dto.request.SaveUserDto;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class RegisterApi {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<ServerResponse> register(@RequestBody SaveUserDto saveUserDto) {
        return ResponseEntity.ok(userService.saveUser(saveUserDto));
    }
}
