package com.be.custom.api.login;

import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.common.security.UserDetailsHolder;
import com.be.custom.dto.login.LoginAdminDto;
import com.be.custom.service.login.LoginService;
import com.be.base.dto.ServerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/web-admin")
@RequiredArgsConstructor
public class Login_AdminAPI {

    private final LoginService loginService;
    private final UserDetailsHolder userDetailsHolder;

    @PostMapping("/login")
    public ResponseEntity<ServerResponse> login(@ModelAttribute LoginAdminDto loginDto) {
        return loginService.login(loginDto);
    }

    @GetMapping("/is-authenticated")
    public CustomUserDetails isAuth() {
        return userDetailsHolder.getCustomUserDetails();
    }

}
