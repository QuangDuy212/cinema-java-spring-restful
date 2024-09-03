package com.vn.cinema_internal_java_spring_rest.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.Time;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
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
        if (reqTime.getFilms() != null) {
            List<Long> listIds = reqTime.getFilms().stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Film> listFilms = this.filmRepository.findByIdIn(listIds);
            reqTime.setFilms(listFilms);
        }
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
        if (reqTime.getFilms() != null) {
            List<Long> listIds = reqTime.getFilms().stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Film> listFilms = this.filmRepository.findByIdIn(listIds);
            time.setFilms(listFilms);
        }
        return time;
    }

    public void handleDeleteTime(Time reqTime) {
        this.timeRepository.deleteById(reqTime.getId());
    }
}
