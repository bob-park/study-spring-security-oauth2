package com.example.oauth2client3.filter;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.mapping.SimpleAuthorityMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizationSuccessHandler;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

@Slf4j
public class CustomOAuth2AuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    public static final String DEFAULT_FILTER_PROCESSING_URI = "/oauth2Login/**";

    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;
    private final OAuth2AuthorizationSuccessHandler successHandler;

    private final Duration clockSkew = Duration.ofSeconds(3_600);
    private final Clock clock = Clock.systemUTC();

    // 필터가 동작하는 url pattern
    public CustomOAuth2AuthenticationFilter(DefaultOAuth2AuthorizedClientManager authorizedClientManager,
        OAuth2AuthorizedClientRepository authorizedClientRepository) {
        super(DEFAULT_FILTER_PROCESSING_URI);

        this.authorizedClientManager = authorizedClientManager;
        this.authorizedClientRepository = authorizedClientRepository;

        this.successHandler =
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
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException, IOException, ServletException {

        // ! Controller 에서는 해당 부분을 실행할 경우 Spring Security 의 Filter 를 거치기 때문에 Authentication 에 Anonymous Authentication Token 를 넣어줌으로써 null 이 될수 없다.
        // Anonymous Authentication Token 은 UsernamePasswordAuthenticationFilter 뒤에서 넣어줌
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            authentication = new AnonymousAuthenticationToken(
                "anonymous", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
        }

        OAuth2AuthorizeRequest authorizeRequest =
            OAuth2AuthorizeRequest.withClientRegistrationId("keycloak")
                .principal(authentication)
                .attribute(HttpServletRequest.class.getName(), request)
                .attribute(HttpServletResponse.class.getName(), response)
                .build();

        OAuth2AuthorizedClient authorizedClient = authorizedClientManager.authorize(authorizeRequest);

        // 권한 부여 타입을 변경하지 않고 실행
        if (authorizedClient != null
            && hasTokenExpired(authorizedClient.getAccessToken()) && authorizedClient.getRefreshToken() != null) {
            authorizedClientManager.authorize(authorizeRequest);
        }

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

            // spring security 에서 OAuth2AuthorizedClientManager 에서 client 가 인가 받을 경우 기본 설정된 DefaultOAuth2AuthorizedClientManager 에 의해 OAuth2AuthorizationSuccessHandler 가 호출되어 OAuth2AuthorizedClientRepository 에 인가 받은 OAuth2AuthorizedClient 가 저장됨
            // 하지만, 로직으로 인증받은 OAuth2AuthorizedClient 는 다시 OAuth2AuthorizedClientRepository 에 저장하여 인증까지 받은 OAuth2AuthorizedClient 를 다시 넣어주어야 한다.
            this.successHandler.onAuthorizationSuccess(authorizedClient, authenticationToken,
                createAttributes(request, response));

            return authenticationToken;
        }

        return null;
    }

    private boolean hasTokenExpired(OAuth2Token token) {
        return this.clock.instant().isAfter(token.getExpiresAt().minus(this.clockSkew));
    }

    private static Map<String, Object> createAttributes(HttpServletRequest servletRequest,
        HttpServletResponse servletResponse) {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(HttpServletRequest.class.getName(), servletRequest);
        attributes.put(HttpServletResponse.class.getName(), servletResponse);
        return attributes;
    }
}
