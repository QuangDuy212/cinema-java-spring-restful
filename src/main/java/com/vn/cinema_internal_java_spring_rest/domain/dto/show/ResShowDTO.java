package com.vn.cinema_internal_java_spring_rest.domain.dto.show;

import java.util.List;

import com.vn.cinema_internal_java_spring_rest.domain.Seat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResShowDTO {
    private long id;
    private int zoomNumber;
    private double price;
    private List<Seat> seats;
    private TimeShow timeShow;

    @Getter
    @Setter
    public static class TimeShow {
        private long id;
        private String date;
    }
}
