package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.SupportProgramEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SupportProgramRepository extends BaseRepository<SupportProgramEntity> {

    List<SupportProgramEntity> findAllByIsDeletedFalse();

    @Query(value = "select s from SupportProgramEntity s " +
            "where s.isDeleted = false " +
            "and (?1 is null or (s.title like concat('%', ?1, '%')) or (s.title like concat('%', ?1, '%')))")
    Page<SupportProgramEntity> getPageSupportProgramClient(String keyword, Pageable pageable);

    @Query(value = "select s from SupportProgramEntity s " +
            "where  (?1 is null or (s.title like concat('%', ?1, '%')) or (s.title like concat('%', ?1, '%')))")
    Page<SupportProgramEntity> getPageSupportProgramAdmin(String keyword, Pageable pageable);

    @Modifying
    @Query(value = "update SupportProgramEntity s " +
            "set s.isDeleted = true " +
            "where s.id = ?1")
    void changeStatusSupportProgram(Long supportProgramId);
}
