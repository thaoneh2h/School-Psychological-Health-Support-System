package com.be.custom.service.authorization;

import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.common.security.UserDetailsHolder;
import com.be.custom.entity.TypeUser;
import com.be.custom.enums.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "authorizationService")
@Slf4j
@RequiredArgsConstructor
public class PreAuthorizeService {

    private final UserDetailsHolder userDetailsHolder;

    public boolean isSystemAdmin() {
        CustomUserDetails userDetails = userDetailsHolder.getCurrentUser();
        if (userDetails == null) {
            return false;
        }
        return Role.SYSTEM_ADMIN == userDetails.getRole();
    }

    public boolean isCompanyAdmin() {
        CustomUserDetails userDetails = userDetailsHolder.getCurrentUser();
        if (userDetails == null) {
            return false;
        }
        return Role.SYSTEM_ADMIN == userDetails.getRole();
    }

    public boolean isCompanyStaff() {
        CustomUserDetails userDetails = userDetailsHolder.getCurrentUser();
        if (userDetails == null) {
            return false;
        }
        return TypeUser.WEB_ADMIN == userDetails.getTypeUser();
    }

    public boolean isLogin() {
        CustomUserDetails userDetails = userDetailsHolder.getCurrentUser();
        if (userDetails == null) {
            return false;
        }
        return userDetails.getUsername() != null;
    }

}
