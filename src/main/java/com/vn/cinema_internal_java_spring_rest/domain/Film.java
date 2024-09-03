package com.vn.cinema_internal_java_spring_rest.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "Name must not empty")
    private String name;

    @NotBlank(message = "Director must not empty")
    private String director;

    @NotBlank(message = "Image must not empty")
    private String image;

    @NotBlank(message = "Performer must not empty")
    private String performer;

    @NotBlank(message = "Premiere must not empty")
    private String premiere;// time first show

    @NotBlank(message = "ShortDetail must not empty")
    @Column(columnDefinition = "MEDIUMTEXT")
    private String shortDesc;

    @NotBlank(message = "ContentModeration must not empty")
    private String contentModeration;

    private long duration;

    @NotBlank(message = "Trailer must not empty")
    private String trailer;
    @NotBlank(message = "Origin must not empty")
    private String origin;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    // @JsonIgnoreProperties(value = { "roles" })
    @JoinTable(name = "show_film", joinColumns = @JoinColumn(name = "film_id"), inverseJoinColumns = @JoinColumn(name = "show_id"))
    private List<Show> shows;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "films")
    @JsonIgnore
    private List<Time> times;

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
