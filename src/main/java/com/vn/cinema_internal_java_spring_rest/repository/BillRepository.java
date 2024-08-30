package com.vn.cinema_internal_java_spring_rest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.vn.cinema_internal_java_spring_rest.domain.Bill;
import com.vn.cinema_internal_java_spring_rest.domain.User;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long>, JpaSpecificationExecutor<Bill> {
    List<Bill> findByUser(User user);
}
