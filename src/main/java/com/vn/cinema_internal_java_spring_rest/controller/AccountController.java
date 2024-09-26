package com.vn.cinema_internal_java_spring_rest.controller;

import java.util.UUID;
import java.time.Instant;

import java.time.temporal.ChronoUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vn.cinema_internal_java_spring_rest.domain.User;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.PasswordChangeDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.PasswordChangeWithCodeDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.ResFetchUserDTO;
import com.vn.cinema_internal_java_spring_rest.domain.dto.user.RetryPasswordDTO;
import com.vn.cinema_internal_java_spring_rest.service.EmailService;
import com.vn.cinema_internal_java_spring_rest.service.UserService;
import com.vn.cinema_internal_java_spring_rest.util.annotation.ApiMessage;
import com.vn.cinema_internal_java_spring_rest.util.constant.CodeSendEmail;
import com.vn.cinema_internal_java_spring_rest.util.error.CommonException;

import jakarta.validation.Valid;

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
    public ResponseEntity<ResFetchUserDTO> retryPassword(@RequestBody RetryPasswordDTO retry)
            throws CommonException {
        // Check email
        User user = this.userService.fetchUserByEmail(retry.getEmail());
        if (user == null) {
            throw new CommonException("Email không tồn tại");
        }

        // Send mail
        String codeId = UUID.randomUUID().toString();
        user.setCodeId(codeId);
        user.setCodeExpired(Instant.now().plus(5, ChronoUnit.MINUTES));
        this.userService.handleSaveUser(user);
        this.emailService.sendEmailFromTemplateSync("duy2k4ml1234@gmail.com", "Test send email", "send-code",
                retry.getEmail(),
                codeId);
        ResFetchUserDTO res = this.userService.convertUserToResFetchUserDTO(user);
        return ResponseEntity.ok().body(res);
    }

    @PostMapping("/account/change-password-with-code")
    @ApiMessage(value = "Change password forgot success")
    public ResponseEntity<Void> changePasswordWithCode(@Valid @RequestBody PasswordChangeWithCodeDTO data)
            throws CommonException {
        // Check email
        User user = this.userService.fetchUserByEmail(data.getEmail());
        if (user == null) {
            throw new CommonException("Email không tồn tại");
        }

        // Check code
        if (!data.getCode().equals(user.getCodeId()))
            throw new CommonException("Mã xác thực nhập không chính xác.");
        if (user.getCodeExpired().compareTo(Instant.now()) <= 0)
            throw new CommonException("Mã xác thực đã hết hạn.");
        if (!data.getNewPassword().equals(data.getConfirmPassword()))
            throw new CommonException("Mật khẩu / xác nhận mật khẩu nhập không chính xác.");

        this.userService.changePasswordForgot(data.getEmail(), data.getNewPassword());
        user.setCodeId(null);
        user.setCodeExpired(null);
        this.userService.handleSaveUser(user);
        return ResponseEntity.ok().body(null);
    }
}
