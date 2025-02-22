package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.custom.dto.response_api.QuestionOfSurveyDto;
import com.be.custom.entity.SurveyEntity;
import com.be.custom.entity.SurveyOptionEntity;
import com.be.custom.entity.SurveyQuestionEntity;
import com.be.custom.enums.TypeQuestion;
import com.be.custom.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService extends BaseService<SurveyEntity, SurveyRepository> {

    private final SurveyQuestionService surveyQuestionService;
    private final SurveyOptionService surveyOptionService;

    public List<SurveyEntity> getAllSurvey() {
        return repository.findAllByIsDeletedFalse();
    }

    public List<QuestionOfSurveyDto> getListQuestionOfSurvey(Long surveyId) {
        List<SurveyQuestionEntity> listQuestionOfSurvey = surveyQuestionService.getListQuestionOfSurvey(surveyId);
        if (listQuestionOfSurvey.isEmpty()) {
            return null;
        }

        List<Long> listQuestionId = listQuestionOfSurvey.stream()
                .map(SurveyQuestionEntity::getId)
                .collect(Collectors.toList());
        List<SurveyOptionEntity> listAllOption = surveyOptionService.getListAllOptionOfQuestion(listQuestionId);
        return convertDataToQuestionOfSurveyDto(listQuestionOfSurvey, listAllOption, surveyId);
    }

    private List<QuestionOfSurveyDto> convertDataToQuestionOfSurveyDto(List<SurveyQuestionEntity> listQuestionOfSurvey,
                                                                       List<SurveyOptionEntity> listAllOption,
                                                                       Long surveyId) {
        List<QuestionOfSurveyDto> listQuestionOfSurveyDto = new ArrayList<>();

        for (SurveyQuestionEntity question : listQuestionOfSurvey) {
            Long questionId = question.getId();
            TypeQuestion typeQuestion = question.getQuestionType();
            List<SurveyOptionEntity> listOptionOfQuestion = null;
            if (typeQuestion == TypeQuestion.CHOOSE_OPTION) {
                listOptionOfQuestion = listAllOption.stream()
                        .filter(item -> item.getId().equals(questionId))
                        .collect(Collectors.toList());

            }
            QuestionOfSurveyDto questionOfSurveyDto = QuestionOfSurveyDto.builder()
                    .surveyId(surveyId)
                    .questionId(questionId)
                    .typeQuestion(typeQuestion)
                    .questionText(question.getQuestionText())
                    .listOption(listOptionOfQuestion)
                    .build();
            listQuestionOfSurveyDto.add(questionOfSurveyDto);
        }

        return listQuestionOfSurveyDto;

    }
}
