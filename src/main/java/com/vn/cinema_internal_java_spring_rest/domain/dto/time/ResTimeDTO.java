package com.vn.cinema_internal_java_spring_rest.domain.dto.time;

import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ResTimeDTO {
    private long id;
    private String date;
    private List<Show> shows;

    @Getter
    @Setter
    public static class Show {
        private long id;
        private String time;
    }
}
