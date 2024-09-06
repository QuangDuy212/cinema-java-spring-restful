package com.vn.cinema_internal_java_spring_rest.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.Time;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, JpaSpecificationExecutor<Show> {
    boolean existsByZoomNumberAndTimeAndDayAndFilm(int zoomNumber, String time, Time day, Film film);

    List<Show> findByIdIn(List<Long> listIds);

    List<Show> findByFilmAndDay(Film film, Time day);
}
