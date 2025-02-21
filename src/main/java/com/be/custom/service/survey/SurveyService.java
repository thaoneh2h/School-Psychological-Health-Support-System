package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.custom.entity.SurveyEntity;
import com.be.custom.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService extends BaseService<SurveyEntity, SurveyRepository> {

    public List<SurveyEntity> getAllSurvey() {
        return repository.findAllByIsDeletedFalse();
    }
}
