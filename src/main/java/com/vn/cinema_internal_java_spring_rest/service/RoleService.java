package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.repository.RoleRepository;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public boolean isExistsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role handleCreateRole(Role reqRole) {
        return this.roleRepository.save(reqRole);
    }
}
