package com.vn.cinema_internal_java_spring_rest.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "shows")
@Getter
@Setter
public class Show {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 1, message = "Minium is 1")
    private int zoomNumber;

    private Instant time;

    private double price;

    private boolean isActive;

    @ManyToOne
    @JoinColumn(name = "film_id")
    private Film film;

    @OneToMany(mappedBy = "show")
    private List<Seat> seats;

}
