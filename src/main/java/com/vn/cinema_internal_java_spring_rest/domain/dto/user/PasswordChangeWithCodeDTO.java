package com.vn.cinema_internal_java_spring_rest.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeWithCodeDTO implements Serializable {
    @NotEmpty(message = "Email must not empty")
    private String email;

    @NotEmpty(message = "Code must not empty")
    private String code;

    @NotEmpty(message = "NewPassword must not empty")
    private String newPassword;

    @NotEmpty(message = "ConfirmPassword must not empty")
    private String confirmPassword;
}
