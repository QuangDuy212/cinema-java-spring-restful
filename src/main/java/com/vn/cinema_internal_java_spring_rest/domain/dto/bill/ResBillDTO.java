package com.vn.cinema_internal_java_spring_rest.domain.dto.bill;

import java.util.List;

import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;
import com.vn.cinema_internal_java_spring_rest.util.constant.StatusBillEnum;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class ResBillDTO {
    private long id;
    private double total;
    private long quantity;
    private StatusBillEnum status;
    private BillUser user;
    private List<BillSeat> seats;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @Getter
    @Setter
    public static class BillUser {
        private String email;
    }

    @Getter
    @Setter
    public static class BillSeat {
        private long id;
        private SeatNameEnum name;
    }

}
