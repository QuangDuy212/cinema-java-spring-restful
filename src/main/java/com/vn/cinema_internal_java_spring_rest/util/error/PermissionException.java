package com.vn.cinema_internal_java_spring_rest.util.error;

public class PermissionException extends Exception {
    // Constructor that accepts a message
    public PermissionException(String message) {
        super(message);
    }
}
