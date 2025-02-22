package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveAnswerSurveyRequest;
import com.be.custom.entity.*;
import com.be.custom.enums.TypeQuestion;
import com.be.custom.repository.SurveyHistoryRepository;
import com.be.custom.repository.SurveyResponseRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyResponseService extends BaseService<SurveyResponseEntity, SurveyResponseRepository> {

    private final SurveyQuestionService surveyQuestionService;
    private final UserService userService;
    private final SurveyService surveyService;
    private final SurveyHistoryRepository surveyHistoryRepository;

    @Transactional
    public ServerResponse saveAnswerSurvey(Long userId, List<SaveAnswerSurveyRequest> dataAnswer, Long surveyId) {
        if (dataAnswer.isEmpty()) {
            return ServerResponse.ERROR;
        }
        UserEntity student = userService.findUserById(userId).orElse(null);
        SurveyEntity survey = surveyService.findById(surveyId);
        if (student == null || survey == null) {
            return ServerResponse.ERROR;
        }

        List<SurveyQuestionEntity> listQuestion = surveyQuestionService.getListQuestionOfSurvey(surveyId);
        Map<Long, SurveyQuestionEntity> mapQuestionById = listQuestion.stream()
                .collect(Collectors.toMap(SurveyQuestionEntity::getId, surveyQuestionEntity -> surveyQuestionEntity));

        List<SurveyResponseEntity> listResponseNeedSave = new ArrayList<>();
        for (SaveAnswerSurveyRequest answer : dataAnswer) {
            SurveyResponseEntity surveyResponseEntity = new SurveyResponseEntity();
            SurveyQuestionEntity question = mapQuestionById.get(answer.getQuestionId());
            if (question == null) {
                continue;
            }
            TypeQuestion typeQuestion = question.getQuestionType();
            surveyResponseEntity.setTypeAnswer(typeQuestion);
            String answerText = "";
            if (typeQuestion == TypeQuestion.FILL_TEXT) {
                answerText = answer.getAnswerText();
            }
            List<Long> listOptionId = answer.getListOptionId();
            if (typeQuestion == TypeQuestion.CHOOSE_OPTION && !listOptionId.isEmpty()) {
                answerText = listOptionId.stream().map(String::valueOf).collect(Collectors.joining(","));
            }
            surveyResponseEntity.setAnswer(answerText);
            surveyResponseEntity.setUser(student);
            surveyResponseEntity.setQuestion(question);
            listResponseNeedSave.add(surveyResponseEntity);
        }

        saveAll(listResponseNeedSave);

        SurveyHistoryEntity surveyHistory = new SurveyHistoryEntity();
        surveyHistory.setSurvey(survey);
        surveyHistory.setUser(student);
        surveyHistory.setScore(0);
        surveyHistory.setUpdatedAt(new Date());
        surveyHistory.setCompletedAt(new Date());
        surveyHistoryRepository.save(surveyHistory);

        return ServerResponse.SUCCESS;
    }
}
