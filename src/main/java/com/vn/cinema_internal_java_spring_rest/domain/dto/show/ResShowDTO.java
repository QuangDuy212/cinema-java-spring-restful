package com.vn.cinema_internal_java_spring_rest.domain.dto.show;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResShowDTO {
    private long id;
    private int zoomNumber;
    private double price;
    private String time;
    private Day day;
    private FilmShow film;
    private boolean active;

    @Getter
    @Setter
    public static class Day {
        private long id;
        private String date;
    }

    @Getter
    @Setter
    public static class FilmShow {
        private long id;
        private String name;
    }
}
