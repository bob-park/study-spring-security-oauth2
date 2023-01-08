package com.example.oauth2client2.controller;

import java.util.Arrays;
import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping("client")
public class ClientController {

    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final OAuth2AuthorizedClientService authorizedClientService;

    @GetMapping
    public String client(HttpServletRequest request, Model model) {

        String clientRegistrationId = "keycloak";

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthorizedClient authorizedClient1 =
            authorizedClientRepository.loadAuthorizedClient(clientRegistrationId, authentication, request);

        OAuth2AuthorizedClient authorizedClient2 =
            authorizedClientService.loadAuthorizedClient(clientRegistrationId, authentication.getName());

        // * 인가 받은 AuthorizedClient 까지 생성됨
        OAuth2AccessToken accessToken = authorizedClient1.getAccessToken();
        OAuth2RefreshToken refreshToken = authorizedClient1.getRefreshToken();

        // *  사용자 정보를 가져오 인증 처리 작업
        OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();

        OAuth2User oAuth2User =
            userService.loadUser(new OAuth2UserRequest(authorizedClient1.getClientRegistration(), accessToken));

        // * 최종 인증 객체
        OAuth2AuthenticationToken authenticationToken =
            new OAuth2AuthenticationToken(oAuth2User, Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")),
                authorizedClient1.getClientRegistration().getRegistrationId());

        // * Spring Security 에 등록
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        model.addAttribute("accessToken", accessToken.getTokenValue());
        model.addAttribute("refreshToken", refreshToken.getTokenValue());
        model.addAttribute("principalName", oAuth2User.getName());
        model.addAttribute("clientName", authorizedClient1.getClientRegistration().getRegistrationId());

        return "client";
    }

}
