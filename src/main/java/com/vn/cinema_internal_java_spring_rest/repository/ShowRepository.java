package com.vn.cinema_internal_java_spring_rest.repository;

import java.time.Instant;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.cinema_internal_java_spring_rest.domain.Show;

@Repository
public interface ShowRepository extends JpaRepository<Show, Long>, JpaSpecificationExecutor<Show> {
    boolean existsByZoomNumberAndTime(int zoomNumber, String time);

    List<Show> findByIdIn(List<Long> listIds);
}
