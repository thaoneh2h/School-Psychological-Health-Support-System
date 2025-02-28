package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyOptionEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyOptionRepository extends BaseRepository<SurveyOptionEntity> {

    @Query(value = "select so from SurveyOptionEntity so " +
            "where so.question.id in ?1")
    List<SurveyOptionEntity> getAllOptionOfListQuestionId(List<Long> listQuestionId);

    @Modifying
    @Query(value = "delete from SurveyOptionEntity s " +
            "where s.id in ?1")
    void deleteOptionByListOptionId(List<Long> listOptionId);
}
