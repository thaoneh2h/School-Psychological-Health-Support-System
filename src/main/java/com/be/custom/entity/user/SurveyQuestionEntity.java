package com.be.custom.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "SurveyQuestions")
@Getter
@Setter
public class SurveyQuestionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "SurveyID", nullable = false)
    private SurveyEntity survey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String questionText;

    @Column(nullable = false)
    private String questionType;

    @ManyToOne
    @JoinColumn(name = "UpdatedBy")
    private UserEntity updatedBy;

    @OneToMany(mappedBy = "question")
    private List<SurveyOptionEntity> options;

}
