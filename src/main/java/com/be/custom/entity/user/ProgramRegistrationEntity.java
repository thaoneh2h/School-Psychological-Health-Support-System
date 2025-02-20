package com.be.custom.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ProgramRegistrations")
@Getter
@Setter
public class ProgramRegistrationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long registrationId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "ProgramID", nullable = false)
    private SupportProgramEntity program;

    @Column(nullable = false)
    private LocalDateTime registeredAt = LocalDateTime.now();

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
