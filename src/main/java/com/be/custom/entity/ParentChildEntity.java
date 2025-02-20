package com.be.custom.entity;

import com.be.base.core.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ParentChildren")
@Getter
@Setter
public class ParentChildEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ParentID", nullable = false)
    private UserEntity parent;

    @ManyToOne
    @JoinColumn(name = "ChildID", nullable = false)
    private UserEntity child;

    @Column(nullable = false)
    private String relationship;

    @Override
    public Long getId() {
        return id;
    }
}
