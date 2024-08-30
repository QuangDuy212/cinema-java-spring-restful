package com.vn.cinema_internal_java_spring_rest.domain.dto.seat;

import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResSeatDTO {
    private long id;
    private SeatNameEnum name;
    private boolean isActive;
    private ShowSeat show;

    @Getter
    @Setter
    public static class ShowSeat {
        private long id;
        private int zoomNumber;
        private double price;
        private Film film;
        private boolean isActive;
        private Instant time;
    }
}