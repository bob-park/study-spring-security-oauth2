package com.example.oauth2clientsociallogin.controller;

import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.oauth2clientsociallogin.security.service.AbstractOAuth2UserService;

import static com.example.oauth2clientsociallogin.security.service.AbstractOAuth2UserService.NAVER;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model, Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth2User) {

        OAuth2AuthenticationToken oAuth2AuthenticationToken = (OAuth2AuthenticationToken) authentication;

        if (oAuth2AuthenticationToken != null) {
            Map<String, Object> attributes = oAuth2User.getAttributes();

            String name = (String) attributes.get("name");

            if (oAuth2AuthenticationToken.getAuthorizedClientRegistrationId().equals(NAVER)) {
                Map<String, Object> response = (Map<String, Object>) attributes.get("response");

                name = (String) response.get("name");
            }

            model.addAttribute("user", name);
        }

        return "index";
    }

}
