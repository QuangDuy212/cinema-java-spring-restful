package com.vn.cinema_internal_java_spring_rest.domain.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordChangeDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    private String currentPassword;
    private String newPassword;
}
