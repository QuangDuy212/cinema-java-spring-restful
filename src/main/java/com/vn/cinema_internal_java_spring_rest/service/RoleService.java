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

import com.vn.cinema_internal_java_spring_rest.domain.Permission;
import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.repository.PermissionRepository;
import com.vn.cinema_internal_java_spring_rest.repository.RoleRepository;

@Service
public class RoleService {
    private final Logger log = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean isExistsByName(String name) {
        return this.roleRepository.existsByName(name);
    }

    public Role handleCreateRole(Role reqRole) {
        if (reqRole.getPermissions() != null) {
            List<Long> listIds = reqRole.getPermissions()
                    .stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Permission> listPers = this.permissionRepository.findByIdIn(listIds);
            reqRole.setPermissions(listPers);
        }
        return this.roleRepository.save(reqRole);
    }

    public Role fetchRoleById(long id) {
        log.debug("Request to fetch Role by id : {}", id);
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }

    public ResultPaginationDTO fetchAllRoles(Specification<Role> spe, Pageable page) {
        log.debug("Request to fetch all Roles");
        Page<Role> roles = this.roleRepository.findAll(spe, page);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(roles.getTotalPages());
        meta.setTotal(roles.getTotalElements());

        res.setMeta(meta);
        res.setResult(roles.getContent());
        return res;
    }

    public Role hanldeUpdateRole(Role reqRole) {
        log.debug("Request to update Role : {}", reqRole);
        Role currentRole = this.fetchRoleById(reqRole.getId());
        if (reqRole.getName() != null) {
            boolean checkExistName = this.isExistsByName(reqRole.getName());
            boolean checkEqual = currentRole.getName().equals(reqRole.getName());
            if (!checkExistName && !checkEqual) {
                currentRole.setName(reqRole.getName());
            }
        }
        if (reqRole.getDescription() != null) {
            currentRole.setDescription(reqRole.getDescription());
        }
        if (reqRole.isActive() != currentRole.isActive()) {
            currentRole.setActive(reqRole.isActive());
        }
        if (reqRole.getPermissions() != null) {
            List<Long> listIds = reqRole.getPermissions()
                    .stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Permission> listPers = this.permissionRepository.findByIdIn(listIds);
            currentRole.setPermissions(listPers);
        }
        return this.roleRepository.save(currentRole);
    }

    public List<Role> fetchListRoleByListId(List<Long> listIds) {
        return this.roleRepository.findByIdIn(listIds);
    }

    public void handleDeleteRole(long id) {
        this.roleRepository.deleteById(id);
    }
}
