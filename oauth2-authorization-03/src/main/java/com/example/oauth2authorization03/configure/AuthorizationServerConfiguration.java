package com.example.oauth2authorization03.configure;

import java.util.List;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeRequestAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.oauth2authorization03.provider.CustomAuthenticationProvider;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class AuthorizationServerConfiguration {

    @Bean
    public SecurityFilterChain authSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer.oidc(Customizer.withDefaults());

        RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();

        // custom
        authorizationServerConfigurer.authorizationEndpoint(
            authorizationEndpoint ->
                authorizationEndpoint
                    .authenticationProvider(customAuthenticationProvider(null, null, null))
                    .authorizationResponseHandler(
                        (request, response, authentication) -> {
                            OAuth2AuthorizationCodeRequestAuthenticationToken authentication1 = (OAuth2AuthorizationCodeRequestAuthenticationToken) authentication;
                            log.debug("authentication={}", authentication1);
                            String redirectUri = authentication1.getRedirectUri();
                            String authorizationCode = authentication1.getAuthorizationCode().getTokenValue();
                            String state = null;
                            if (StringUtils.hasText(authentication1.getState())) {
                                state = authentication1.getState();
                            }
                            response.sendRedirect(redirectUri + "?code=" + authorizationCode + "&state=" + state);
                        })
                    .errorResponseHandler(
                        (request, response, exception) -> {
                            log.error("Authorization Error - {}", exception.getMessage(), exception);
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                        }));

        http
            .securityMatcher(endpointsMatcher)
            .authorizeHttpRequests(authorize ->
                authorize.anyRequest().authenticated())
            .csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
            .apply(authorizationServerConfigurer);

        http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

        http.exceptionHandling(
            exception ->
                exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService() {
        return new InMemoryOAuth2AuthorizationConsentService();
    }

    @Bean
    public AuthenticationProvider customAuthenticationProvider(RegisteredClientRepository registeredClientRepository,
        OAuth2AuthorizationService oAuth2AuthorizationService,
        OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService) {
        return new CustomAuthenticationProvider(registeredClientRepository, oAuth2AuthorizationService,
            oAuth2AuthorizationConsentService);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3_600L);
        configuration.setExposedHeaders(
            List.of(
                HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
                HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
                HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
                HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS,
                HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD,
                HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN,
                HttpHeaders.AUTHORIZATION,
                HttpHeaders.CONTENT_TYPE,
                HttpHeaders.CONTENT_DISPOSITION,
                HttpHeaders.CONTENT_LENGTH,
                HttpHeaders.ORIGIN,
                HttpHeaders.ACCEPT));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

}
