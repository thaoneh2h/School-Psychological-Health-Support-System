package com.be.custom.common.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsHolder {

    /**
     * get user details of current login user.
     *
     * @return CustomUserDetails instance if user authenticated , return null if user not authenticated.
     */
    public CustomUserDetails getCurrentUser() {
        if (SecurityContextHolder.getContext() != null) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {

                Object principal = authentication.getPrincipal();
                if (principal instanceof CustomUserDetails) {
                    return (CustomUserDetails) principal;
                }
            }
        }
        return null;
    }

    public CustomUserDetails getCustomUserDetails() {
        return getCurrentUser();
    }

}
