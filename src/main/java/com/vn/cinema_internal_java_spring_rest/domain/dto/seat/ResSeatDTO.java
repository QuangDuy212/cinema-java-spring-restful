package com.vn.cinema_internal_java_spring_rest.domain.dto.seat;

import java.util.List;

import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import lombok.Getter;
import lombok.Setter;

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
        private FilmSeat film;
        private boolean isActive;
        private String time;
    }

    @Getter
    @Setter
    public static class FilmSeat {
        private long id;
        private String name;
    }
}
