package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyResponseEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyResponseRepository extends BaseRepository<SurveyResponseEntity> {

    @Query(value = "select sr from SurveyResponseEntity sr " +
            "where sr.question.id in ?1 " +
            "and sr.user.id = ?2")
    List<SurveyResponseEntity> getListResponseOfStudent(List<Long> listQuestionId, Long studentId);

}
