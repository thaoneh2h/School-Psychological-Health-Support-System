package com.be.custom.api.survey;

import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveUserDto;
import com.be.custom.entity.SurveyEntity;
import com.be.custom.service.UserService;
import com.be.custom.service.survey.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyApi {

    private final SurveyService surveyService;

    @PostMapping("/get-all-survey")
    public ResponseEntity<List<SurveyEntity>> getAllSurvey() {
        return ResponseEntity.ok(surveyService.getAllSurvey());
    }
    
    
}
