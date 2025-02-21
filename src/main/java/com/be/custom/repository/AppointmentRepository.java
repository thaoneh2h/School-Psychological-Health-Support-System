package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends BaseRepository<AppointmentEntity> {

    @Query(value = "select a from AppointmentEntity a " +
            "where a.isDeleted = false " +
            "and a.student.id in ?1")
    List<AppointmentEntity> getListAppointmentOfStudent(List<Long> ListStudentId);

    @Query(value = "SELECT a FROM AppointmentEntity a " +
            "WHERE (a.startTime < ?2 AND a.endTime > ?1) " +
            "AND a.isDeleted = false")
    Optional<AppointmentEntity> getAppointBetweenTimeInDb(Date startTime, Date endTime);


}
