package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "BlogPosts")
@Getter
@Setter
public class BlogPostEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    private UserEntity author;

    @Column(nullable = false)
    private Date createdAt = new Date();

    @Column(nullable = false)
    private Date updatedAt = new Date();

    @Column(nullable = false)
    private Boolean isDeleted = false;

    @Override
    public Long getId() {
        return id;
    }
}
