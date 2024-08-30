package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.dto.seat.ResSeatDTO;
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

    public SeatService(SeatRepository seatRepository, ShowRepository showRepository) {
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
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

    public boolean checkExistByListName(List<SeatNameEnum> listNames) {
        return this.seatRepository.existsByNameIn(listNames);
    }

    public List<Seat> handleCreateSeat(List<Seat> reqListSeat) {
        for (Seat seat : reqListSeat) {
            Optional<Show> show = this.showRepository.findById(seat.getShow().getId());
            if (show.isPresent())
                seat.setShow(show.get());
            seat.setActive(true);
        }
        return this.seatRepository.saveAll(reqListSeat);
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
        show.setFilm(seat.getShow().getFilm());
        res.setShow(show);
        return res;
    }

    public void handleDeleteListSeats(List<Seat> listSeats) {
        List<Long> listIds = listSeats.stream().map(i -> i.getId())
                .collect(Collectors.toList());
        this.seatRepository.deleteAllById(listIds);
    }
}
