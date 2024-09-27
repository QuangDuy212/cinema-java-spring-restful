package com.vn.cinema_internal_java_spring_rest.domain.dto.bill;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.util.List;

import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BillSendEmailDTO implements Serializable {
    private double total;
    private long quantity;
    private int zoomNumber;
    private String date;
    private String time;
    private String seats;
    private String nameFilm;
    private BillUser user;

    @Getter
    @Setter
    public static class BillUser {
        private String email;
    }

}
