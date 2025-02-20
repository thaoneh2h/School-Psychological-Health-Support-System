package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.UserEntity;
import com.be.custom.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<UserEntity> {

    Optional<UserEntity> findByEmailAndIsDeletedFalse(String email);

    Optional<UserEntity> findByUserIdAndIsDeletedFalse(Long id);

    @Query(value = "select u from UserEntity u " +
            "where u.isDeleted = false " +
            "and (?1 is null or (u.fullName like concat('%', ?1, '%')) or (u.fullName like concat('%', ?1, '%'))) " +
            "and (u.role in ?2) ")
    Page<UserEntity> getPageAdmin(String keyword, List<Role> listRoleFilter, Pageable pageable);

    List<UserEntity> findAllByUserIdIn(Collection<Long> ids);

    UserEntity findByUsername(String username);

}
