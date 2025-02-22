package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.custom.entity.SurveyQuestionEntity;
import com.be.custom.repository.SurveyQuestionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyQuestionService extends BaseService<SurveyQuestionEntity, SurveyQuestionRepository> {

    public List<SurveyQuestionEntity> getListQuestionOfSurvey(Long surveyId) {
        return repository.getListQuestionOfSurvey(surveyId);
    }
}
