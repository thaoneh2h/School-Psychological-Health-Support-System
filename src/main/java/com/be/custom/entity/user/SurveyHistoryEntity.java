package com.be.custom.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SurveyHistory")
@Getter
@Setter
public class SurveyHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne
    @JoinColumn(name = "UserID", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "SurveyID", nullable = false)
    private SurveyEntity survey;

    private Integer score;

    @Column(nullable = false)
    private LocalDateTime completedAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
