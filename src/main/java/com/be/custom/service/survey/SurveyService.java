package com.be.custom.service.survey;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveSurveyOptionReq;
import com.be.custom.dto.request.SaveSurveyQuestionReq;
import com.be.custom.dto.request.SaveSurveyReq;
import com.be.custom.dto.response_api.QuestionOfSurveyDto;
import com.be.custom.entity.SurveyEntity;
import com.be.custom.entity.SurveyOptionEntity;
import com.be.custom.entity.SurveyQuestionEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.TypeQuestion;
import com.be.custom.repository.SurveyOptionRepository;
import com.be.custom.repository.SurveyRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService extends BaseService<SurveyEntity, SurveyRepository> {

    private final SurveyQuestionService surveyQuestionService;
    private final SurveyOptionService surveyOptionService;
    private final UserService userService;
    private final SurveyOptionRepository surveyOptionRepository;

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
                        .filter(item -> item.getQuestion().getId().equals(questionId))
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

    @Transactional
    public ServerResponse saveSurvey(SaveSurveyReq saveSurveyReq, Long userId) {
        Long surveyId = saveSurveyReq.getSurveyId();
        boolean isNewSurvey = surveyId == null;
        UserEntity user = userService.findById(userId);
        if (user == null) {
            return ServerResponse.error("Not found user save survey");
        }

        SurveyEntity survey = new SurveyEntity();
        if (isNewSurvey) {
            survey.setCreatedBy(user);
        } else {
            survey = repository.getById(surveyId);
            survey.setUpdatedBy(user);
        }
        survey.setTitle(saveSurveyReq.getTitle());
        survey.setDescription(saveSurveyReq.getDescription());
        survey.setIsDeleted(false);

        repository.save(survey);

        saveQuestionOfSurvey(survey, saveSurveyReq);
        return ServerResponse.SUCCESS;

    }

    private void saveQuestionOfSurvey(SurveyEntity survey, SaveSurveyReq saveSurveyReq) {
        Long surveyId = survey.getId();
        List<SurveyQuestionEntity> listQuestionIbDb = surveyQuestionService.getListQuestionOfSurvey(surveyId);
        Map<Long, SurveyQuestionEntity> mapQuestionInDbById = new HashMap<>();
        if (!listQuestionIbDb.isEmpty()) {
            mapQuestionInDbById = listQuestionIbDb.stream()
                    .collect(Collectors.toMap(SurveyQuestionEntity::getId, question -> question));
        }

        List<SaveSurveyQuestionReq> listQuestionReq = saveSurveyReq.getListQuestion();
        if (listQuestionReq.isEmpty()) {
            return;
        }
        List<SurveyQuestionEntity> listSurveyQuestionNeedSave = new ArrayList<>();
        Map<SaveSurveyQuestionReq, SurveyQuestionEntity> mapEntityByReq = new HashMap<>();
        for (SaveSurveyQuestionReq questionReq : listQuestionReq) {
            Long questionId = questionReq.getQuestionId();
            SurveyQuestionEntity surveyQuestion = new SurveyQuestionEntity();
            if (questionId != null) {
                surveyQuestion = mapQuestionInDbById.get(questionReq.getQuestionId());
            }
            surveyQuestion.setSurvey(survey);
            surveyQuestion.setQuestionText(questionReq.getQuestionText());
            surveyQuestion.setQuestionType(questionReq.getTypeQuestion());
            listSurveyQuestionNeedSave.add(surveyQuestion);
            mapEntityByReq.put(questionReq, surveyQuestion);
        }

        surveyQuestionService.saveAll(listSurveyQuestionNeedSave);
        List<Long> listQuestionId = listSurveyQuestionNeedSave.stream().
                map(SurveyQuestionEntity::getId).collect(Collectors.toList());

        saveOptionOfQuestion(mapEntityByReq, listQuestionId);
    }

    private void saveOptionOfQuestion(Map<SaveSurveyQuestionReq, SurveyQuestionEntity> mapEntityByReq,
                                      List<Long> listQuestionId) {
        List<SurveyOptionEntity> listOptionNeedSave = new ArrayList<>();
        List<Long> listOptionIdNeedDelete = new ArrayList<>();
        List<SurveyOptionEntity> listAllOptionInDb = surveyOptionService.getListAllOptionOfQuestion(listQuestionId);
        Map<Long, List<SurveyOptionEntity>> mapOptionInDbByQuestionId = new HashMap<>();
        if (!listAllOptionInDb.isEmpty()) {
            mapOptionInDbByQuestionId = listAllOptionInDb.stream()
                    .collect(Collectors.groupingBy(option -> option.getQuestion().getId()));
        }

        for (Map.Entry<SaveSurveyQuestionReq, SurveyQuestionEntity> entry : mapEntityByReq.entrySet()) {
            SaveSurveyQuestionReq questionReq = entry.getKey();
            SurveyQuestionEntity questionEntity = entry.getValue();

            List<SaveSurveyOptionReq> listOptionReq = questionReq.getListOption();
            TypeQuestion typeQuestion = questionEntity.getQuestionType();
            if (typeQuestion == TypeQuestion.FILL_TEXT || listOptionReq.isEmpty()) {
                continue;
            }
            Long questionId = questionEntity.getId();
            List<SurveyOptionEntity> listOptionInDb = mapOptionInDbByQuestionId.get(questionId);
            Map<Long, SurveyOptionEntity> mapOptionOfOneQuestionById = listOptionInDb.stream()
                    .collect(Collectors.toMap(SurveyOptionEntity::getId, option -> option));
            List<Long> listOptionIdOfQuestionInDb = listOptionInDb.isEmpty() ? new ArrayList<>() :
                    listOptionInDb.stream()
                            .map(SurveyOptionEntity::getId).collect(Collectors.toList());
            List<Long> listOptionIdInReq = listOptionReq.stream()
                    .map(SaveSurveyOptionReq::getOptionId).collect(Collectors.toList());
            listOptionIdOfQuestionInDb.removeAll(listOptionIdInReq);
            listOptionIdNeedDelete.addAll(listOptionIdOfQuestionInDb);

            for (SaveSurveyOptionReq optionReq : listOptionReq) {
                Long optionId = optionReq.getOptionId();
                SurveyOptionEntity option = new SurveyOptionEntity();
                if (optionId != null) {
                    SurveyOptionEntity optionIdDb = mapOptionOfOneQuestionById.get(optionId);
                    if (optionIdDb != null) {
                        option = optionIdDb;
                    }
                }
                option.setQuestion(questionEntity);
                option.setOptionText(optionReq.getOptionText());
                listOptionNeedSave.add(option);
            }
        }
        if (!listOptionNeedSave.isEmpty()) {
            surveyOptionService.saveAll(listOptionNeedSave);
        }
        if (listOptionIdNeedDelete.isEmpty()) {
            surveyOptionRepository.deleteOptionByListOptionId(listOptionIdNeedDelete);

        }
    }
}
