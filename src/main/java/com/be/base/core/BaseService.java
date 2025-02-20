package com.be.base.core;

import com.be.custom.entity.UserEntity;
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
            entity.setCreatedAt(new Date());
        }
        entity.setUpdatedAt(new Date());
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
            entity.setUpdatedAt(new Date());
            return (E) repository.save(entity);
        }
    }
}
