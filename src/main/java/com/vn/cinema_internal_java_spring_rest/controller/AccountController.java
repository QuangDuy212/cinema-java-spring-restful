package com.vn.cinema_internal_java_spring_rest.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.dto.user.PasswordChangeDTO;
import com.vn.cinema_internal_java_spring_rest.service.UserService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;

    public AccountController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/account/change-password")
    @ApiMessage(value = "Change password success")
    public ResponseEntity<Void> changePassword(@RequestBody PasswordChangeDTO passwordChangeDto)
            throws CommonException {
        if (passwordChangeDto.getCurrentPassword().isEmpty() || passwordChangeDto.getNewPassword().isEmpty()) {
            throw new CommonException("Lỗi password!");
        }
        this.userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
        return ResponseEntity.ok().body(null);
    }
}
