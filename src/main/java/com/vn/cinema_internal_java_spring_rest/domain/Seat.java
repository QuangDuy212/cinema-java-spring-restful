package com.vn.cinema_internal_java_spring_rest.domain;

import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "seats")
@Getter
@Setter
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Enumerated(EnumType.STRING)
    private SeatNameEnum name;
    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "show_id", nullable = false)
    private Show show;
}
