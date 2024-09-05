package com.vn.cinema_internal_java_spring_rest.service;

import java.util.Optional;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.Show;
import com.vn.cinema_internal_java_spring_rest.domain.Time;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.film.ResFilmDTO;
import com.vn.cinema_internal_java_spring_rest.repository.CategoryRepository;
import com.vn.cinema_internal_java_spring_rest.repository.FilmRepository;
import com.vn.cinema_internal_java_spring_rest.repository.ShowRepository;
import com.vn.cinema_internal_java_spring_rest.repository.TimeRepository;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

@Service
public class FilmService {
    private final Logger log = LoggerFactory.getLogger(FilmService.class);
    private final FilmRepository filmRepository;
    private final CategoryRepository categoryRepository;
    private final ShowRepository showRepository;
    private final TimeRepository timeRepository;

    public FilmService(FilmRepository filmRepository, CategoryRepository categoryRepository,
            ShowRepository showRepository, TimeRepository timeRepository) {
        this.filmRepository = filmRepository;
        this.categoryRepository = categoryRepository;
        this.showRepository = showRepository;
        this.timeRepository = timeRepository;
    }

    public boolean isExistsByNameAndDirector(String name, String director) {
        return this.filmRepository.existsByNameAndDirector(name, director);
    }

    public Film handlCreateAFilm(Film reqFilm) {
        log.debug("Request to save Film : {}", reqFilm);
        if (reqFilm.getShows() != null) {
            List<Long> listIds = reqFilm.getShows()
                    .stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Show> shows = this.showRepository.findByIdIn(listIds);
            reqFilm.setShows(shows);
        }
        if (reqFilm.getTimes() != null) {
            List<Long> listIds = reqFilm.getTimes()
                    .stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Time> times = this.timeRepository.findByIdIn(listIds);
            reqFilm.setTimes(times);
        }
        return this.filmRepository.save(reqFilm);
    }

    public Film fetchFilmById(long id) {
        log.debug("Request to fetch Film by id: {}", id);
        Optional<Film> fOptional = this.filmRepository.findById(id);
        if (fOptional.isPresent())
            return fOptional.get();
        return null;
    }

    public ResultPaginationDTO fetchAllFilms(Specification<Film> spe, Pageable page) {
        log.debug("Request to get all Films");
        Page<Film> listFilms = this.filmRepository.findAll(spe, page);
        List<ResFilmDTO> listResFilm = listFilms.getContent()
                .stream().map(i -> this.convertFilmToResFilmDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listFilms.getTotalPages());
        meta.setTotal(listFilms.getTotalElements());

        res.setMeta(meta);
        res.setResult(listResFilm);
        return res;
    }

    public Film handleUpdateAFilm(Film reqFilm) throws CommonException {
        Film currentFilm = this.fetchFilmById(reqFilm.getId());
        if ((reqFilm.getName() != null && reqFilm.getDirector() != null
                && !reqFilm.getName().equals(currentFilm.getName())
                && !reqFilm.getDirector().equals(currentFilm.getDirector()))
                || (reqFilm.getName() != null && reqFilm.getDirector() != null
                        && !reqFilm.getName().equals(currentFilm.getName()))
                || (reqFilm.getName() != null && reqFilm.getDirector() != null
                        && !reqFilm.getDirector().equals(currentFilm.getDirector()))) {
            boolean checkExist = this.isExistsByNameAndDirector(reqFilm.getName(), reqFilm.getDirector());
            if (checkExist)
                throw new CommonException("Film existed");
            currentFilm.setName(reqFilm.getName());
            currentFilm.setDirector(reqFilm.getDirector());
        }

        if (reqFilm.getName() != null && !reqFilm.getName().equals(currentFilm.getName())
                && reqFilm.getDirector() == null) {
            boolean checkExist = this.isExistsByNameAndDirector(reqFilm.getName(), currentFilm.getDirector());
            if (checkExist)
                throw new CommonException("Film existed");
            currentFilm.setName(reqFilm.getName());
        }

        if (reqFilm.getDirector() != null && !reqFilm.getDirector().equals(currentFilm.getDirector())
                && reqFilm.getName() == null) {
            boolean checkExist = this.isExistsByNameAndDirector(currentFilm.getName(), reqFilm.getDirector());
            if (checkExist)
                throw new CommonException("Film existed");
            currentFilm.setDirector(reqFilm.getDirector());
        }

        if (reqFilm.getImage() != null)
            currentFilm.setImage(reqFilm.getImage());
        if (reqFilm.getPerformer() != null)
            currentFilm.setPerformer(reqFilm.getPerformer());
        if (reqFilm.getPremiere() != null)
            currentFilm.setPremiere(reqFilm.getPremiere());
        if (reqFilm.getShortDesc() != null)
            currentFilm.setShortDesc(reqFilm.getShortDesc());
        if (reqFilm.getContentModeration() != null)
            currentFilm.setContentModeration(reqFilm.getContentModeration());
        if (reqFilm.getDuration() > 0)
            currentFilm.setDuration(reqFilm.getDuration());
        if (reqFilm.getTrailer() != null)
            currentFilm.setTrailer(reqFilm.getTrailer());
        if (reqFilm.getCategory() != null) {
            Optional<Category> category = this.categoryRepository.findById(reqFilm.getCategory().getId());
            if (category.isPresent())
                currentFilm.setCategory(category.get());
        }
        return this.filmRepository.save(currentFilm);
    }

    public ResFilmDTO convertFilmToResFilmDTO(Film film) {
        ResFilmDTO res = new ResFilmDTO();
        res.setId(film.getId());
        res.setName(film.getName());
        res.setDirector(film.getDirector());
        res.setImage(film.getImage());
        res.setPerformer(film.getPerformer());
        res.setPremiere(film.getPremiere());
        res.setShortDesc(film.getShortDesc());
        res.setContentModeration(film.getContentModeration());
        res.setDuration(film.getDuration() > 0 ? film.getDuration() : 0);
        res.setTrailer(film.getTrailer());
        res.setCreatedAt(film.getCreatedAt());
        res.setCreatedBy(film.getCreatedBy());
        res.setUpdatedAt(film.getUpdatedAt());
        res.setUpdatedBy(film.getUpdatedBy());
        res.setOrigin(film.getOrigin());
        res.setShows(film.getShows());
        List<Time> listTimes = film.getTimes();
        List<ResFilmDTO.TimeFilm> times = new ArrayList<ResFilmDTO.TimeFilm>();
        for (Time item : listTimes) {
            ResFilmDTO.TimeFilm time = new ResFilmDTO.TimeFilm();
            time.setId(item.getId());
            time.setDate(item.getDate());
            times.add(time);
        }
        res.setTimes(times);
        ResFilmDTO.CategoryFilm category = new ResFilmDTO.CategoryFilm();
        category.setId(film.getCategory() != null ? film.getCategory().getId() : null);
        category.setName(film.getCategory() != null ? film.getCategory().getName() : null);
        res.setCategory(category);
        return res;
    }

    public void handleDeleteAFilm(Film reqFilm) {
        this.filmRepository.deleteById(reqFilm.getId());
    }

}
