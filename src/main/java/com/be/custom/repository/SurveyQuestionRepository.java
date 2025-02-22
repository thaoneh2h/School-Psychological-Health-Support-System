package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyQuestionEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyQuestionRepository extends BaseRepository<SurveyQuestionEntity> {

    @Query(value = "select sq from SurveyQuestionEntity sq " +
            "where sq.survey.id = ?1")
    List<SurveyQuestionEntity> getListQuestionOfSurvey(Long surveyId);

}
