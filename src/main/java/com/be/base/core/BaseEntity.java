package com.be.base.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseEntity {

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    protected Date createdTime;

    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    protected Date updatedTime;

    @Transient
    protected String creator;

    @Transient
    protected String updater;

    protected Long createdBy;
    protected Long updatedBy;

    @JsonIgnore
    protected boolean isDeleted;

    public abstract Long getId();
}
