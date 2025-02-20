package com.be.custom.common.security;

import com.be.custom.entity.TypeUser;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private static final long serialVersionUID = 2983770557678161325L;

    @JsonIgnore
    private String accessToken;
    private Long userId;
    private String username;
    private String name;
    private TypeUser typeUser;
    private Role role;

    public CustomUserDetails(UserEntity user,  String accessToken) {
        this.accessToken = accessToken;
        this.userId = user.getId();
        this.username = user.getEmail();
        this.name = user.getFullName();
        this.role = user.getRole();
    }

    @JsonProperty
    public Long getUserId() {
        return this.userId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role.name()));
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    @JsonProperty
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    @JsonProperty
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    @JsonProperty
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CustomUserDetails)) {
            return false;
        }
        CustomUserDetails that = (CustomUserDetails) o;
        return Objects.equals(accessToken, that.accessToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, username);
    }
}

