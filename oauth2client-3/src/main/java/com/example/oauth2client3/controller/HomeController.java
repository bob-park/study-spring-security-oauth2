package com.example.oauth2client3.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("home")
public class HomeController {


    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping
    public String home(Model model, OAuth2AuthenticationToken authentication) {

        OAuth2AuthorizedClient authorizedClient =
            authorizedClientService.loadAuthorizedClient("keycloak", authentication.getName());

        model.addAttribute("authenticationToken", authentication);
        model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("refreshToken", authorizedClient.getRefreshToken().getTokenValue());

        return "home";
    }

}
