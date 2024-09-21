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
import com.vn.cinema_internal_java_spring_rest.domain.History;
import com.vn.cinema_internal_java_spring_rest.domain.Seat;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.film.ResFilmDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.history.ResHistoryDTO;
import com.vn.cinema_internal_java_spring_rest.repository.HistoryRepository;
import com.vn.cinema_internal_java_spring_rest.repository.UserRepository;
import com.vn.cinema_internal_java_spring_rest.util.SecurityUtil;

@Service
public class HistoryService {
    private final Logger log = LoggerFactory.getLogger(HistoryService.class);
    private final HistoryRepository historyRepository;
    private final UserRepository userRepository;

    public HistoryService(HistoryRepository historyRepository, UserRepository userRepository) {
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    public History handleCreateHistory(History reqHis) {
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent())
            reqHis.setUser(user.get());
        return this.historyRepository.save(reqHis);
    }

    public ResHistoryDTO convertHisToResHistoryDTO(History reqHis) {
        ResHistoryDTO res = new ResHistoryDTO();
        res.setId(reqHis.getId());
        res.setNameFilm(reqHis.getNameFilm());
        res.setDate(reqHis.getDate());
        res.setShow(reqHis.getTime());
        if (reqHis.getUser() != null)
            res.setEmail(reqHis.getUser().getEmail());
        res.setCreatedAt(reqHis.getCreatedAt());
        res.setCreatedBy(reqHis.getCreatedBy());
        res.setUpdatedAt(reqHis.getUpdatedAt());
        res.setUpdatedBy(reqHis.getUpdatedBy());
        res.setTotal(reqHis.getTotal());
        res.setQuantity(reqHis.getQuantity());
        res.setSeats(reqHis.getSeats());
        res.setZoomNumber(reqHis.getZoomNumber());
        return res;
    }

    public History fetchHistoryById(Long id) {
        log.debug("Request to fetch History by id : {}", id);
        Optional<History> his = this.historyRepository.findById(id);
        if (his.isPresent())
            return his.get();
        return null;
    }

    public ResultPaginationDTO fetchAllHistories(Specification<History> spe,
            Pageable page) {
        log.debug("Request to get all Histories");
        Page<History> lsitHistories = this.historyRepository.findAll(spe, page);
        List<ResHistoryDTO> listResHis = lsitHistories.getContent()
                .stream().map(i -> this.convertHisToResHistoryDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(lsitHistories.getTotalPages());
        meta.setTotal(lsitHistories.getTotalElements());

        res.setMeta(meta);
        res.setResult(listResHis);
        return res;
    }

    public ResultPaginationDTO fetchHistoryByUser(Specification<History> spe,
            Pageable page) {
        log.debug("Request to get all Histories");
        String email = SecurityUtil.getCurrentUserLogin().isPresent()
                ? SecurityUtil.getCurrentUserLogin().get()
                : "";
        Optional<User> u = this.userRepository.findByEmail(email);
        User user = u.isPresent() ? u.get() : new User();
        Page<History> listHistories = this.historyRepository.findByUser(user, page);
        List<ResHistoryDTO> listResHis = listHistories.getContent()
                .stream().map(i -> this.convertHisToResHistoryDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listHistories.getTotalPages());
        meta.setTotal(listHistories.getTotalElements());

        res.setMeta(meta);
        res.setResult(listResHis);
        return res;
    }

    public void handleDeleteHistory(long id) {
        this.historyRepository.deleteById(id);
    }
}
