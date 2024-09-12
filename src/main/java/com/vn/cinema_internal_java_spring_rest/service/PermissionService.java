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
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

@Service
public class PermissionService {
    private final Logger log = LoggerFactory.getLogger(PermissionService.class);
    private final PermissionRepository permissionRepository;
    private final RoleService roleService;

    public PermissionService(PermissionRepository permissionRepository, RoleService roleService) {
        this.permissionRepository = permissionRepository;
        this.roleService = roleService;
    }

    public boolean isExists(String apiPath, String method) {
        return this.permissionRepository.existsByApiPathAndMethod(apiPath, method);
    }

    public List<Permission> fetchListPerByListIds(List<Long> listIds) {
        return this.permissionRepository.findByIdIn(listIds);
    }

    public Permission handleCreatePermission(Permission reqPer) {
        log.debug("Request to create Permission : {}", reqPer);
        if (reqPer.getRoles() != null) {
            List<Long> listIds = reqPer.getRoles()
                    .stream().map(i -> i.getId())
                    .collect(Collectors.toList());
            List<Role> roles = this.roleService.fetchListRoleByListId(listIds);
            reqPer.setRoles(roles);
        }
        return this.permissionRepository.save(reqPer);
    }

    public Permission fetchPermissionById(long id) {
        log.debug("Request to get Permission by id : {}", id);
        Optional<Permission> per = this.permissionRepository.findById(id);
        if (per.isPresent())
            return per.get();
        return null;
    }

    public ResultPaginationDTO fetchAllPermissions(Specification<Permission> spe, Pageable page) {
        Page<Permission> pers = this.permissionRepository.findAll(spe, page);

        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(pers.getTotalPages());
        meta.setTotal(pers.getTotalElements());

        res.setMeta(meta);
        res.setResult(pers.getContent());
        return res;
    }

    public Permission handleUpdateAPermission(Permission reqPer) throws CommonException {
        log.debug("Request to update Permission  : {}", reqPer);
        Permission currentPer = this.fetchPermissionById(reqPer.getId());
        if ((reqPer.getApiPath() != null && !reqPer.getApiPath().equals(currentPer.getApiPath())
                && reqPer.getMethod() != null
                && !reqPer.getMethod().equals(currentPer.getMethod()))
                || (reqPer.getApiPath() != null && !reqPer.getApiPath().equals(currentPer.getApiPath())
                        && reqPer.getMethod() != null)
                || (reqPer.getApiPath() != null
                        && reqPer.getMethod() != null
                        && !reqPer.getMethod().equals(currentPer.getMethod()))) {
            boolean checkExist = this.isExists(reqPer.getApiPath(), reqPer.getMethod());
            if (checkExist) {
                throw new CommonException("Permission existd");
            }
            currentPer.setApiPath(reqPer.getApiPath());
            currentPer.setMethod(reqPer.getMethod());
        }
        if (reqPer.getApiPath() != null && !reqPer.getApiPath().equals(currentPer.getApiPath())
                && reqPer.getMethod() == null) {
            boolean checkExist = this.isExists(reqPer.getApiPath(), currentPer.getMethod());
            if (checkExist) {
                throw new CommonException("Permission existd");
            }
            currentPer.setApiPath(reqPer.getApiPath());
        }

        if (reqPer.getApiPath() == null && reqPer.getMethod() != null
                && !reqPer.getMethod().equals(currentPer.getMethod())) {
            boolean checkExist = this.isExists(currentPer.getApiPath(), reqPer.getMethod());
            if (checkExist) {
                throw new CommonException("Permission existd");
            }
            currentPer.setMethod(reqPer.getMethod());
        }

        if (reqPer.getName() != null)
            currentPer.setName(reqPer.getName());
        if (reqPer.getModule() != null)
            currentPer.setModule(reqPer.getModule());
        return this.permissionRepository.save(currentPer);
    }

    public void handleDeletePer(long id) {
        this.permissionRepository.deleteById(id);
    }
}
