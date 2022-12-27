package com.bob.oauth2client.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginPageController {

    @GetMapping(path = "login")
    public String loginPage(){
        return "login";
    }


}
