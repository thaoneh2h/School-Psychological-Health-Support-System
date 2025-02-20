package com.be.custom.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SurveyOptions")
@Getter
@Setter
public class SurveyOptionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne
    @JoinColumn(name = "QuestionID", nullable = false)
    private SurveyQuestionEntity question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionText;

}
