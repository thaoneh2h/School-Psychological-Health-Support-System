package com.be.custom.service.appointment;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveAppointmentDto;
import com.be.custom.dto.response_api.ReportAppointmentRes;
import com.be.custom.entity.AppointmentEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.StatusBookingAppointment;
import com.be.custom.repository.AppointmentRepository;
import com.be.custom.repository.UserRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService extends BaseService<AppointmentEntity, AppointmentRepository> {

    private final UserService userService;
    private final UserRepository userRepository;

    @Transactional
    public ServerResponse bookingPsychologist(SaveAppointmentDto appointmentDto) {
        if (appointmentDto.getStudentId() == null || appointmentDto.getPsychologistId() == null) {
            return ServerResponse.ERROR;
        }

        Optional<AppointmentEntity> appointmentOpt =
                repository.getAppointBetweenTimeInDb(appointmentDto.getStartTime(), appointmentDto.getEndTime());
        if (appointmentOpt.isPresent()) {
            return ServerResponse.ERROR;
        }

        UserEntity student = userService.findById(appointmentDto.getStudentId());
        UserEntity psychologist = userService.findById(appointmentDto.getPsychologistId());
        UserEntity parent = null;
        if (appointmentDto.getParentId() != null) {
            parent = userService.findById(appointmentDto.getParentId());
        }
        if (student == null || psychologist == null) {
            return ServerResponse.ERROR;
        }

        AppointmentEntity appointment = getAppointmentEntity(appointmentDto, psychologist, student, parent);

        save(appointment);
        return ServerResponse.SUCCESS;

    }

    private static @NotNull AppointmentEntity getAppointmentEntity(SaveAppointmentDto appointmentDto, UserEntity psychologist,
                                                                   UserEntity student, UserEntity parent) {
        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setAppointmentDate(new Date());
        appointment.setPsychologist(psychologist);
        appointment.setStudent(student);
        appointment.setParent(parent);
        appointment.setNotes(appointmentDto.getNotes());
        appointment.setStartTime(appointment.getStartTime());
        appointment.setEndTime(appointment.getEndTime());
        appointment.setStatus(StatusBookingAppointment.SCHEDULED);
        appointment.setCreatedAt(new Date());
        appointment.setUpdatedAt(new Date());
        appointment.setCreatedBy(parent != null ? parent : student);
        appointment.setUpdatedBy(parent != null ? parent : student);
        appointment.setIsDeleted(false);
        return appointment;
    }

    public List<AppointmentEntity> getHistoryBooking(List<Long> listStudentId) {
        if (listStudentId.isEmpty()) {
            return Collections.emptyList();
        }

        return repository.getListAppointmentOfStudent(listStudentId);
    }

    public List<AppointmentEntity> getAllBookingOfStudentFromParent(Long parentId) {
        List<Long> listStudentIdOfParent = userRepository.getListStudentIdOfParent(parentId);
        return getHistoryBooking(listStudentIdOfParent);
    }

    public List<ReportAppointmentRes> getReportAppointment(List<Long> listStudentId) {
        List<AppointmentEntity> listAppointmentInDb = repository.getListAppointmentOfStudent(listStudentId);
        return listAppointmentInDb.stream().map(appointment ->
                ReportAppointmentRes.builder()
                        .studentId(appointment.getStudent().getId())
                        .studentName(appointment.getStudent().getFullName())
                        .parentId(appointment.getParent().getId())
                        .parentName(appointment.getParent().getFullName())
                        .psychologistId(appointment.getPsychologist().getId())
                        .psychologistName(appointment.getPsychologist().getFullName())
                        .report(appointment.getReport())
                        .timeUpload(appointment.getUpdatedAt())
                        .timeAppointment(appointment.getAppointmentDate())
                        .build()
        ).collect(Collectors.toList());
    }

    public List<ReportAppointmentRes> getListAllReportFromParent(Long parentId) {
        List<Long> listStudentIdOfParent = userRepository.getListStudentIdOfParent(parentId);
        if (listStudentIdOfParent.isEmpty()) {
            return null;
        }

        return getReportAppointment(listStudentIdOfParent);
    }

    public List<ReportAppointmentRes> getReportOfOneStudentFromParent(Long parentId, Long studentId) {
        List<Long> listStudentIdOfParent = userRepository.getListStudentIdOfParent(parentId);
        if (listStudentIdOfParent.isEmpty() || !listStudentIdOfParent.contains(studentId)) {
            return null;
        }

        List<Long> listStudentNeedFind = List.of(studentId);
        return getReportAppointment(listStudentNeedFind);
    }

    public Page<AppointmentEntity> getPageAppointment(Pageable pageable, String keyword) {
        return repository.getPageSurveyHistory(keyword, pageable);
    }

    public Page<ReportAppointmentRes> getReportAppointment(String keyword, Pageable pageable) {
        Page<AppointmentEntity> pageAppointmentInDb = repository.getPageSurveyHistory(keyword, pageable);
        return pageAppointmentInDb.map(appointment ->
                ReportAppointmentRes.builder()
                        .studentId(appointment.getStudent().getId())
                        .studentName(appointment.getStudent().getFullName())
                        .parentId(appointment.getParent().getId())
                        .parentName(appointment.getParent().getFullName())
                        .psychologistId(appointment.getPsychologist().getId())
                        .psychologistName(appointment.getPsychologist().getFullName())
                        .report(appointment.getReport())
                        .timeUpload(appointment.getUpdatedAt())
                        .timeAppointment(appointment.getAppointmentDate())
                        .build()
        );
    }

}
