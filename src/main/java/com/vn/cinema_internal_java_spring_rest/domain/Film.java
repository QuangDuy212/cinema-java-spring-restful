package com.vn.cinema_internal_java_spring_rest.domain;

import java.time.Instant;

import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "films")
@Getter
@Setter
public class Film {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String name;
    private String director;
    private String image;
    private String performer;
    private Instant premiere;// time first show
    private String shortDetail;
    private String contentModeration;
    private Instant showTime;
    private long duration;
    private String trailer;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    private void handleBeforeCreate() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.createdBy = email;
        this.createdAt = Instant.now();
    }

    @PreUpdate
    private void handleBeforeUpdate() {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        this.updatedBy = email;
        this.updatedAt = Instant.now();
    }
}
