package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.custom.dto.response_api.DetailAnswerDto;
import com.be.custom.entity.*;
import com.be.custom.enums.TypeQuestion;
import com.be.custom.repository.SurveyHistoryRepository;
import com.be.custom.repository.SurveyResponseRepository;
import com.be.custom.repository.UserRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyHistoryService extends BaseService<SurveyHistoryEntity, SurveyHistoryRepository> {

    private final UserService userService;
    private final UserRepository userRepository;
    private final SurveyQuestionService surveyQuestionService;
    private final SurveyOptionService surveyOptionService;
    private final SurveyResponseRepository surveyResponseRepository;

    public List<SurveyHistoryEntity> getListHistorySurveyByListStudentId(List<Long> listStudentId) {
        if (listStudentId.isEmpty()) {
            return null;
        }

        return repository.getListHistorySurveyByListStudentId(listStudentId);
    }

    public List<SurveyHistoryEntity> getListHistorySurveyFromParent(Long parentId) {
        List<Long> listStudentId = userRepository.getListStudentIdOfParent(parentId);
        if (listStudentId.isEmpty()) {
            return null;
        }

        return getListHistorySurveyByListStudentId(listStudentId);
    }

    public List<DetailAnswerDto> getDetailSurvey(Long surveyId, Long studentId) {
        if (surveyId == null || studentId == null) {
            return null;
        }

        List<SurveyQuestionEntity> listQuestion = surveyQuestionService.getListQuestionOfSurvey(surveyId);
        List<Long> listQuestionId = listQuestion.stream().map(SurveyQuestionEntity::getId)
                .collect(Collectors.toList());

        List<SurveyOptionEntity> listOption = surveyOptionService.getListAllOptionOfQuestion(listQuestionId);
        List<SurveyResponseEntity> listAnswer = surveyResponseRepository.getListResponseOfStudent(listQuestionId, studentId);
        List<DetailAnswerDto> listDataAnswer = new ArrayList<>();

        for (SurveyQuestionEntity question : listQuestion) {
            TypeQuestion typeQuestion = question.getQuestionType();
            Long questionId = question.getId();
            SurveyResponseEntity answerOfQuestion = listAnswer.stream()
                    .filter(item -> item.getQuestion().getId().equals(questionId))
                    .findFirst().orElse(null);
            String answerText = "";
            List<Long> listOptionIdSelected = new ArrayList<>();
            if (answerOfQuestion == null) {
                continue;
            }
            if (typeQuestion == TypeQuestion.FILL_TEXT) {
                answerText = answerOfQuestion.getAnswer();
            }
            if (typeQuestion == TypeQuestion.CHOOSE_OPTION) {
                List<Long> listOptionId = Arrays.stream(answerOfQuestion.getAnswer().split(","))
                        .map(Long::parseLong)
                        .collect(Collectors.toList());
                listOptionIdSelected.addAll(listOptionId);
            }
            DetailAnswerDto detailAnswerDto = DetailAnswerDto.builder()
                    .answerText(answerText)
                    .listOption(listOption)
                    .listOptionIdSelected(listOptionIdSelected)
                    .questionId(questionId)
                    .questionText(question.getQuestionText())
                    .typeQuestion(typeQuestion)
                    .build();

            listDataAnswer.add(detailAnswerDto);
        }

        return listDataAnswer;
    }

    public Page<SurveyHistoryEntity> getPageSurveyHistory(Pageable pageable, String keyword) {
        return repository.getPageSurveyHistory(keyword, pageable);
    }
}
