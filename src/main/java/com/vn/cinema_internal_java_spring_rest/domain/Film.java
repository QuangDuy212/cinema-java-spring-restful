package com.vn.cinema_internal_java_spring_rest.domain;

import java.time.Instant;
import java.util.List;

import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @Min(value = 60, message = "Minium time is 60")
    private long duration;

    @NotBlank(message = "Trailer must not empty")
    private String trailer;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "film")
    private List<Show> shows;

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
