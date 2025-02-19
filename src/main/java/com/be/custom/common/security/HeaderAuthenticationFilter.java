package com.be.custom.common.security;

import com.be.custom.dto.cache.TokenDto;
import com.be.custom.dto.cache.TypeToken;
import com.be.custom.entity.user.TypeUser;
import com.be.custom.entity.user.UserEntity;
import com.be.custom.repository.UserRepository;
import com.be.custom.service.cache.TokenCacheService;
import com.be.custom.utils.UserDetailUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class HeaderAuthenticationFilter implements Filter {

    private static final String PARAM_ACCESS_TOKEN = "access_token";
    private static final int CODE_TOKEN_INVALID = 999;
    private static final String URL_LOGIN = "/login";
    private static final String URL_LOGOUT = "/logout";
    private static final String URL_ERROR = "/error";

    private final TokenCacheService tokenCacheService;
    private final UserRepository userRepository;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String uri = request.getRequestURI();
        if (SecurityUtils.isAuthenticated()
                || uri.contains(URL_LOGIN) || uri.contains(URL_LOGOUT) || uri.contains(URL_ERROR)) {
            chain.doFilter(request, response);
            return;
        }

        String accessToken = request.getHeader(PARAM_ACCESS_TOKEN);
        if (accessToken != null) {
            Optional<TokenDto> tokenOpt = tokenCacheService.getTokenFromCache(accessToken, TypeToken.ACCESS_TOKEN);
            if (tokenOpt.isEmpty()) {
                log.info("access token is invalid {}", accessToken);
                response.setStatus(CODE_TOKEN_INVALID);
                return;
            }
            TokenDto tokenDto = tokenOpt.get();
            TypeUser typeUser = tokenDto.getTypeUser();
            Long userId = tokenDto.getUserId();
            if (TypeUser.WEB_ADMIN == typeUser) {
                Optional<UserEntity> userOpt = userRepository.findByIdAndIsDeletedFalseAndActiveTrue(userId);
                if (userOpt.isEmpty()) {
                    log.info("web admin is invalid with id {}", userId);
                    response.setStatus(CODE_TOKEN_INVALID);
                    return;
                }
                UserEntity user = userOpt.get();
                UserDetailUtils.setUserAuthenticated(user, TypeUser.WEB_ADMIN, accessToken);
            }

        }
        chain.doFilter(request, response);
    }

}
