package com.vn.cinema_internal_java_spring_rest.domain.dto.film;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResFilmDTO {
    private long id;
    private String name;
    private String director;
    private String image;
    private String performer;
    private String premiere;// time first show
    private String shortDesc;
    private String contentModeration;
    private long duration;
    private String trailer;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    private CategoryFilm category;

    @Getter
    @Setter
    public static class CategoryFilm {
        private long id;
        private String name;
    }
}