package com.vn.cinema_internal_java_spring_rest.domain.dto.user;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResCreateUserDTO {
    private long id;
    private String email;
    private String fullName;
    private String address;
    private String phone;
    private Instant createdAt;
    private Instant updatedAt;
    private RoleUser role;
    private boolean active;

    @Setter
    @Getter
    public static class RoleUser {
        private long id;
        private String name;
    }
}
