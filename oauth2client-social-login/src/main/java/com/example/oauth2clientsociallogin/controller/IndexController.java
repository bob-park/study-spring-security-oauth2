package com.example.oauth2clientsociallogin.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.oauth2clientsociallogin.security.model.PrincipalUser;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal PrincipalUser principalUser) {

        String view = "index";

        if (principalUser != null) {

            String userName = principalUser.providerUser().getUsername();

            model.addAttribute("user", userName);
            model.addAttribute("provider", principalUser.providerUser().getProvider());
        }

        return "index";
    }

}
