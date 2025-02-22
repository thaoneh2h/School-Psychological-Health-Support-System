package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.be.custom.enums.TypeQuestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SurveyResponses")
@Getter
@Setter
public class SurveyResponseEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestionEntity question;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private TypeQuestion typeAnswer;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private Date submittedAt = new Date();

    @Override
    public Long getId() {
        return id;
    }
}
