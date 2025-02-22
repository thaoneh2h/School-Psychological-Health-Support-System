package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.custom.entity.SurveyOptionEntity;
import com.be.custom.repository.SurveyOptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyOptionService extends BaseService<SurveyOptionEntity, SurveyOptionRepository> {

    public List<SurveyOptionEntity> getListAllOptionOfQuestion(List<Long> listQuestionId) {
        return repository.getAllOptionOfListQuestionId(listQuestionId);
    }
}
