package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "SurveyOptions")
@Getter
@Setter
public class SurveyOptionEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long optionId;

    @ManyToOne
    @JoinColumn(name = "QuestionID", nullable = false)
    private SurveyQuestionEntity question;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String optionText;

    @Override
    public Long getId() {
        return optionId;
    }

}
