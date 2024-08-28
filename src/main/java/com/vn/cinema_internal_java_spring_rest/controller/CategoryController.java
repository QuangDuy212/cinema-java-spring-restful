package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.CategoryService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class CategoryController {

    private final Logger log = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping("/categories")
    @ApiMessage(value = "Create a category success")
    public ResponseEntity<Category> createUser(@Valid @RequestBody Category reqCa) throws CommonException {
        log.debug("REST request to create Category : {}", reqCa);
        boolean checkExist = this.categoryService.isExistsByName(reqCa.getName());
        if (checkExist)
            throw new CommonException("Category existed");
        Category category = this.categoryService.handleCreateACategory(reqCa);
        return ResponseEntity.status(HttpStatus.CREATED).body(category);
    }

    @GetMapping("/categories/{id}")
    @ApiMessage(value = "Fetch category success")
    public ResponseEntity<Category> fetchCategoryById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get a Category by id : {}", id);
        Category category = this.categoryService.fetchCategoryById(id);
        if (category == null) {
            throw new CommonException("Category not found");
        }

        return ResponseEntity.ok().body(category);
    }

    @GetMapping("/categories")
    @ApiMessage(value = "Fetch all Categories success")
    public ResponseEntity<ResultPaginationDTO> fetchallCategories(Pageable page) {
        log.debug("REST request to get all Categories ");
        ResultPaginationDTO res = this.categoryService.fetchAllCategories(page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/categories")
    @ApiMessage(value = "Update a Category success")
    public ResponseEntity<Category> updateAUser(@RequestBody Category reqCa) throws CommonException {
        log.debug("REST request to update Category : {}", reqCa);
        Category category = this.categoryService.fetchCategoryById(reqCa.getId());
        if (category == null) {
            throw new CommonException("Category not found");
        }
        if (reqCa.getName() != null && !reqCa.getName().equals(category.getName())) {
            boolean checkExist = this.categoryService.isExistsByName(reqCa.getName());
            if (checkExist)
                throw new CommonException("Name existed");
        }
        Category newCate = this.categoryService.hanldeUpdateACategory(reqCa);
        return ResponseEntity.ok().body(newCate);
    }
}
