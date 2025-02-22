package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.be.custom.enums.Role;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Users")
@Where(clause = "is_deleted=false")
@Getter
@Setter
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private Date dateOfBirth;

    private String gender;

    private String phoneNumber;

    private String imageUrl;

    @Column(columnDefinition = "TEXT")
    private String address;

    private String invitationParentCode;

    private Boolean isDeleted = false;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<ParentChildEntity> children;

    @JsonIgnore
    @OneToMany(mappedBy = "child")
    private List<ParentChildEntity> parents;

    @JsonIgnore
    @OneToMany(mappedBy = "createdBy")
    private List<SurveyEntity> createdSurveys;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedBy")
    private List<SurveyEntity> updatedSurveys;

    @JsonIgnore
    @OneToMany(mappedBy = "updatedBy")
    private List<SupportProgramEntity> updatedPrograms;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<ProgramRegistrationEntity> programRegistrations;

    @JsonIgnore
    @OneToMany(mappedBy = "student")
    private List<AppointmentEntity> studentAppointments;

    @JsonIgnore
    @OneToMany(mappedBy = "parent")
    private List<AppointmentEntity> parentAppointments;

    @JsonIgnore
    @OneToMany(mappedBy = "psychologist")
    private List<AppointmentEntity> psychologistAppointments;

    @JsonIgnore
    @OneToMany(mappedBy = "author")
    private List<BlogPostEntity> blogPosts;

    @JsonIgnore
    @OneToMany(mappedBy = "uploadedBy")
    private List<ResourceEntity> resources;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<SurveyHistoryEntity> surveyHistories;

    @Override
    public Long getId() {
        return id;
    }
}
