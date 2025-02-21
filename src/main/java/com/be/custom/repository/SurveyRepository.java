package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SurveyEntity;

import java.util.List;

public interface SurveyRepository extends BaseRepository<SurveyEntity> {
    
    List<SurveyEntity> findAllByIsDeletedFalse();
}
