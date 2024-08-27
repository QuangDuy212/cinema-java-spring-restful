package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.service.RoleService;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    public ResponseEntity<Role> postMethodName(@RequestBody Role reqRole) throws CommonException {
        boolean checkExistByEmail = this.roleService.isExistsByName(reqRole.getName());
        if (checkExistByEmail)
            throw new CommonException("Role existed");
        Role role = this.roleService.handleCreateRole(reqRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

}
