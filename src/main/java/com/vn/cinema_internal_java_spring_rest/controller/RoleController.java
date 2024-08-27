package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.service.RoleService;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> createARole(@RequestBody Role reqRole) throws CommonException {
        boolean checkExistByEmail = this.roleService.isExistsByName(reqRole.getName());
        if (checkExistByEmail)
            throw new CommonException("Role existed");
        Role role = this.roleService.handleCreateRole(reqRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> fetchRoleById(@PathVariable("id") long id) throws CommonException {
        Role role = this.roleService.fetchRoleById(id);
        if (role == null) {
            throw new CommonException("Role not found");
        }
        return ResponseEntity.ok().body(role);
    }

    @GetMapping("/roles")
    public ResponseEntity<ResultPaginationDTO> fetchAllRoles(Pageable page) throws CommonException {
        ResultPaginationDTO res = this.roleService.fetchAllRoles(page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/roles")
    public ResponseEntity<Role> updateARole(@RequestBody Role reqRole) throws CommonException {
        Role role = this.roleService.fetchRoleById(reqRole.getId());
        if (role == null)
            throw new CommonException("Role not found");
        Role currentRole = this.roleService.hanldeUpdateRole(reqRole);
        return ResponseEntity.ok().body(currentRole);
    }

}
