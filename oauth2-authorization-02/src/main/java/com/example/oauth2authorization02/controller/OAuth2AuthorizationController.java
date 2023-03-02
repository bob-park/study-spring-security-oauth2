package com.example.oauth2authorization02.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("authorization")
public class OAuth2AuthorizationController {

    private final OAuth2AuthorizationService authorizationService;

    @GetMapping(path = "list")
    public OAuth2Authorization oAuth2Authorizations(String token) {

        return authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);
    }

}
