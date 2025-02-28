package com.be.custom.api.appointment_api;

import com.be.base.dto.ServerResponse;
import com.be.custom.common.security.CustomUserDetails;
import com.be.custom.dto.request.SaveAppointmentDto;
import com.be.custom.dto.response_api.ReportAppointmentRes;
import com.be.custom.entity.AppointmentEntity;
import com.be.custom.service.appointment.AppointmentService;
import com.be.custom.utils.PageUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/appointment")
@RequiredArgsConstructor
public class AppointmentApi {

    private final AppointmentService appointmentService;

    @PostMapping("/booking-from-student")
    public ResponseEntity<ServerResponse> bookingFromStudent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                             @RequestBody SaveAppointmentDto saveAppointmentDto) {
        Long studentId = userDetails.getUserId();
        saveAppointmentDto.setStudentId(studentId);
        return ResponseEntity.ok(appointmentService.bookingPsychologist(saveAppointmentDto));
    }

    @PostMapping("/booking-from-parent")
    public ResponseEntity<ServerResponse> bookingFromParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                            @RequestBody SaveAppointmentDto saveAppointmentDto) {
        Long parentId = userDetails.getUserId();
        saveAppointmentDto.setParentId(parentId);
        return ResponseEntity.ok(appointmentService.bookingPsychologist(saveAppointmentDto));
    }

    @GetMapping("/get-history-booking-from-student")
    public ResponseEntity<List<AppointmentEntity>> getHistoryBookingFromStudent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long studentId = userDetails.getUserId();
        List<Long> listStudentId = Collections.singletonList(studentId);
        return ResponseEntity.ok(appointmentService.getHistoryBooking(listStudentId));
    }

    @GetMapping("/get-booking-all-student-from-parent")
    public ResponseEntity<List<AppointmentEntity>> getAllBookingFromParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long parentId = userDetails.getUserId();
        return ResponseEntity.ok(appointmentService.getAllBookingOfStudentFromParent(parentId));
    }

    @GetMapping("/get-history-booking-of-student-from-parent")
    public ResponseEntity<List<AppointmentEntity>> getHistoryBookingOfStudentFromParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                        @RequestParam Long studentId) {
        List<Long> listStudentId = Collections.singletonList(studentId);
        return ResponseEntity.ok(appointmentService.getHistoryBooking(listStudentId));
    }

    @GetMapping("/get-report-form-student")
    public ResponseEntity<List<ReportAppointmentRes>> getReportFormStudent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long studentId = userDetails.getUserId();
        List<Long> listStudentId = List.of(studentId);
        return ResponseEntity.ok(appointmentService.getReportAppointment(listStudentId));
    }

    @GetMapping("/get-all-report-form-parent")
    public ResponseEntity<List<ReportAppointmentRes>> getAllReportFormParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long parentId = userDetails.getUserId();
        return ResponseEntity.ok(appointmentService.getListAllReportFromParent(parentId));
    }

    @GetMapping("/get-report-of-one-student-form-parent")
    public ResponseEntity<List<ReportAppointmentRes>> getReportOfStudentFormParent(@ApiIgnore @AuthenticationPrincipal CustomUserDetails userDetails,
                                                                                   @RequestParam Long studentId) {
        Long parentId = userDetails.getUserId();
        return ResponseEntity.ok(appointmentService.getReportOfOneStudentFromParent(parentId, studentId));
    }

    @GetMapping("/get-page-booking-for-admin")
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    public ResponseEntity<?> getPageBookingForAdmin(@RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "50") int size,
                                                    @RequestParam(defaultValue = "createdTime", required = false) String sortField,
                                                    @RequestParam(defaultValue = "desc") String sortDir,
                                                    @RequestParam String keywordSearch) {
        Pageable pageable = PageUtils.from(sortDir, sortField, page, size);
        return ResponseEntity.ok(appointmentService.getPageAppointment(pageable, keywordSearch));
    }

    @GetMapping("/get-page-report-for-admin")
    @PreAuthorize("@authorizationService.isSystemAdmin()")
    public ResponseEntity<?> getPageReportForAdmin(@RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "50") int size,
                                                   @RequestParam(defaultValue = "createdTime", required = false) String sortField,
                                                   @RequestParam(defaultValue = "desc") String sortDir,
                                                   @RequestParam String keywordSearch) {
        Pageable pageable = PageUtils.from(sortDir, sortField, page, size);
        return ResponseEntity.ok(appointmentService.getReportAppointment(keywordSearch, pageable));
    }
}
