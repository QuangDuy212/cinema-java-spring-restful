package com.vn.cinema_internal_java_spring_rest.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class HelloController {
    @GetMapping("/")
    public String helloWorld() {
        return "Hello world";
    }

}