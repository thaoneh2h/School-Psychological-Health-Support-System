package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.be.custom.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "Users")
@Where(clause = "is_deleted=false")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDate dateOfBirth;

    @Column(nullable = false)
    private String gender;

    private String phoneNumber;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String invitationParentCode;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "parent")
    private List<ParentChildEntity> children;

    @OneToMany(mappedBy = "child")
    private List<ParentChildEntity> parents;

    @OneToMany(mappedBy = "createdBy")
    private List<SurveyEntity> createdSurveys;

    @OneToMany(mappedBy = "updatedBy")
    private List<SurveyEntity> updatedSurveys;

    @OneToMany(mappedBy = "updatedBy")
    private List<SupportProgramEntity> updatedPrograms;

    @OneToMany(mappedBy = "user")
    private List<ProgramRegistrationEntity> programRegistrations;

    @OneToMany(mappedBy = "student")
    private List<AppointmentEntity> studentAppointments;

    @OneToMany(mappedBy = "parent")
    private List<AppointmentEntity> parentAppointments;

    @OneToMany(mappedBy = "psychologist")
    private List<AppointmentEntity> psychologistAppointments;

    @OneToMany(mappedBy = "author")
    private List<BlogPostEntity> blogPosts;

    @OneToMany(mappedBy = "uploadedBy")
    private List<ResourceEntity> resources;

    @OneToMany(mappedBy = "user")
    private List<SurveyHistoryEntity> surveyHistories;

    @Override
    public Long getId() {
        return userId;
    }
}
