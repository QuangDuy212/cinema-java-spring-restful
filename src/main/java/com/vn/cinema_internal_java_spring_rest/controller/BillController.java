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
import com.vn.cinema_internal_java_spring_rest.domain.Bill;
import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.bill.ResBillDTO;
import com.vn.cinema_internal_java_spring_rest.service.BillService;
import com.vn.cinema_internal_java_spring_rest.service.SeatService;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

import java.util.List;

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

    @GetMapping("/bills/{id}")
    @ApiMessage(value = "Fetch Bill success")
    public ResponseEntity<ResBillDTO> fetchBillById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get a Bill by id : {}", id);
        Bill bill = this.billService.fetchBillById(id);
        if (bill == null) {
            throw new CommonException("Bill not found");
        }
        ResBillDTO res = this.billService.converBillToResBillDTO(bill);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/bills")
    @ApiMessage(value = "Fetch Bill success")
    public ResponseEntity<ResultPaginationDTO> fetchAllBills(@Filter Specification<Bill> spe, Pageable page)
            throws CommonException {
        log.debug("REST request to get all Bills");
        ResultPaginationDTO res = this.billService.fetchAllBills(spe, page);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/bills/by-user")
    @ApiMessage(value = "Fetch Bill by user success")
    public ResponseEntity<List<ResBillDTO>> fetchBillsByUser() throws CommonException {
        log.debug("REST request to get all Bills");
        List<ResBillDTO> res = this.billService.fetchListBillsByUser();
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/bills")
    @ApiMessage(value = "Update a Bill success")
    public ResponseEntity<ResBillDTO> updateABill(@RequestBody Bill reqBill) throws CommonException {
        log.debug("REST request to update BIll : {}", reqBill);
        Bill bill = this.billService.fetchBillById(reqBill.getId());
        if (bill == null) {
            throw new CommonException("Bill not found");
        }
        Bill updatedBill = this.billService.handleUpdateABill(reqBill);
        ResBillDTO res = this.billService.converBillToResBillDTO(updatedBill);
        return ResponseEntity.ok().body(res);
    }

    @DeleteMapping("/bills/{id}")
    @ApiMessage(value = "Delete a Bill success")
    public ResponseEntity<Void> deleteABill(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to delete Bill by Id: {}", id);
        Bill bill = this.billService.fetchBillById(id);
        if (bill == null)
            throw new CommonException("Bill not found");
        this.billService.handleDeleteBill(id);
        return ResponseEntity.ok().body(null);
    }
}
