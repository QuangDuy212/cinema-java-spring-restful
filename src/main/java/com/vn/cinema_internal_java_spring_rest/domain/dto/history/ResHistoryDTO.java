package com.vn.cinema_internal_java_spring_rest.domain.dto.history;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResHistoryDTO {
    private long id;
    private double total;
    private long quantity;
    private int zoomNumber;
    private String date;
    private String show;
    private String email;
    private String seats;
    private String nameFilm;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
}
