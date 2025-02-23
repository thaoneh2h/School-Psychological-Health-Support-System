package com.be.base.core;

import com.be.custom.entity.UserEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
public abstract class BaseEntity {

    @JsonIgnore
    @Transient
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    protected Date createdAt;

    @JsonIgnore
    @Transient
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    protected Date updatedAt;

    @JsonIgnore
    @Transient
    protected String creator;

    @JsonIgnore
    @Transient
    protected String updater;
    
    public abstract Long getId();
}
