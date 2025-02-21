package com.be.custom.service.appointment;

import com.be.base.core.BaseService;
import com.be.base.dto.ServerResponse;
import com.be.custom.dto.request.SaveAppointmentDto;
import com.be.custom.entity.AppointmentEntity;
import com.be.custom.entity.ProgramRegistrationEntity;
import com.be.custom.entity.SupportProgramEntity;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.StatusBookingAppointment;
import com.be.custom.enums.StatusProgramRegistration;
import com.be.custom.repository.AppointmentRepository;
import com.be.custom.repository.SupportProgramRegistrationRepository;
import com.be.custom.repository.SupportProgramRepository;
import com.be.custom.repository.UserRepository;
import com.be.custom.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
}
