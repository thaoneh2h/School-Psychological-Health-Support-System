package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "SurveyHistory")
@Getter
@Setter
public class SurveyHistoryEntity extends BaseEntity {

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
    private Date updatedAt;

    @Override
    public Long getId() {
        return historyId;
    }
}
