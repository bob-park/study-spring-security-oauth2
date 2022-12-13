package com.bob.oauth2client.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @GetMapping(path = "loginPage")
    public String loginPage() {
        return "loginPage";
    }

}
