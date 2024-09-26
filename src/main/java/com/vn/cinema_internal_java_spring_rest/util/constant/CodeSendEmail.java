package com.vn.cinema_internal_java_spring_rest.util.constant;

import java.time.Instant;

import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CodeSendEmail {
    private String code;
    private Long timeToLive;
    private Instant created;

    public void active() {
        this.created = Instant.now();
    }

    public boolean isActive() {
        Long current = Instant.now().toEpochMilli();
        boolean active = (current - created.toEpochMilli()) == timeToLive;
        return active;
    }
}
