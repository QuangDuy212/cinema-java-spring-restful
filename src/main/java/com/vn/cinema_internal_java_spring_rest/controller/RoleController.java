package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.vn.cinema_internal_java_spring_rest.domain.Permission;
import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.RoleService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final Logger log = LoggerFactory.getLogger(RoleController.class);
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role success")
    public ResponseEntity<Role> createARole(@RequestBody Role reqRole) throws CommonException {
        log.debug("REST request to create Role : {}", reqRole);
        boolean checkExistByEmail = this.roleService.isExistsByName(reqRole.getName());
        if (checkExistByEmail)
            throw new CommonException("Role existed");
        Role role = this.roleService.handleCreateRole(reqRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch a role success")
    public ResponseEntity<Role> fetchRoleById(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to fetch a role by id : {}", id);
        Role role = this.roleService.fetchRoleById(id);
        if (role == null) {
            throw new CommonException("Role not found");
        }
        return ResponseEntity.ok().body(role);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch all roles success")
    public ResponseEntity<ResultPaginationDTO> fetchAllRoles(@Filter Specification<Role> spe, Pageable page)
            throws CommonException {
        log.debug("REST request to fetch all Roles ");
        ResultPaginationDTO res = this.roleService.fetchAllRoles(spe, page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role success")
    public ResponseEntity<Role> updateARole(@RequestBody Role reqRole) throws CommonException {
        log.debug("REST request to update Role : {}", reqRole);
        Role role = this.roleService.fetchRoleById(reqRole.getId());
        if (role == null)
            throw new CommonException("Role not found");
        Role currentRole = this.roleService.hanldeUpdateRole(reqRole);
        return ResponseEntity.ok().body(currentRole);
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage(value = "Delete a Role success")
    public ResponseEntity<Void> deleteARole(@PathVariable("id") long id) throws CommonException {
        log.debug("REST request to delete Role by Id: {}", id);
        Role role = this.roleService.fetchRoleById(id);
        if (role == null)
            throw new CommonException("Role not found");
        this.roleService.handleDeleteRole(id);
        return ResponseEntity.ok().body(null);
    }

}
