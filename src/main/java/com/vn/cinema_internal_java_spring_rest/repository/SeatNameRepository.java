package com.vn.cinema_internal_java_spring_rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.cinema_internal_java_spring_rest.domain.SeatName;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import java.util.List;

@Repository
public interface SeatNameRepository extends JpaRepository<SeatName, Long>, JpaSpecificationExecutor<SeatName> {
    boolean existsByNameIn(List<SeatNameEnum> names);
}
