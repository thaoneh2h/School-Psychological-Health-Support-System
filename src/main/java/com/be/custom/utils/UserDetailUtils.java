package com.be.custom.utils;


import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.entity.TypeUser;
import com.be.custom.entity.UserEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;

public class UserDetailUtils {

    public static void setUserAuthenticated(UserEntity user, String accessToken) {
        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        UserDetails userDetails = new CustomUserDetails(user, accessToken);
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

}
