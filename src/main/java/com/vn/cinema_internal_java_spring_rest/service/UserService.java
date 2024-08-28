package com.vn.cinema_internal_java_spring_rest.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vn.cinema_internal_java_spring_rest.domain.Role;
import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResFetchUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResUpdateUserDTO;
import com.vn.cinema_internal_java_spring_rest.repository.RoleRepository;
import com.vn.cinema_internal_java_spring_rest.repository.UserRepository;

@Service
public class UserService {
    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    public boolean isExistByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleCreateUser(User reqUser) {
        if (reqUser.getRole() != null) {
            Optional<Role> role = this.roleRepository.findById(reqUser.getRole().getId());
            if (role.isPresent())
                reqUser.setRole(role.get());
        }
        return this.userRepository.save(reqUser);
    }

    public ResCreateUserDTO convertUserToResUserCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setAddress(user.getAddress());
        res.setPhone(user.getPhone());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        ResCreateUserDTO.RoleUser role = new ResCreateUserDTO.RoleUser();
        role.setId(user.getRole().getId());
        role.setName(user.getRole().getName());
        res.setRole(role);
        return res;
    }

    public User fetchUserById(long id) {
        log.debug("Request to get User by id : {}", id);
        Optional<User> userOptional = this.userRepository.findById(id);
        if (userOptional.isPresent())
            return userOptional.get();
        return null;
    }

    public ResFetchUserDTO convertUserToResFetchUserDTO(User user) {
        ResFetchUserDTO res = new ResFetchUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setPhone(user.getPhone());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedBy(user.getCreatedBy());
        res.setCreatedBy(user.getCreatedBy());
        ResFetchUserDTO.RoleUser role = new ResFetchUserDTO.RoleUser();
        role.setId(user.getRole().getId());
        role.setName(user.getRole().getName());
        res.setRole(role);
        return res;
    }

    public ResultPaginationDTO fetchAllUsers(Pageable page) {
        log.debug("Request to get all Users");
        Page<User> listUsers = this.userRepository.findAll(page);
        List<ResFetchUserDTO> users = listUsers.stream().map(i -> this.convertUserToResFetchUserDTO(i))
                .collect(Collectors.toList());
        ResultPaginationDTO res = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(page.getPageNumber() + 1);
        meta.setPageSize(page.getPageSize());

        meta.setPages(listUsers.getTotalPages());
        meta.setTotal(listUsers.getSize());

        res.setMeta(meta);
        res.setResult(users);
        return res;
    }

    public User handleUpdateUser(User reqUser) {
        log.debug("Request to update User  : {}", reqUser);
        User user = this.fetchUserById(reqUser.getId());
        if (reqUser.getEmail() != null && !reqUser.getEmail().equals(user.getEmail())) {
            if (!this.isExistByEmail(reqUser.getEmail())) {
                user.setEmail(reqUser.getEmail());
            }
        }
        if (reqUser.getFullName() != null)
            user.setFullName(reqUser.getFullName());
        if (reqUser.getPhone() != null)
            user.setPhone(reqUser.getPhone());
        if (reqUser.getAddress() != null)
            user.setAddress(reqUser.getAddress());
        if (reqUser.getRole() != null) {
            Role role = this.roleRepository.findById(reqUser.getId()).isPresent()
                    ? this.roleRepository.findById(reqUser.getId()).get()
                    : null;
            user.setRole(role);
        }
        return this.userRepository.save(user);
    }

    public ResUpdateUserDTO convertUserToResUpdateUserDTO(User user) {
        ResUpdateUserDTO res = new ResUpdateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
        res.setEmail(user.getEmail());
        res.setAddress(user.getAddress());
        res.setPhone(user.getPhone());
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        ResUpdateUserDTO.RoleUser role = new ResUpdateUserDTO.RoleUser();
        role.setId(user.getRole().getId());
        role.setName(user.getRole().getName());
        res.setRole(role);
        return res;
    }

    public User handleGetUserByUsername(String email) {
        log.debug("Request to get User  by username: {}", email);
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent())
            return user.get();
        return null;
    }

    public void updateUserToken(String token, String email) {
        log.debug("Request to update refreshToken");
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        log.debug("Request to get User  by username: {} and refreshToken: {}", email, refreshToken);
        Optional<User> user = this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
        if (user.isPresent())
            return user.get();
        return null;
    }

    public void handleLogout(User user) {
        log.debug("Request to logout");
        user.setRefreshToken(null);
        this.userRepository.save(user);
    }
}
