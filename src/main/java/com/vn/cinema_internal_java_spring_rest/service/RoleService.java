package com.vn.cinema_internal_java_spring_rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
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

    public Role fetchRoleById(long id) {
        Optional<Role> roleOptional = this.roleRepository.findById(id);
        if (roleOptional.isPresent())
            return roleOptional.get();
        return null;
    }

    public ResultPaginationDTO fetchAllRoles(Pageable page) {
        Page<Role> roles = this.roleRepository.findAll(page);

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
        return this.roleRepository.save(currentRole);
    }

    public List<Role> fetchListRoleByListId(List<Long> listIds) {
        return this.roleRepository.findByIdIn(listIds);
    }
}
