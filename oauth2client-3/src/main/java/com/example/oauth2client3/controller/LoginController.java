package com.example.oauth2client3.controller;

import java.util.Set;

import lombok.RequiredArgsConstructor;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequiredArgsConstructor
@Controller
@RequestMapping
public class LoginController {


    @GetMapping(path = "v2/oauth2Login")
    public String oauth2Login(@RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient authorizedClient,
        Model model) {

        // 아직 인증받지 못한 authentication 이므로, anonymous 가 됨

        // * Resource Owner Password 방식
        if (authorizedClient != null) {
            // 사용자 정보를 가져와 인증처리
            OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();

            ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();

            OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);

            OAuth2User oAuth2User = userService.loadUser(userRequest);

            // 인가 서버로부터 받는 Authority 를 custom 하여 맵핑할 수 있음
            SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
            authorityMapper.setPrefix("SYSTEM_"); // 기본은 SCOPE_

            Set<GrantedAuthority> authorities = authorityMapper.mapAuthorities(oAuth2User.getAuthorities());

            OAuth2AuthenticationToken authenticationToken =
                new OAuth2AuthenticationToken(oAuth2User,
                    authorities,
                    clientRegistration.getRegistrationId());

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            model.addAttribute("authenticationToken", authenticationToken);

        }

        // ! Client Credentials 방식은 client 가 인가 처리만 받으면 완료
        // 별도의 인증처리를 받지 않았기 때문에, logout 에 접근할 수 없음
        model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("refreshToken", authorizedClient.getRefreshToken().getTokenValue());

        return "home";
    }


}
