package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Appointments")
@Getter
@Setter
public class AppointmentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "StudentID", nullable = false)
    private UserEntity student;

    @ManyToOne
    @JoinColumn(name = "ParentID")
    private UserEntity parent;

    @ManyToOne
    @JoinColumn(name = "PsychologistID", nullable = false)
    private UserEntity psychologist;

    @Column(nullable = false)
    private LocalDateTime appointmentDate;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "CreatedBy", nullable = false)
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;

    @Column(nullable = false)
    private Date updatedAt = new Date();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Override
    public Long getId() {
        return id;
    }
}
