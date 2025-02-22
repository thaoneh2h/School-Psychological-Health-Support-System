package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyOptionEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyOptionRepository extends BaseRepository<SurveyOptionEntity> {

    @Query(value = "select so from SurveyOptionEntity so " +
            "where so.deleted = false " +
            "and so.question.id in ?1")
    List<SurveyOptionEntity> getAllOptionOfListQuestionId(List<Long> listQuestionId);
}
