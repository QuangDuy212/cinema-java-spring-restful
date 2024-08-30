package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.SeatName;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.seat.ResSeatDTO;
import com.vn.cinema_internal_java_spring_rest.repository.SeatNameRepository;
import com.vn.cinema_internal_java_spring_rest.util.constant.SeatNameEnum;

import java.util.List;
import java.util.ArrayList;

@Service
public class SeatNameService {
    private final SeatNameRepository seatNameRepository;

    public SeatNameService(SeatNameRepository seatNameRepository) {
        this.seatNameRepository = seatNameRepository;
    }

    public boolean checkExistByListName(List<SeatNameEnum> names) {
        return this.seatNameRepository.existsByNameIn(names);
    }

    public List<SeatName> handleCreateNames() {
        List<SeatName> names = new ArrayList<SeatName>();
        for (SeatNameEnum name : SeatNameEnum.values()) {
            SeatName newName = new SeatName();
            newName.setName(name);
            names.add(newName);
        }
        return this.seatNameRepository.saveAll(names);
    }

    public ResultPaginationDTO fetchAllSeatNames(Specification<SeatName> spe, Pageable page) {
        Page<SeatName> listSeatNames = this.seatNameRepository.findAll(spe, page);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listSeatNames.getTotalPages());
        meta.setTotal(listSeatNames.getTotalElements());

        res.setMeta(meta);
        res.setResult(listSeatNames.getContent());
        return res;
    }

}
