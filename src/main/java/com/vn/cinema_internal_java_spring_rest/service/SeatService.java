package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.repository.SeatRepository;
import com.vn.cinema_internal_java_spring_rest.repository.ShowRepository;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    public SeatService(SeatRepository seatRepository, ShowRepository showRepository) {
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
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

}
