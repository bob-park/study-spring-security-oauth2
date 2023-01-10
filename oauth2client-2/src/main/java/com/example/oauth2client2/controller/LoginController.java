package com.example.oauth2client2.controller;

import java.time.Clock;
import java.time.Duration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RequiredArgsConstructor
@Controller
public class LoginController {


    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    private final Duration clockSkew = Duration.ofSeconds(3_600);
    private final Clock clock = Clock.systemUTC();

    @GetMapping(path = "oauth2Login")
    public String oauth2Login(Model model, HttpServletRequest request, HttpServletResponse response) {

        // 아직 인증받지 못한 authentication 이므로, anonymous 가 됨
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        OAuth2AuthorizeRequest authorizeRequest =
            OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal(authentication)
                .attribute(HttpServletRequest.class.getName(), request)
                .attribute(HttpServletResponse.class.getName(), response)
                .build();

        // DefaultAuthorizedClientManager 에서 인가 처리가 된 client 는 AuthorizedClientRepository 에 추가되거나 삭제되는 처리가 들어있다.
        OAuth2AuthorizationSuccessHandler successHandler =
            (authorizedClient, principal, attributes) -> {
                authorizedClientRepository.saveAuthorizedClient(authorizedClient,
                    principal,
                    (HttpServletRequest) attributes.get(HttpServletRequest.class.getName()),
                    (HttpServletResponse) attributes.get(HttpServletResponse.class.getName()));

                log.debug("authorizedClient={}", authorizedClient);
                log.debug("principal={}", principal);
                log.debug("attributes={}", attributes);
            };

        authorizedClientManager.setAuthorizationSuccessHandler(successHandler);

        // null 이 아닌 경우가 바로 인가를 받은 상태
        // client 는 인가를 받은 상태지만, user 는 아직 인증받지 않은 상태이므로 anonymousUser 가 됨
        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        // * Resource Owner Password 방식
//        if (authorizedClient != null) {
//            // 사용자 정보를 가져와 인증처리
//            OAuth2UserService<OAuth2UserRequest, OAuth2User> userService = new DefaultOAuth2UserService();
//
//            ClientRegistration clientRegistration = authorizedClient.getClientRegistration();
//            OAuth2AccessToken accessToken = authorizedClient.getAccessToken();
//
//            OAuth2UserRequest userRequest = new OAuth2UserRequest(clientRegistration, accessToken);
//
//            OAuth2User oAuth2User = userService.loadUser(userRequest);
//
//            // 인가 서버로부터 받는 Authority 를 custom 하여 맵핑할 수 있음
//            SimpleAuthorityMapper authorityMapper = new SimpleAuthorityMapper();
//            authorityMapper.setPrefix("SYSTEM_"); // 기본은 SCOPE_
//
//            Set<GrantedAuthority> authorities = authorityMapper.mapAuthorities(oAuth2User.getAuthorities());
//
//            OAuth2AuthenticationToken authenticationToken =
//                new OAuth2AuthenticationToken(oAuth2User,
//                    authorities,
//                    clientRegistration.getRegistrationId());
//
//            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//
//            model.addAttribute("oAuth2AuthenticationToken", authenticationToken);
//
//        }

        // * Refresh Token Flow
        // 권한 부여 타입을 변경하지 않고 실행
//        if (authorizedClient != null
//            && hasTokenExpired(authorizedClient.getAccessToken()) && authorizedClient.getRefreshToken() != null) {
//            authorizedClientManager.authorize(authorizeRequest);
//        }

        // 권한 부여 타입을 변경하고 실행
        if (authorizedClient != null
            && hasTokenExpired(authorizedClient.getAccessToken()) && authorizedClient.getRefreshToken() != null) {
            ClientRegistration clientRegistration =
                ClientRegistration.withClientRegistration(authorizedClient.getClientRegistration())
                    .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                    .build();

            OAuth2AuthorizedClient authorizedClient2 =
                new OAuth2AuthorizedClient(clientRegistration,
                    authorizedClient.getPrincipalName(),
                    authorizedClient.getAccessToken(),
                    authorizedClient.getRefreshToken());

            OAuth2AuthorizeRequest authorizeRequest2 =
                OAuth2AuthorizeRequest.withAuthorizedClient(authorizedClient2)
                    .principal(authentication)
                    .attribute(HttpServletRequest.class.getName(), request)
                    .attribute(HttpServletResponse.class.getName(), response)
                    .build();

            authorizedClientManager.authorize(authorizeRequest2);
        }

        // ! Client Credentials 방식은 client 가 인가 처리만 받으면 완료
        // 별도의 인증처리를 받지 않았기 때문에, logout 에 접근할 수 없음
        model.addAttribute("accessToken", authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("refreshToken", authorizedClient.getRefreshToken().getTokenValue());

        return "home";
    }

    @GetMapping(path = "logout")
    public String logout(Authentication authentication, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

        logoutHandler.logout(request, response, authentication);

        return "redirect:/";
    }


    private boolean hasTokenExpired(OAuth2Token token) {
        return this.clock.instant().isAfter(token.getExpiresAt().minus(this.clockSkew));
    }
}
