package com.vn.cinema_internal_java_spring_rest.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.PasswordChangeDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.RetryPasswordDTO;
import com.vn.cinema_internal_java_spring_rest.service.EmailService;
import com.vn.cinema_internal_java_spring_rest.service.UserService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.constant.CodeSendEmail;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

@RestController
@RequestMapping("/api/v1")
public class AccountController {
    private final Logger log = LoggerFactory.getLogger(AccountController.class);
    private final UserService userService;
    private final EmailService emailService;

    public AccountController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
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

    @PostMapping("/account/retry-password")
    @ApiMessage(value = "Retry password success")
    public ResponseEntity<Void> retryPassword(@RequestBody RetryPasswordDTO retry)
            throws CommonException {
        // Check email
        User user = this.userService.fetchUserByEmail(retry.getEmail());
        if (user == null) {
            throw new CommonException("Email không tồn tại");
        }

        // Send mail
        CodeSendEmail codeSendEmail = new CodeSendEmail();
        codeSendEmail.setCode(UUID.randomUUID().toString());
        codeSendEmail.active();
        this.emailService.sendEmailFromTemplateSync("duy2k4ml1234@gmail.com", "Test send email", "send-code",
                retry.getEmail(),
                codeSendEmail.getCode());
        return ResponseEntity.ok().body(null);
    }
}
