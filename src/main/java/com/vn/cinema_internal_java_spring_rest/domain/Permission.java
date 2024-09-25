package com.vn.cinema_internal_java_spring_rest.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank(message = "Name must not empty")
    private String name;

    @NotBlank(message = "Apipath must not empty")
    private String apiPath;

    @NotBlank(message = "Method must not empty")
    private String method;

    @NotBlank(message = "Module must not empty")
    private String module;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissions")
    @JsonIgnore
    private List<Role> roles;

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

    public Permission(@NotBlank(message = "Name must not empty") String name,
            @NotBlank(message = "Apipath must not empty") String apiPath,
            @NotBlank(message = "Method must not empty") String method,
            @NotBlank(message = "Module must not empty") String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

}
