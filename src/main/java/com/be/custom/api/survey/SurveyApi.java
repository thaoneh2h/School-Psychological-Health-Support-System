package com.be.custom.api.survey;

import com.be.base.dto.ServerResponse;
import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.dto.request.SaveAnswerSurveyRequest;
import com.be.custom.dto.request.SaveSurveyReq;
import com.be.custom.dto.response_api.DetailAnswerDto;
import com.be.custom.dto.response_api.QuestionOfSurveyDto;
import com.be.custom.entity.SurveyEntity;
import com.be.custom.entity.SurveyHistoryEntity;
import com.be.custom.service.survey.SurveyHistoryService;
import com.be.custom.service.survey.SurveyResponseService;
import com.be.custom.service.survey.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

@RestController
@RequestMapping("/survey")
@RequiredArgsConstructor
public class SurveyApi {

    private final SurveyService surveyService;
    private final SurveyResponseService surveyResponseService;
    private final SurveyHistoryService surveyHistoryService;

    @GetMapping("/get-all-survey")
    public ResponseEntity<List<SurveyEntity>> getAllSurvey() {
        return ResponseEntity.ok(surveyService.getAllSurvey());
    }

    @GetMapping("/get-data-survey")
    public ResponseEntity<List<QuestionOfSurveyDto>> getDataOfSurvey(@RequestParam Long surveyId) {
        return ResponseEntity.ok(surveyService.getListQuestionOfSurvey(surveyId));
    }

    @PostMapping("/save-data-answer")
    public ResponseEntity<ServerResponse> saveAnswerSurvey(@RequestBody List<SaveAnswerSurveyRequest> dataAnswer,
                                                           @RequestParam Long surveyId,
                                                           @ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(surveyResponseService.saveAnswerSurvey(userId, dataAnswer, surveyId));
    }

    @GetMapping("/get-history-survey-from-student")
    public ResponseEntity<List<SurveyHistoryEntity>> getHistorySurveyFromStudent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long studentId = userDetails.getUserId();
        List<Long> listStudentId = List.of(studentId);
        return ResponseEntity.ok(surveyHistoryService.getListHistorySurveyByListStudentId(listStudentId));
    }

    @GetMapping("/get-history-survey-from-parent")
    public ResponseEntity<List<SurveyHistoryEntity>> getHistorySurveyFromParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long parentId = userDetails.getUserId();
        return ResponseEntity.ok(surveyHistoryService.getListHistorySurveyFromParent(parentId));
    }

    @GetMapping("/get-detail-history-of-survey")
    public ResponseEntity<List<DetailAnswerDto>> getDetailHistory(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                  @RequestParam Long surveyId) {
        Long studentId = userDetails.getUserId();
        return ResponseEntity.ok(surveyHistoryService.getDetailSurvey(surveyId, studentId));
    }

    @PostMapping("/save-survey")
    public ResponseEntity<ServerResponse> saveSurvey(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @RequestBody SaveSurveyReq saveSurveyReq) {
        Long userId = userDetails.getUserId();
        return ResponseEntity.ok(surveyService.saveSurvey(saveSurveyReq, userId));
    }

}
