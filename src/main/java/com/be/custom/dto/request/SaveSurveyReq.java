package com.be.custom.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SaveSurveyReq {
    private Long surveyId;
    private String title;
    private String description;
    private List<SaveSurveyQuestionReq> listQuestion;
}
