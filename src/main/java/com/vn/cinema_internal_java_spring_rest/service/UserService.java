package com.vn.cinema_internal_java_spring_rest.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResFetchUserDTO;
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

    public User createUser(User reqUser) {
        return this.userRepository.save(reqUser);
    }

    public ResCreateUserDTO convertUserToResUserCreateUserDTO(User user) {
        ResCreateUserDTO res = new ResCreateUserDTO();
        res.setId(user.getId());
        res.setEmail(user.getEmail());
        res.setFullName(user.getFullName());
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
        res.setCreatedAt(user.getCreatedAt());
        res.setUpdatedAt(user.getUpdatedAt());
        res.setCreatedBy(user.getCreatedBy());
        res.setCreatedBy(user.getCreatedBy());
        return res;
    }
}
