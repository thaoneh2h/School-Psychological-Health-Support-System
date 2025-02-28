package com.be.custom.dto.request;

import lombok.Data;

@Data
public class SaveSurveyOptionReq {
    private Long optionId;
    private String optionText;
}
