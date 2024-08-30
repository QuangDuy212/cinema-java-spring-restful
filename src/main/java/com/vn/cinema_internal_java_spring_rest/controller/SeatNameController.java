package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.SeatName;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.SeatNameService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import java.util.List;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1")
public class SeatNameController {
    private final Logger log = LoggerFactory.getLogger(SeatNameController.class);
    private final SeatNameService seatNameService;

    public SeatNameController(SeatNameService seatNameService) {
        this.seatNameService = seatNameService;
    }

    @PostMapping("/names")
    @ApiMessage(value = "Create Names success")
    public ResponseEntity<List<SeatName>> createNames() throws CommonException {
        log.debug("REST request to create Names");
        List<SeatNameEnum> listNames = new ArrayList<SeatNameEnum>();
        for (SeatNameEnum name : SeatNameEnum.values()) {
            listNames.add(name);
        }
        boolean checkExist = this.seatNameService.checkExistByListName(listNames);
        if (checkExist)
            throw new CommonException("List name have created");
        List<SeatName> seatNames = this.seatNameService.handleCreateNames();
        return ResponseEntity.status(HttpStatus.CREATED).body(seatNames);
    }

    @GetMapping("/names")
    @ApiMessage(value = "Fetch all Seat Names success")
    public ResponseEntity<ResultPaginationDTO> fetchAllShows(@Filter Specification<SeatName> spe, Pageable page) {
        log.debug("REST request to get all Shows ");
        ResultPaginationDTO res = this.seatNameService.fetchAllSeatNames(spe, page);
        return ResponseEntity.ok().body(res);
    }

}
