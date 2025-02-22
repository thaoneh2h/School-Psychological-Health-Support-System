package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "Resources")
@Getter
@Setter
public class ResourceEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String filePath;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "UploadedBy", nullable = false)
    private UserEntity uploadedBy;

    @Column(nullable = false)
    private LocalDateTime uploadedAt = LocalDateTime.now();

    @Column(nullable = false)
    private Date updatedAt = new Date();

    @Override
    public Long getId() {
        return id;
    }
}
