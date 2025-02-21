package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.be.custom.enums.StatusBookingAppointment;
import lombok.Builder;
import lombok.Data;
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
    @JoinColumn(name = "student_id", nullable = false)
    private UserEntity student;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private UserEntity parent;

    @ManyToOne
    @JoinColumn(name = "psychologist_id", nullable = false)
    private UserEntity psychologist;

    @Column(nullable = false)
    private Date appointmentDate;

    @Column(nullable = false)
    private Date startTime;

    @Column(nullable = false)
    private Date endTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusBookingAppointment status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @ManyToOne
    @JoinColumn(name = "created_by", nullable = false)
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "updated_by")
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
