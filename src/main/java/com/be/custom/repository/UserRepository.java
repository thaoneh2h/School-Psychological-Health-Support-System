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

    Optional<UserEntity> findByIdAndIsDeletedFalse(Long id);

    @Query(value = "select u from UserEntity u " +
            "where u.isDeleted = false " +
            "and (?1 is null or (u.fullName like concat('%', ?1, '%')) or (u.fullName like concat('%', ?1, '%'))) " +
            "and (u.role in ?2) ")
    Page<UserEntity> getPageAdmin(String keyword, List<Role> listRoleFilter, Pageable pageable);

    List<UserEntity> findAllByIdIn(Collection<Long> ids);

    UserEntity findByEmail(String username);

    @Query(value = "select u from UserEntity u " +
            "join ParentChildEntity pc on u.id = pc.child.id " +
            "where pc.parent.id = ?1 " +
            "and u.isDeleted = false")
    List<UserEntity> getListStudentOfParent(Long parentId);

    @Query(value = "select u.id from UserEntity u " +
            "join ParentChildEntity pc on u = pc.child " +
            "where pc.parent.id = ?1 " +
            "and u.isDeleted = false")
    List<Long> getListStudentIdOfParent(Long parentId);

    @Query(value = "select u from UserEntity u " +
            "where u.isDeleted = false " +
            "and u.role =?1")
    List<UserEntity> getListUserByRole(Role role);

}
