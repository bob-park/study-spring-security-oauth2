package com.example.resourceserveropaque.controller;

import java.security.Principal;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resourceserveropaque.model.OpaqueDto;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("/")
    public Authentication index(Authentication authentication,
        @AuthenticationPrincipal OAuth2AuthenticatedPrincipal principal) {

        BearerTokenAuthentication tokenAuthentication = (BearerTokenAuthentication) authentication;

        Map<String, Object> tokenAttributes = tokenAuthentication.getTokenAttributes();

        OpaqueDto opaqueDto = new OpaqueDto((boolean) tokenAttributes.get("active"), authentication, principal);

        log.debug("opaqueDto={}", opaqueDto);

        return authentication;
    }

}
