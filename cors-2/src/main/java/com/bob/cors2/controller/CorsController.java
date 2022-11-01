package com.bob.cors2.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bob.cors2.model.User;

@RestController
public class CorsController {

    @GetMapping(path = "users")
    public User users(){
        return new User("user", 20);
    }

}
