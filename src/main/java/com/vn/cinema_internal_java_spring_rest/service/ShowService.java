package com.vn.cinema_internal_java_spring_rest.service;

import java.time.Instant;
import java.util.Optional;
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
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.repository.FilmRepository;
import com.vn.cinema_internal_java_spring_rest.repository.ShowRepository;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

@Service
public class ShowService {
    private final Logger log = LoggerFactory.getLogger(ShowService.class);
    private final ShowRepository showRepository;
    private final FilmRepository filmRepository;

    public ShowService(ShowRepository showRepository, FilmRepository filmRepository) {
        this.showRepository = showRepository;
        this.filmRepository = filmRepository;
    }

    public boolean isExistsByZoomNumberAndTime(int zNumber, String time) {
        return this.showRepository.existsByZoomNumberAndTime(zNumber, time);
    }

    public Show handleCreateAShow(Show reqShow) {
        log.debug("Request to create Show : {}", reqShow);
        if (reqShow.getFilm() != null) {
            Optional<Film> film = this.filmRepository.findById(reqShow.getFilm().getId());
            if (film.isPresent())
                reqShow.setFilm(film.get());
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
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listShows.getTotalPages());
        meta.setTotal(listShows.getTotalElements());

        res.setMeta(meta);
        res.setResult(listShows.getContent());
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
            boolean checkExist = this.isExistsByZoomNumberAndTime(reqShow.getZoomNumber(), reqShow.getTime());
            if (checkExist)
                throw new CommonException("Show existed");
            currentShow.setTime(reqShow.getTime());
            currentShow.setZoomNumber(reqShow.getZoomNumber());
        }

        if (reqShow.getTime() != null && !currentShow.getTime().equals(reqShow.getTime())
                && reqShow.getZoomNumber() == 0) {
            boolean checkExist = this.isExistsByZoomNumberAndTime(currentShow.getZoomNumber(), reqShow.getTime());
            if (checkExist)
                throw new CommonException("Show existed");
            currentShow.setTime(reqShow.getTime());
        }

        if (reqShow.getZoomNumber() > 0 && !(currentShow.getZoomNumber() == reqShow.getZoomNumber())
                && reqShow.getTime() == null) {
            boolean checkExist = this.isExistsByZoomNumberAndTime(reqShow.getZoomNumber(), currentShow.getTime());
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

    public void handleDeleteAShow(Show reqShow) {
        this.showRepository.deleteById(reqShow.getId());
    }
}
