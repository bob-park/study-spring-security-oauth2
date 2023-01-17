package com.example.oauth2clientsociallogin.controller;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HomeController {


    @GetMapping(path = "/api/user")
    public Authentication user(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) {

        log.debug("authentication={}, oAuth2User={}", authentication, oAuth2User);

        return authentication;
    }

    @GetMapping(path = "/api/oidc")
    public Authentication oidc(Authentication authentication, @AuthenticationPrincipal OidcUser oidcUser) {

        log.debug("authentication={}, oidcUser={}", authentication, oidcUser);

        return authentication;
    }


}
