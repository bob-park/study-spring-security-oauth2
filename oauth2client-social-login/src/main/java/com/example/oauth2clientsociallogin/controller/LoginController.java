package com.example.oauth2clientsociallogin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @GetMapping("logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(request, response, authentication);

        return "redirect:/login";
    }

}
