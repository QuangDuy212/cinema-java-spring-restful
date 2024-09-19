package com.vn.cinema_internal_java_spring_rest.service;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.Time;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.show.ResShowDTO;
import com.vn.cinema_internal_java_spring_rest.repository.FilmRepository;
import com.vn.cinema_internal_java_spring_rest.repository.ShowRepository;
import com.vn.cinema_internal_java_spring_rest.repository.TimeRepository;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

@Service
public class ShowService {
    private final Logger log = LoggerFactory.getLogger(ShowService.class);
    private final ShowRepository showRepository;
    private final FilmRepository filmRepository;
    private final TimeRepository timeRepository;

    public ShowService(ShowRepository showRepository, FilmRepository filmRepository, TimeRepository timeRepository) {
        this.showRepository = showRepository;
        this.filmRepository = filmRepository;
        this.timeRepository = timeRepository;
    }

    public boolean isExists(int zNumber, String time, Time day, Film film) {
        return this.showRepository.existsByZoomNumberAndTimeAndDayAndFilm(zNumber, time, day, film);
    }

    public Show handleCreateAShow(Show reqShow) {
        log.debug("Request to create Show : {}", reqShow);
        if (reqShow.getFilm() != null) {
            Optional<Film> film = this.filmRepository.findById(reqShow.getFilm().getId());
            if (film.isPresent())
                reqShow.setFilm(film.get());
        }

        if (reqShow.getDay() != null) {
            long id = reqShow.getDay().getId();
            Optional<Time> timeShow = this.timeRepository.findById(id);
            if (timeShow.isPresent())
                reqShow.setDay(timeShow.get());
        }
        return this.showRepository.save(reqShow);
    }

    public Show fetchShowById(long id) {
        Optional<Show> sOptional = this.showRepository.findById(id);
        if (sOptional.isPresent())
            return sOptional.get();
        return null;
    }

    public ResultPaginationDTO fetchAllShows(Specification<Show> spe, Pageable page) {
        log.debug("Request to get all Categories");
        Page<Show> listShows = this.showRepository.findAll(spe, page);
        List<ResShowDTO> shows = listShows.stream().map(i -> this.convertShowToResShowDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listShows.getTotalPages());
        meta.setTotal(listShows.getTotalElements());

        res.setMeta(meta);
        res.setResult(shows);
        return res;
    }

    public Show handleUpdateAShow(Show reqShow) throws CommonException {
        Show currentShow = this.fetchShowById(reqShow.getId());
        if (reqShow.getTime() != null && !currentShow.getTime().equals(reqShow.getTime())
                && reqShow.getZoomNumber() > 0 && !(currentShow.getZoomNumber() == reqShow.getZoomNumber())
                || (reqShow.getTime() != null && !currentShow.getTime().equals(reqShow.getTime())
                        && reqShow.getZoomNumber() > 0)
                || (reqShow.getZoomNumber() > 0 && !(currentShow.getZoomNumber() == reqShow.getZoomNumber())
                        && reqShow.getTime() != null)) {
            boolean checkExist = this.isExists(reqShow.getZoomNumber(), reqShow.getTime(), reqShow.getDay(),
                    reqShow.getFilm());
            if (checkExist)
                throw new CommonException("Show existed");
            currentShow.setTime(reqShow.getTime());
            currentShow.setZoomNumber(reqShow.getZoomNumber());
        }

        if (reqShow.getTime() != null && !currentShow.getTime().equals(reqShow.getTime())
                && reqShow.getZoomNumber() == 0) {
            boolean checkExist = this.isExists(currentShow.getZoomNumber(), reqShow.getTime(), reqShow.getDay(),
                    reqShow.getFilm());
            if (checkExist)
                throw new CommonException("Show existed");
            currentShow.setTime(reqShow.getTime());
        }

        if (reqShow.getZoomNumber() > 0 && !(currentShow.getZoomNumber() == reqShow.getZoomNumber())
                && reqShow.getTime() == null) {
            boolean checkExist = this.isExists(reqShow.getZoomNumber(), currentShow.getTime(), reqShow.getDay(),
                    reqShow.getFilm());
            if (checkExist)
                throw new CommonException("Show existed");
            currentShow.setZoomNumber(reqShow.getZoomNumber());
        }

        if (reqShow.isActive() != currentShow.isActive()) {
            currentShow.setActive(reqShow.isActive());
        }

        if (reqShow.getFilm() != null) {
            Optional<Film> film = this.filmRepository.findById(reqShow.getFilm().getId());
            if (film.isPresent())
                currentShow.setFilm(film.get());
        }
        return this.showRepository.save(currentShow);
    }

    public void handleDeleteAShow(long id) {
        Show show = this.fetchShowById(id);
        show.setActive(false);
        this.showRepository.save(show);
    }

    public ResShowDTO convertShowToResShowDTO(Show show) {
        ResShowDTO res = new ResShowDTO();
        res.setId(show.getId());
        res.setZoomNumber(show.getZoomNumber());
        res.setPrice(show.getPrice());
        res.setTime(show.getTime());
        ResShowDTO.Day day = new ResShowDTO.Day();
        day.setId(show.getDay().getId());
        day.setDate(show.getDay().getDate());
        res.setDay(day);
        if (show.getFilm() != null) {
            ResShowDTO.FilmShow film = new ResShowDTO.FilmShow();
            film.setId(show.getFilm().getId());
            film.setName(show.getFilm().getName());
            res.setFilm(film);
        }
        res.setActive(show.isActive());
        return res;
    }

    public List<Show> fetchShowsByFilmAndDay(long idFilm, long idTime) {
        Optional<Film> film = this.filmRepository.findById(idFilm);
        Optional<Time> time = this.timeRepository.findById(idTime);
        if (film.isPresent() && time.isPresent())
            return this.showRepository.findByFilmAndDay(film.get(), time.get());
        return null;
    }
}
