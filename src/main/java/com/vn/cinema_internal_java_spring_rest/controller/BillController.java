package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.Bill;
import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.dto.bill.ResBillDTO;
import com.vn.cinema_internal_java_spring_rest.service.BillService;
import com.vn.cinema_internal_java_spring_rest.service.SeatService;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class BillController {
    private final Logger log = LoggerFactory.getLogger(BillController.class);
    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/bills")
    @ApiMessage(value = "Create a Bill success")
    public ResponseEntity<ResBillDTO> createACategory(@Valid @RequestBody Bill reqBill) throws CommonException {
        log.debug("REST request to create Bill : {}", reqBill);
        Bill bill = this.billService.handleCreateABill(reqBill);
        ResBillDTO res = this.billService.converBillToResBillDTO(bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
