package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Bill;
import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.seat.ResSeatDTO;
import com.vn.cinema_internal_java_spring_rest.repository.BillRepository;
import com.vn.cinema_internal_java_spring_rest.repository.SeatRepository;
import com.vn.cinema_internal_java_spring_rest.repository.ShowRepository;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;
    private final BillRepository billRepository;

    public SeatService(SeatRepository seatRepository, ShowRepository showRepository, BillRepository billRepository) {
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
        this.billRepository = billRepository;
    }

    public boolean checkExistByListIds(List<Long> listIds) {
        for (Long id : listIds) {
            boolean exist = true;
            exist = this.seatRepository.existsById(id);
            if (!exist)
                return false;
        }
        return true;
    }

    public boolean checkExist(SeatNameEnum name, Show show) {
        return this.seatRepository.existsByNameAndShow(name, show);
    }

    public Seat handleCreateSeat(Seat reqSeat) {
        Optional<Show> show = this.showRepository.findById(reqSeat.getShow().getId());
        if (show.isPresent())
            reqSeat.setShow(show.get());
        reqSeat.setActive(true);
        if (reqSeat.getBill() != null) {
            Optional<Bill> bill = this.billRepository.findById(reqSeat.getBill().getId());
            if (bill.isPresent())
                reqSeat.setBill(bill.get());
        }
        return this.seatRepository.save(reqSeat);
    }

    public ResSeatDTO convertSeatToResSeatDTO(Seat seat) {
        ResSeatDTO res = new ResSeatDTO();
        res.setId(seat.getId());
        res.setName(seat.getName());
        res.setActive(seat.isActive());
        ResSeatDTO.ShowSeat show = new ResSeatDTO.ShowSeat();
        show.setId(seat.getShow().getId());
        show.setZoomNumber(seat.getShow().getZoomNumber());
        show.setPrice(seat.getShow().getPrice());
        show.setActive(seat.getShow().isActive());
        show.setTime(seat.getShow().getTime());
        if (seat.getShow().getFilm() != null) {
            ResSeatDTO.FilmSeat film = new ResSeatDTO.FilmSeat();
            film.setId(seat.getShow().getFilm().getId());
            film.setName(seat.getShow().getFilm().getName());
            show.setFilm(film);
        }
        res.setShow(show);
        return res;
    }

    public void handleDeleteListSeats(List<Seat> listSeats) {
        List<Long> listIds = listSeats.stream().map(i -> i.getId())
                .collect(Collectors.toList());
        this.seatRepository.deleteAllById(listIds);
    }

    public ResultPaginationDTO fetchListSeatsByShow(long id, Pageable page) {
        Optional<Show> reqShow = this.showRepository.findById(id);
        Page<Seat> listSeats = this.seatRepository.findByShow(reqShow.get(), page);
        List<ResSeatDTO> listResSeats = listSeats.getContent()
                .stream().map(i -> this.convertSeatToResSeatDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listSeats.getTotalPages());
        meta.setTotal(listSeats.getTotalElements());

        res.setMeta(meta);
        res.setResult(listResSeats);
        return res;

    }

    public ResultPaginationDTO fetchAllSeats(Specification<Seat> spe, Pageable page) {
        Page<Seat> listSeats = this.seatRepository.findAll(spe, page);
        List<ResSeatDTO> listResSeats = listSeats.getContent()
                .stream().map(i -> this.convertSeatToResSeatDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listSeats.getTotalPages());
        meta.setTotal(listSeats.getTotalElements());

        res.setMeta(meta);
        res.setResult(listResSeats);
        return res;
    }

    public Seat fetchSeatById(long id) {
        Optional<Seat> seat = this.seatRepository.findById(id);
        if (seat.isPresent())
            return seat.get();
        return null;
    }

}
