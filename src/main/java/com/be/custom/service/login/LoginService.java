package com.be.custom.service.login;

import com.be.base.dto.ResponseCase;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.cache.TokenDto;
import com.be.custom.dto.cache.TypeToken;
import com.be.custom.dto.login.LoginAdminDto;
import com.be.custom.dto.login.LoginResponseDto;
import com.be.custom.entity.TypeUser;
import com.be.custom.entity.UserEntity;
import com.be.custom.service.UserService;
import com.be.custom.service.cache.TokenCacheService;
import com.be.custom.utils.UserDetailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserService userService;
    private final TokenCacheService tokenCacheService;

    public ResponseEntity<ServerResponse> login(LoginAdminDto loginDto) {
        String username = loginDto.getUsername();
        String password = loginDto.getPassword();
        log.info("start login admin with username: {}", username);
        Optional<UserEntity> userOpt = userService.validateUser(username, password);
        if (userOpt.isEmpty()) {
            log.info("user is invalid");
            return ResponseEntity.ok(ServerResponse.with(ResponseCase.USERNAME_PWD_INVALID));
        }

        UserEntity user = userOpt.get();
        Long userId = user.getUserId();

        String token = tokenCacheService.generateToken();
        TokenDto tokenDto = new TokenDto(token, userId, TypeToken.ACCESS_TOKEN);
        tokenCacheService.updateCache(tokenDto);

        UserDetailUtils.setUserAuthenticated(user, token);

        LoginResponseDto response = new LoginResponseDto(user.getUserId(), username, user.getFullName());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", String.format("access_token=%s;Max-Age=86400;Path=/;", token));
        return ResponseEntity.ok().headers(headers).body(ServerResponse.success(response));
    }

}
