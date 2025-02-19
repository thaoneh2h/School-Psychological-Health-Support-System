package com.be.custom.entity.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.be.base.core.BaseEntity;
import com.be.custom.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Entity
@Table(name = "user")
@Where(clause = "is_deleted=false")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String username;
    private String oldUsername;
    private String email;
    private String phone;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @JsonIgnore
    private String password;

    private boolean active;
}
