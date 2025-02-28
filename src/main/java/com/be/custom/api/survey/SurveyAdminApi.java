package com.be.custom.api.survey;

import com.be.custom.service.survey.SurveyHistoryService;
import com.be.custom.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey-admin")
@PreAuthorize("@authorizationService.isSystemAdmin()")
public class SurveyAdminApi {

    private final SurveyHistoryService surveyHistoryService;

    @GetMapping("/get-page-survey")
    public ResponseEntity<?> getPageSurvey(@RequestParam(defaultValue = "1") int page,
                                             @RequestParam(defaultValue = "50") int size,
                                             @RequestParam(defaultValue = "createdTime", required = false) String sortField,
                                             @RequestParam(defaultValue = "desc") String sortDir,
                                             @RequestParam String keywordSearch) {
        Pageable pageable = PageUtils.from(sortDir, sortField, page, size);
        return ResponseEntity.ok(surveyHistoryService.getPageSurveyHistory(pageable, keywordSearch));
    }
}
