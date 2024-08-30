package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.Permission;
import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.film.ResFilmDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.seat.ResSeatDTO;
import com.vn.cinema_internal_java_spring_rest.service.SeatService;
import com.vn.cinema_internal_java_spring_rest.service.ShowService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class SeatController {
    private final Logger log = LoggerFactory.getLogger(SeatController.class);

    private final SeatService seatService;
    private final ShowService showService;

    public SeatController(SeatService seatService, ShowService showService) {
        this.seatService = seatService;
        this.showService = showService;
    }

    @PostMapping("/seats")
    @ApiMessage(value = "Create seats success")
    public ResponseEntity<List<ResSeatDTO>> createListSeat(@Valid @RequestBody List<Seat> reqListSeats)
            throws CommonException {
        log.debug("REST request to create Seat : {}", reqListSeats);
        // List<SeatNameEnum> listNames = reqListSeats.stream().map(i -> i.getName())
        // .collect(Collectors.toList());
        List<SeatNameEnum> names = new ArrayList<SeatNameEnum>();
        for (Seat seat : reqListSeats) {
            names.add(seat.getName());
        }
        boolean checkExistsListName = this.seatService.checkExistByListName(names);
        if (checkExistsListName)
            throw new CommonException("Name seat existed");
        List<Seat> seats = this.seatService.handleCreateSeat(reqListSeats);

        List<ResSeatDTO> res = seats.stream().map(i -> this.seatService.convertSeatToResSeatDTO(i))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    @DeleteMapping("/seats")
    @ApiMessage(value = "Delete seats success")
    public ResponseEntity<Void> deleteListSeats(@RequestBody List<Seat> reqListSeats) throws CommonException {
        log.debug("REST request to delete Seats : {}", reqListSeats);
        List<Long> listIds = new ArrayList<Long>();
        for (Seat seat : reqListSeats) {
            listIds.add(seat.getId());
        }
        boolean checkExistsByListIds = this.seatService.checkExistByListIds(listIds);
        if (!checkExistsByListIds)
            throw new CommonException("Seat not found");
        this.seatService.handleDeleteListSeats(reqListSeats);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/seats/by-show")
    @ApiMessage(value = "Fetch seats by show success")
    public ResponseEntity<ResultPaginationDTO> getListSeatsByShow(@RequestBody Show reqShow, Pageable page)
            throws CommonException {
        log.debug("REST request to get Seats : {}", reqShow);
        Show show = this.showService.fetchShowById(reqShow.getId());
        if (show == null)
            throw new CommonException("Show not found");
        ResultPaginationDTO res = this.seatService.fetchListSeatsByShow(reqShow, page);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/seats")
    @ApiMessage(value = "Fetch all seats success")
    public ResponseEntity<ResultPaginationDTO> fetchAllSeat(@Filter Specification<Seat> spe, Pageable page)
            throws CommonException {
        log.debug("REST request to fetch all Seats");
        ResultPaginationDTO res = this.seatService.fetchAllSeats(spe, page);
        return ResponseEntity.ok().body(res);
    }

    @GetMapping("/seats/{id}")
    @ApiMessage(value = "Fetch seat by id success")
    public ResponseEntity<ResSeatDTO> fetchSeatById(@PathVariable("id") long id)
            throws CommonException {
        log.debug("REST request to fetch Seat by id : {}", id);
        Seat seat = this.seatService.fetchSeatById(id);
        if (seat == null)
            throw new CommonException("Seat not found");
        ResSeatDTO res = this.seatService.convertSeatToResSeatDTO(seat);
        return ResponseEntity.ok().body(res);
    }
}
