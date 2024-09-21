package com.vn.cinema_internal_java_spring_rest.domain;

import java.time.Instant;
import java.util.List;

import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;
import com.vn.cinema_internal_java_spring_rest.util.constant.StatusBillEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "histories")
@Getter
@Setter
public class History {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private double total;

    private long quantity;

    private int zoomNumber;

    @NotBlank(message = "Date must not empty")
    private String date;

    @NotBlank(message = "Time must not empty")
    private String time;

    private String seats;

    @NotBlank(message = "Name film must not empty")
    private String nameFilm;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

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
