package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "SupportPrograms")
@Getter
@Setter
public class SupportProgramEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "updated_by")
    private UserEntity updatedBy;

    @Column(nullable = false)
    private Date updatedAt = new Date();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "program")
    private List<ProgramRegistrationEntity> registrations;

    @Override
    public Long getId() {
        return id;
    }
}
