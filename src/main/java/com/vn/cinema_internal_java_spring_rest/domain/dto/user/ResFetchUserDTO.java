package com.vn.cinema_internal_java_spring_rest.domain.dto.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResFetchUserDTO {
    private long id;
    private String email;
    private String fullName;
    private String phone;
    private String address;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String udpatedBy;
}
