package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Surveys")
@Getter
@Setter
public class SurveyEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @ManyToOne
    @JoinColumn(name = "CreatedBy")
    private UserEntity createdBy;

    @ManyToOne
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;

    private Boolean isDeleted = false;

    @OneToMany(mappedBy = "survey")
    private List<SurveyQuestionEntity> questions;

    @Override
    public Long getId() {
        return id;
    }
}
