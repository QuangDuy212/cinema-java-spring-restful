package com.vn.cinema_internal_java_spring_rest.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.cinema_internal_java_spring_rest.domain.Time;

@Repository
public interface TimeRepository extends JpaRepository<Time, Long>, JpaSpecificationExecutor<Time> {
    boolean existsByDate(String date);

    List<Time> findByIdIn(List<Long> listIds);
}
