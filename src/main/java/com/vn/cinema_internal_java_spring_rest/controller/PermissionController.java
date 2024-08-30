package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.Permission;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.PermissionService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class PermissionController {
    private final Logger log = LoggerFactory.getLogger(PermissionController.class);
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage(value = "Create a permission success")
    public ResponseEntity<Permission> createAPermission(@Valid @RequestBody Permission reqPer) throws CommonException {
        log.debug("REST request to create Permission : {}", reqPer);
        boolean checkExists = this.permissionService.isExists(reqPer.getApiPath(), reqPer.getMethod());
        if (checkExists)
            throw new CommonException("Permission existed");
        Permission per = this.permissionService.handleCreatePermission(reqPer);
        return ResponseEntity.status(HttpStatus.CREATED).body(per);
    }

    @PutMapping("/permissions")
    @ApiMessage(value = "Update a permission success")
    public ResponseEntity<Permission> updateAPermission(@RequestBody Permission reqPer) throws CommonException {
        log.debug("REST request to update Permission : {}", reqPer);
        Permission per = this.permissionService.fetchPermissionById(reqPer.getId());
        if (per == null)
            throw new CommonException("Permission not found");
        Permission updatePer = this.permissionService.handleUpdateAPermission(reqPer);
        return ResponseEntity.ok().body(updatePer);
    }

    @GetMapping("/permissions/{id}")
    @ApiMessage(value = "Fetch permission by id success")
    public ResponseEntity<Permission> fetchPermissionById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to get Permission by id: {}", id);
        Permission per = this.permissionService.fetchPermissionById(id);
        if (per == null)
            throw new CommonException("Permission not found");
        return ResponseEntity.ok().body(per);
    }

    @GetMapping("/permissions")
    @ApiMessage(value = "Fetch all permissions success")
    public ResponseEntity<ResultPaginationDTO> fetchAllPermissions(@Filter Specification<Permission> spe,
            Pageable page) {
        ResultPaginationDTO res = this.permissionService.fetchAllPermissions(spe, page);
        return ResponseEntity.ok().body(res);
    }

}
