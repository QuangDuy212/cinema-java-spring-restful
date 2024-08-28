package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
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
}
