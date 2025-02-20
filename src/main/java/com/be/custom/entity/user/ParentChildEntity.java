package com.be.custom.entity.user;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "ParentChildren")
@Getter
@Setter
public class ParentChildEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentChildId;

    @ManyToOne
    @JoinColumn(name = "ParentID", nullable = false)
    private UserEntity parent;

    @ManyToOne
    @JoinColumn(name = "ChildID", nullable = false)
    private UserEntity child;

    @Column(nullable = false)
    private String relationship;
}
