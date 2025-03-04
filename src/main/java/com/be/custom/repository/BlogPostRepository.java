package com.be.custom.repository;

import com.be.base.core.BaseRepository;
import com.be.custom.entity.BlogPostEntity;

import java.util.List;

public interface BlogPostRepository extends BaseRepository<BlogPostEntity> {
    List<BlogPostEntity> findAllByIsDeletedFalse();
}
