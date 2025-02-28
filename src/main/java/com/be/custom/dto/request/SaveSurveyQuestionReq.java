package com.be.custom.dto.request;

import com.be.custom.enums.TypeQuestion;
import lombok.Data;

import java.util.List;

@Data
public class SaveSurveyQuestionReq {
    private Long questionId;
    private String questionText;
    private TypeQuestion typeQuestion;
    private List<SaveSurveyOptionReq> listOption;
}
