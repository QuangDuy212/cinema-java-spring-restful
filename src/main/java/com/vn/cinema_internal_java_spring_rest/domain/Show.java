package com.vn.cinema_internal_java_spring_rest.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private String time;

    private double price;

    private boolean isActive;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "shows")
    @JsonIgnore
    private List<Film> films;

    @OneToMany(mappedBy = "show")
    private List<Seat> seats;

}
