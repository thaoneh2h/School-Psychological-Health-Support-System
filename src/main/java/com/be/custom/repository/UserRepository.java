package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.user.UserEntity;
import com.be.custom.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends BaseRepository<UserEntity> {

    Optional<UserEntity> findByUsernameAndIsDeletedFalse(String username);

    Optional<UserEntity> findByIdAndIsDeletedFalseAndActiveTrue(Long id);

    @Query(value = "select u from UserEntity u " +
            "where u.isDeleted = false " +
            "and (?1 is null or (u.name like concat('%', ?1, '%')) or (u.username like concat('%', ?1, '%'))) " +
            "and (u.role in ?2) ")
    Page<UserEntity> getPageAdmin(String keyword, List<Role> listRoleFilter, Pageable pageable);

    List<UserEntity> findAllByIdIn(Collection<Long> ids);

    UserEntity findByUsername(String username);

}
