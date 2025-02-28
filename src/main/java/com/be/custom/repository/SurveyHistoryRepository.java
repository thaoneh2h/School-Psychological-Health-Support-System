package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyHistoryEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SurveyHistoryRepository extends BaseRepository<SurveyHistoryEntity> {

    @Query(value = "select sh from SurveyHistoryEntity sh " +
            "where sh.user.id in ?1")
    List<SurveyHistoryEntity> getListHistorySurveyByListStudentId(List<Long> listStudentId);

    @Query(value = "SELECT sh FROM SurveyHistoryEntity sh " +
            "WHERE (:keyword IS NULL OR sh.survey.title LIKE %:keyword% " +
            "OR sh.user.fullName LIKE %:keyword% " +
            "OR sh.survey.description LIKE %:keyword%) " +
            "AND sh.survey.isDeleted = false")
    Page<SurveyHistoryEntity> getPageSurveyHistory(@Param("keyword") String keyword, Pageable pageable);

}
