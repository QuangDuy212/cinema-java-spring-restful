package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.Time;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.TimeService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class TimeController {

    private final Logger log = LoggerFactory.getLogger(TimeController.class);
    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @PostMapping("/times")
    @ApiMessage(value = "Create a Time success")
    public ResponseEntity<Time> createATime(@Valid @RequestBody Time reqTime) throws CommonException {
        log.debug("REST request to create Time : {}", reqTime);
        boolean checkExist = this.timeService.checkExistsByDate(reqTime.getDate());
        if (checkExist)
            throw new CommonException("Time existed");
        Time time = this.timeService.handleCreateTime(reqTime);
        return ResponseEntity.status(HttpStatus.CREATED).body(time);
    }

    @GetMapping("/times/{id}")
    @ApiMessage(value = "Fetch Time success")
    public ResponseEntity<Time> fetchTimeById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get a Time by id : {}", id);
        Time time = this.timeService.fetchTimeById(id);
        if (time == null) {
            throw new CommonException("Time not found");
        }
        return ResponseEntity.ok().body(time);
    }

    @GetMapping("/times")
    @ApiMessage(value = "Fetch all Times success")
    public ResponseEntity<ResultPaginationDTO> fetchAllTimes(@Filter Specification<Time> spe, Pageable page) {
        log.debug("REST request to get all Times ");
        ResultPaginationDTO res = this.timeService.fetchAllTimes(spe, page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/times")
    @ApiMessage(value = "Update a Time success")
    public ResponseEntity<Time> updateATime(@RequestBody Time reqTime) throws CommonException {
        log.debug("REST request to update Time : {}", reqTime);
        Time time = this.timeService.fetchTimeById(reqTime.getId());
        if (time == null) {
            throw new CommonException("Time not found");
        }
        if (reqTime.getDate() != null) {
            boolean checkDate = this.timeService.checkExistsByDate(reqTime.getDate());
            if (checkDate)
                throw new CommonException("Time date is existd");
        }
        Time updatedTime = this.timeService.handleUpdateTime(reqTime);
        return ResponseEntity.ok().body(updatedTime);
    }

    @DeleteMapping("/times")
    @ApiMessage(value = "Delete a Time success")
    public ResponseEntity<Void> deleteATime(@RequestBody Time reqTime) throws CommonException {
        log.debug("REST request to delete Time : {}", reqTime);
        Time time = this.timeService.fetchTimeById(reqTime.getId());
        if (time == null)
            throw new CommonException("Time not found");
        this.timeService.handleDeleteTime(reqTime);
        return ResponseEntity.ok().body(null);
    }
}