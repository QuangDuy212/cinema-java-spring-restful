package com.vn.cinema_internal_java_spring_rest.domain.dto.show;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResShowDTO {
    private long id;
    private int zoomNumber;
    private double price;
    private Day day;

    @Getter
    @Setter
    public static class Day {
        private long id;
        private String date;
    }
}
