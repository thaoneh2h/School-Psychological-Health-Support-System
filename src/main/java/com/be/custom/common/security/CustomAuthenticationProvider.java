package com.be.custom.common.security;

import com.be.custom.entity.TypeUser;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.Role;
import com.be.custom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    //login for swagger
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        log.info("validate user: {}", username);
        String password = authentication.getCredentials().toString();

        Optional<UserEntity> userOpt = userRepository.findByEmailAndIsDeletedFalse(username);
        if (userOpt.isEmpty()) {
            log.info("user is invalid");
            throw new BadCredentialsException("Username or memberCode is incorrect!");
        }

        UserEntity userEntity = userOpt.get();
        if (!passwordEncoder.matches(password, userEntity.getPasswordHash())) {
            log.info("password is incorrect");
            throw new BadCredentialsException("Username or memberCode is incorrect!");
        }

        if (Role.SYSTEM_ADMIN != userEntity.getRole()) {
            log.info("user is not system admin");
            throw new BadCredentialsException("Username or memberCode is incorrect!");
        }

        Collection<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_SYSTEM_ADMIN"));
        UserDetails userDetails = new CustomUserDetails(userEntity, "");
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }

    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
