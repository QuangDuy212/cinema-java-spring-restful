package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.History;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.film.ResFilmDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.history.ResHistoryDTO;
import com.vn.cinema_internal_java_spring_rest.service.HistoryService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class HistoryController {
    private final Logger log = LoggerFactory.getLogger(HistoryController.class);
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping("/histories")
    @ApiMessage(value = "Create a history success")
    public ResponseEntity<ResHistoryDTO> createUser(@Valid @RequestBody History reqHis) throws CommonException {
        log.debug("REST request to create History : {}", reqHis);
        History his = this.historyService.handleCreateHistory(reqHis);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.historyService.convertHisToResHistoryDTO(his));
    }

    @GetMapping("/histories/{id}")
    @ApiMessage(value = "Fetch a History success")
    public ResponseEntity<ResHistoryDTO> fetchHistoryById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get a History by id : {}", id);
        History his = this.historyService.fetchHistoryById(id);
        if (his == null) {
            throw new CommonException("History not found");
        }
        return ResponseEntity.ok().body(this.historyService.convertHisToResHistoryDTO(his));
    }

    @GetMapping("/histories")
    @ApiMessage(value = "Fetch all History success")
    public ResponseEntity<ResultPaginationDTO> fetchAllHistories(@Filter Specification<History> spe, Pageable page) {
        log.debug("REST request to get all Films ");
        ResultPaginationDTO res = this.historyService.fetchAllHistories(spe, page);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/histories/by-user")
    @ApiMessage(value = "Fetch History by user success")
    public ResponseEntity<ResultPaginationDTO> fetchHistoriesByUser(@Filter Specification<History> spe, Pageable page) {
        log.debug("REST request to get histories by user ");
        ResultPaginationDTO res = this.historyService.fetchHistoryByUser(spe, page);
        return ResponseEntity.ok().body(res);
    }
}
