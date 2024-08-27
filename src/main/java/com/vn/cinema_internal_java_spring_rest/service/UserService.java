package com.vn.cinema_internal_java_spring_rest.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResFetchUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResUpdateUserDTO;
import com.vn.cinema_internal_java_spring_rest.repository.UserRepository;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isExistByEmail(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public User handleCreateUser(User reqUser) {
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
        return res;
    }

    public User fetchUserById(long id) {
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
        return res;
    }

    public ResultPaginationDTO fetchAllUsers(Pageable page) {
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
        User user = this.fetchUserById(reqUser.getId());
        if (reqUser.getEmail() != null) {
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
        return res;
    }

    public User handleGetUserByUsername(String email) {
        Optional<User> user = this.userRepository.findByEmail(email);
        if (user.isPresent())
            return user.get();
        return null;
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.handleGetUserByUsername(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }
}
