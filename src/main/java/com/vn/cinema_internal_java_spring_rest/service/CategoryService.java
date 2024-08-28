package com.vn.cinema_internal_java_spring_rest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Category;
import com.vn.cinema_internal_java_spring_rest.repository.CategoryRepository;

@Service
public class CategoryService {
    private final Logger log = LoggerFactory.getLogger(CategoryService.class);

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public boolean isExistsByName(String name) {
        log.debug("Request to check exists Category by name : {}", name);
        return this.categoryRepository.existsByName(name);
    }

    public Category handleCreateACategory(Category reqCa) {
        log.debug("Request to save Category : {}", reqCa);
        return this.categoryRepository.save(reqCa);
    }
}
