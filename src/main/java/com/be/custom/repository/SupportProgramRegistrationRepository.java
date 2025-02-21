package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.ProgramRegistrationEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportProgramRegistrationRepository extends BaseRepository<ProgramRegistrationEntity> {

    @Query(value = "select p from ProgramRegistrationEntity p " +
            "where p.user.id = ?1 " +
            "and p.isDeleted = false")
    List<ProgramRegistrationEntity> findAllByUserIdAndIsDeletedFalse(Long userId);
}
