package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.film.ResFilmDTO;
import com.vn.cinema_internal_java_spring_rest.service.FilmService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FilmController {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping("/films")
    @ApiMessage(value = "Create a film success")
    public ResponseEntity<ResFilmDTO> createUser(@Valid @RequestBody Film reqFilm) throws CommonException {
        log.debug("REST request to create Film : {}", reqFilm);
        Film film = this.filmService.handlCreateAFilm(reqFilm);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.filmService.convertFilmToResFilmDTO(film));
    }

    @GetMapping("/films/{id}")
    @ApiMessage(value = "Fetch a Film success")
    public ResponseEntity<ResFilmDTO> fetchFilmById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get a Film by id : {}", id);
        Film film = this.filmService.fetchFilmById(id);
        if (film == null) {
            throw new CommonException("Film not found");
        }

        return ResponseEntity.ok().body(this.filmService.convertFilmToResFilmDTO(film));
    }

    @GetMapping("/films")
    @ApiMessage(value = "Fetch all Films success")
    public ResponseEntity<ResultPaginationDTO> fetchallFilms(@Filter Specification<Film> spe, Pageable page) {
        log.debug("REST request to get all Films ");
        ResultPaginationDTO res = this.filmService.fetchAllFilms(spe, page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/films")
    @ApiMessage(value = "Update a Film success")
    public ResponseEntity<ResFilmDTO> updateAFilm(@RequestBody Film reqFilm) throws CommonException {
        log.debug("REST request to update Film : {}", reqFilm);
        Film film = this.filmService.fetchFilmById(reqFilm.getId());
        if (film == null) {
            throw new CommonException("Film not found");
        }
        Film updatedFilm = this.filmService.handleUpdateAFilm(reqFilm);
        return ResponseEntity.ok().body(this.filmService.convertFilmToResFilmDTO(updatedFilm));
    }
}
