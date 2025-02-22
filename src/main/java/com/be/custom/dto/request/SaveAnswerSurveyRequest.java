package com.be.custom.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SaveAnswerSurveyRequest {
    private Long questionId;
    private List<Long> listOptionId;
    private String answerText;
}
