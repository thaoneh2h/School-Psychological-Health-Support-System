package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SurveyResponses")
@Getter
@Setter
public class SurveyResponseEntity  extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "QuestionID", nullable = false)
    private SurveyQuestionEntity question;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private UserEntity user;

    @Column(nullable = false)
    private String typeAnswer;

    @Column(columnDefinition = "TEXT")
    private String answer;

    @Column(nullable = false)
    private LocalDateTime submittedAt = LocalDateTime.now();

    @Override
    public Long getId() {
        return id;
    }
}
