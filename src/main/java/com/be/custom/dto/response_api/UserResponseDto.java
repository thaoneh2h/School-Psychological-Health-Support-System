package com.be.custom.dto.response_api;

import com.be.custom.enums.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponseDto {
    private String name;
    private String username;
    private Long id;
    private Role role;
    private String email;
    private String phone;
}
