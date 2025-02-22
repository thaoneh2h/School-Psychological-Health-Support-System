package com.be.custom.dto.response_api;

import com.be.custom.entity.SurveyOptionEntity;
import com.be.custom.enums.TypeQuestion;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class QuestionOfSurveyDto {
    private Long surveyId;
    private Long questionId;
    private String questionText;
    private TypeQuestion typeQuestion;
    private List<SurveyOptionEntity> listOption;
}
