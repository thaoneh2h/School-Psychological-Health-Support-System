package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.be.custom.enums.StatusProgramRegistration;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "ProgramRegistrations")
@Getter
@Setter
public class ProgramRegistrationEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "program_id", nullable = false)
    private SupportProgramEntity program;

    @Column(nullable = false)
    private Date registeredAt = new Date();

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusProgramRegistration status;

    @Column(nullable = false)
    private Date updatedAt = new Date();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Override
    public Long getId() {
        return id;
    }
}
