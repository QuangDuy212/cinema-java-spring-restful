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
import com.vn.cinema_internal_java_spring_rest.domain.dto.bill.BillSendEmailDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.bill.ResBillDTO;
import com.vn.cinema_internal_java_spring_rest.service.EmailService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class EmailController {
    private final Logger log = LoggerFactory.getLogger(EmailController.class);
    private final EmailService emailService;

    public EmailController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/email/bill")
    @ApiMessage(value = "Send bill to user's email success")
    public ResponseEntity<ResBillDTO> sendBillToUserEmail(@RequestBody BillSendEmailDTO bill) throws CommonException {
        log.debug("REST request to send bill to user's email : {}", bill);
        this.emailService.sendBillToEmail("duy2k4ml1234@gmail.com", "Bill", "bill", bill);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
