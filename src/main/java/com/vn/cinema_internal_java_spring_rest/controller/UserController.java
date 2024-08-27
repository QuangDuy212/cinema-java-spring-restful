package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.ResultPaginationDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResFetchUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResUpdateUserDTO;
import com.vn.cinema_internal_java_spring_rest.service.UserService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    @ApiMessage(value = "Create a user success")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody User reqUser) throws CommonException {
        boolean checkExist = this.userService.isExistByEmail(reqUser.getEmail());
        if (checkExist) {
            throw new CommonException("User's email exist");
        }
        String newPassword = this.passwordEncoder.encode(reqUser.getPassword());
        reqUser.setPassword(newPassword);
        User user = this.userService.handleCreateUser(reqUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(this.userService.convertUserToResUserCreateUserDTO(user));
    }

    @GetMapping("/users/{id}")
    @ApiMessage(value = "Fetch user success")
    public ResponseEntity<ResFetchUserDTO> fetchUserById(@PathVariable("id") long id) throws CommonException {
        User user = this.userService.fetchUserById(id);
        if (user == null) {
            throw new CommonException("User not found");
        }
        return ResponseEntity.ok().body(this.userService.convertUserToResFetchUserDTO(user));
    }

    @GetMapping("/users")
    @ApiMessage(value = "Fetch all users success")
    public ResponseEntity<ResultPaginationDTO> fetchAllUsers(Pageable page) {
        ResultPaginationDTO res = this.userService.fetchAllUsers(page);
        return ResponseEntity.ok().body(res);
    }

    @PutMapping("/users")
    @ApiMessage(value = "Update a user success")
    public ResponseEntity<ResUpdateUserDTO> updateAUser(@RequestBody User reqUser) throws CommonException {
        User user = this.userService.fetchUserById(reqUser.getId());
        if (user == null) {
            throw new CommonException("User not found");
        }
        if (reqUser.getEmail() != null) {
            boolean checkExistEmail = this.userService.isExistByEmail(reqUser.getEmail());
            if (checkExistEmail)
                throw new CommonException("Email existed");
        }
        User currentUser = this.userService.handleUpdateUser(reqUser);
        ResUpdateUserDTO res = this.userService.convertUserToResUpdateUserDTO(currentUser);
        return ResponseEntity.ok().body(res);
    }

}
