package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyHistoryEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyHistoryRepository extends BaseRepository<SurveyHistoryEntity> {

    @Query(value = "select sh from SurveyHistoryEntity sh " +
            "where sh.user.id in ?1")
    List<SurveyHistoryEntity> getListHistorySurveyByListStudentId(List<Long> listStudentId);
}
