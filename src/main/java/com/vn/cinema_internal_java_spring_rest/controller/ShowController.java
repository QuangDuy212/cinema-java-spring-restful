package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.ShowService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class ShowController {
    private final Logger log = LoggerFactory.getLogger(ShowService.class);
    private final ShowService showService;

    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @PostMapping("/shows")
    @ApiMessage(value = "Create a Show success")
    public ResponseEntity<Show> createAShow(@Valid @RequestBody Show reqShow) throws CommonException {
        log.debug("REST request to create Show : {}", reqShow);
        boolean checkExist = this.showService.isExistsByZoomNumberAndTime(reqShow.getZoomNumber(), reqShow.getTime());
        if (checkExist) {
            throw new CommonException("Show existed");
        }
        Show show = this.showService.handleCreateAShow(reqShow);
        return ResponseEntity.status(HttpStatus.CREATED).body(show);
    }

    @GetMapping("/shows/{id}")
    @ApiMessage(value = "Fetch Show success")
    public ResponseEntity<Show> fetchShowById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get a Show by id : {}", id);
        Show show = this.showService.fetchShowById(id);
        if (show == null) {
            throw new CommonException("Show not found");
        }

        return ResponseEntity.ok().body(show);
    }

    @GetMapping("/shows")
    @ApiMessage(value = "Fetch all Shows success")
    public ResponseEntity<ResultPaginationDTO> fetchAllShows(Pageable page) {
        log.debug("REST request to get all Shows ");
        ResultPaginationDTO res = this.showService.fetchAllShows(page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/shows")
    @ApiMessage(value = "Update a Show success")
    public ResponseEntity<Show> updateAShow(@RequestBody Show reqShow) throws CommonException {
        log.debug("REST request to update Show : {}", reqShow);
        Show show = this.showService.fetchShowById(reqShow.getId());
        if (show == null) {
            throw new CommonException("Show not found");
        }
        Show updatedShow = this.showService.handleUpdateAShow(reqShow);
        return ResponseEntity.ok().body(updatedShow);
    }

}
