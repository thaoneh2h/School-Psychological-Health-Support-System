package com.be.base.core;

import com.be.custom.entity.user.UserEntity;
import com.be.custom.repository.UserRepository;
import com.be.custom.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public abstract class BaseService<E extends BaseEntity, R extends BaseRepository> {

    @Autowired
    protected R repository;

    @Autowired
    private UserRepository userRepository;

    /**
     * Create
     */
    public E save(E entity) {
        if (entity == null) {
            return null;
        }

        preSave(entity);

        return (E) repository.save(entity);
    }

    @Transactional
    public void saveAll(Collection<E> listEntity) {
        listEntity.forEach(entity -> preSave(entity));
        repository.saveAll(listEntity);
    }

    public void preSave(E entity) {
        /**
         * gen UUID code
         */

        if (entity.getId() == null) {
            entity.setCreatedTime(new Date());
        }
        entity.setUpdatedTime(new Date());
    }

    /**
     * Service Read
     */
    public E findById(Long id) {
        Object entity = repository.findById(id).orElse(null);

        if (entity == null) {
            return null;
        }

        return (E) entity;
    }

    /**
     * Update
     */
    public E update(E entity) {
        if (entity == null || entity.getId() == null) {
            return null;
        } else {
            entity.setUpdatedTime(new Date());
            return (E) repository.save(entity);
        }
    }


    /**
     * Get findAll
     *
     * @param page
     * @return
     */
    public Page<E> findAll(Pageable page, boolean isDeleted) {
        return repository.findByIsDeleted(page, isDeleted);
    }

    public void setCreatorAndUpdater(Page<E> page) {
        if (page.isEmpty()) {
            return;
        }
        Set<Long> listUserId = page.stream()
                .flatMap(e -> Stream.of(e.getCreatedBy(), e.getUpdatedBy()))
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, String> mapNameUserById = userRepository
                .findAllByIdIn(listUserId)
                .stream()
                .collect(Collectors.toMap(UserEntity::getId, UserEntity::getName));
        page.forEach(e -> {
                    e.setCreator(mapNameUserById.getOrDefault(e.getCreatedBy(), Constants.EMPTY_STRING));
                    e.setUpdater(mapNameUserById.getOrDefault(e.getUpdatedBy(), Constants.EMPTY_STRING));
                }
        );
    }

}
