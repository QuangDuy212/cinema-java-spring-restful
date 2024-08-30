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

import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.Film;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.repository.CategoryRepository;
import com.vn.cinema_internal_java_spring_rest.repository.FilmRepository;

@Service
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;
    private final FilmRepository filmRepository;

    public CategoryService(CategoryRepository categoryRepository, FilmRepository filmRepository) {
        this.categoryRepository = categoryRepository;
        this.filmRepository = filmRepository;
    }

    public boolean isExistsByName(String name) {
        log.debug("Request to check exists Category by name : {}", name);
        return this.categoryRepository.existsByName(name);
    }

    public Category handleCreateACategory(Category reqCa) {
        log.debug("Request to save Category : {}", reqCa);
        return this.categoryRepository.save(reqCa);
    }

    public Category fetchCategoryById(long id) {
        log.debug("Request to get Category by id : {}", id);
        Optional<Category> category = this.categoryRepository.findById(id);
        if (category.isPresent())
            return category.get();
        return null;
    }

    public ResultPaginationDTO fetchAllCategories(Specification<Category> spe, Pageable page) {
        log.debug("Request to get all Categories");
        Page<Category> listcates = this.categoryRepository.findAll(spe, page);
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listcates.getTotalPages());
        meta.setTotal(listcates.getTotalElements());

        res.setMeta(meta);
        res.setResult(listcates.getContent());
        return res;
    }

    public Category hanldeUpdateACategory(Category reqCa) {
        Category currentCat = this.fetchCategoryById(reqCa.getId());
        if (reqCa.getName() != null)
            currentCat.setName(reqCa.getName());
        if (reqCa.getFilms() != null) {
            List<Long> listIds = reqCa.getFilms()
                    .stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Film> listFilms = this.filmRepository.findByIdIn(listIds);
            reqCa.setFilms(listFilms);
        }
        return this.categoryRepository.save(currentCat);
    }
}
