package com.vn.cinema_internal_java_spring_rest.domain.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqLoginDTO {
    @NotBlank(message = "Username must not empty")
    private String username;
    @NotBlank(message = "Password must not empty")
    private String password;
}
