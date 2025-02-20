package com.be.custom.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "SupportPrograms")
@Getter
@Setter
public class SupportProgramEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long programId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;

    @Column(nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "program")
    private List<ProgramRegistrationEntity> registrations;
}
