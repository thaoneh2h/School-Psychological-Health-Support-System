package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.ProgramRegistrationEntity;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportProgramRegistrationRepository extends BaseRepository<ProgramRegistrationEntity> {

    @Query(value = "select p from ProgramRegistrationEntity p " +
            "where p.user.id = ?1 " +
            "and p.isDeleted = false")
    List<ProgramRegistrationEntity> findAllByUserIdAndIsDeletedFalse(Long userId);

    @Query(value = "SELECT pr FROM ProgramRegistrationEntity pr " +
            "WHERE (:keyword IS NULL OR pr.user.fullName LIKE %:keyword% " +
            "OR pr.program.title LIKE %:keyword% " +
            "OR pr.program.description LIKE %:keyword%) " +
            "AND pr.isDeleted = false")
    Page<ProgramRegistrationEntity> getPageProgramRegistration(@Param("keyword") String keyword, Pageable pageable);

//    @Query(value = "SELECT pr FROM ProgramRegistrationEntity pr " +
//            "WHERE (:keyword IS NULL OR pr.user.fullName LIKE %:keyword% " +
//            "OR pr.program.title LIKE %:keyword% " +
//            "OR pr.program.description LIKE %:keyword%) " +
//            "AND pr.isDeleted = false " +
//            "AND pr.program.")
//    Page<ProgramRegistrationEntity> getPageProgramRegistrationFromPsychologist(@Param("keyword") String keyword,
//                                                                               Pageable pageable,
//                                                                               Long psychologistId);
}
