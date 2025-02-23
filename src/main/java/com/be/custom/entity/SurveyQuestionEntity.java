package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.be.custom.enums.TypeQuestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SurveyQuestions")
@Getter
@Setter
public class SurveyQuestionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private SurveyEntity survey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TypeQuestion questionType;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;

    @OneToMany(mappedBy = "question")
    private List<SurveyOptionEntity> options;

    @Override
    public Long getId() {
        return id;
    }
}
