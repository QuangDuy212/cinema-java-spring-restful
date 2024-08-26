package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.RestResponse;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResCreateUserDTO;
import com.vn.cinema_internal_java_spring_rest.service.UserService;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class UserController {
    private final UserService userService;
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/users")
    public ResponseEntity<ResCreateUserDTO> createUser(@RequestBody User reqUser) throws CommonException {
        boolean checkExist = this.userService.isExistByEmail(reqUser.getEmail());
        if (checkExist) {
            throw new CommonException("User's email exist");
        }
        reqUser.setPassword(passwordEncoder.encode(reqUser.getPassword()));
        User user = this.userService.createUser(reqUser);

        return ResponseEntity.ok().body(this.userService.convertUserToResUserCreateUserDTO(user));
    }

}
