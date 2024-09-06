package com.vn.cinema_internal_java_spring_rest.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.Time;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.time.ResTimeDTO;
import com.vn.cinema_internal_java_spring_rest.repository.FilmRepository;
import com.vn.cinema_internal_java_spring_rest.repository.TimeRepository;

@Service
public class TimeService {
    private final Logger log = LoggerFactory.getLogger(TimeService.class);
    private final TimeRepository timeRepository;
    private final FilmRepository filmRepository;

    public TimeService(TimeRepository timeRepository, FilmRepository filmRepository) {
        this.timeRepository = timeRepository;
        this.filmRepository = filmRepository;
    }

    public boolean checkExistsByDate(String date) {
        return this.timeRepository.existsByDate(date);
    }

    public Time handleCreateTime(Time reqTime) {
        log.debug("Request to create a User: {}", reqTime);
        return this.timeRepository.save(reqTime);
    }

    public Time fetchTimeById(long id) {
        Optional<Time> time = this.timeRepository.findById(id);
        log.debug("Request to get User by id: {}", id);
        if (time.isPresent())
            return time.get();
        return null;
    }

    public ResultPaginationDTO fetchAllTimes(Specification<Time> spe, Pageable page) {
        log.debug("Request to get all Times");
        Page<Time> listTimes = this.timeRepository.findAll(spe, page);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listTimes.getTotalPages());
        meta.setTotal(listTimes.getTotalElements());

        res.setMeta(meta);
        res.setResult(listTimes.getContent());
        return res;
    }

    public Time handleUpdateTime(Time reqTime) {
        Time time = this.fetchTimeById(reqTime.getId());
        if (reqTime.getDate() != null && !this.checkExistsByDate(reqTime.getDate())) {
            time.setDate(reqTime.getDate());
        }
        return time;
    }

    public void handleDeleteTime(Time reqTime) {
        this.timeRepository.deleteById(reqTime.getId());
    }

    public ResTimeDTO convertTimeToResTimeDTo(Time time) {
        ResTimeDTO res = new ResTimeDTO();
        res.setId(time.getId());
        res.setDate(time.getDate());
        for (Show show : time.getShows()) {
            ResTimeDTO.Show item = new ResTimeDTO.Show();
            item.setId(show.getId());
            item.setTime(show.getTime());
        }
        return res;
    }
}
