package com.vn.cinema_internal_java_spring_rest.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long>, JpaSpecificationExecutor<Seat> {
    boolean existsByIdIn(List<Long> listIds);

    boolean existsByNameIn(List<SeatNameEnum> names);

    boolean existsByNameAndShow(SeatNameEnum name, Show show);

    Page<Seat> findByShow(Show show, Pageable page);

    List<Seat> findByIdIn(List<Long> listIds);
}
