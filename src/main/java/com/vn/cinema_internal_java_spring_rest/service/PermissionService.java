package com.vn.cinema_internal_java_spring_rest.service;

import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.repository.PermissionRepository;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

}
