package com.be.custom.dto.request;

import com.be.custom.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Setter
public class SaveUserDto {

    private Long id;
    private String username;
    private String name;
    private String password;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    private String email;
    private String phone;
}
