package com.bob.oauth2client.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class OAuth2ClientConfiguration {

    private final ClientRegistrationRepository clientRegistrationRepository;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // * default
//        http.authorizeHttpRequests(requests ->
//            requests
//                .antMatchers("/loginPage").permitAll()
//                .antMatchers("/user").permitAll()
//                .antMatchers("/oidc").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/home").permitAll()
//                .anyRequest().authenticated());

//        http.oauth2Login(oauth2 -> oauth2.loginPage("/loginPage"));
//        http.oauth2Login(Customizer.withDefaults());

        //* OIDC
//        http.logout()
//            .logoutSuccessHandler(oidcLogoutSuccessHandler())
//            .invalidateHttpSession(true)
//            .clearAuthentication(true)
//            .deleteCookies("JSESSIONID");

        // * Custom Authorization BaseUri & Redirect BaseUri
//        http.oauth2Login(oauth2 ->
//            oauth2.loginPage("/login")
//                // ! redirectionEndpointConfig.baseUri() 와 동일한 결과를 가져오지만, redirectionEndpointConfig.baseUri() 의 우선순위가 높다.
//                .loginProcessingUrl("/login/v2/oauth2/code/*")
//                .authorizationEndpoint(authorizationEndpointConfig ->
//                    authorizationEndpointConfig.baseUri("/oauth2/v1/authorization"))
//                .redirectionEndpoint(redirectionEndpointConfig ->
//                    redirectionEndpointConfig.baseUri("/login/v1/oauth2/code/*")));

        // Custom OAuRth2AuthorizationRequestResolver
//        http.oauth2Login(oauth2Login ->
//            oauth2Login.authorizationEndpoint(authorizationEndpointConfig ->
//                authorizationEndpointConfig.authorizationRequestResolver(customOAuth2AuthorizationRequestResolver())));
//
//        http.logout(logoutConfig -> logoutConfig.logoutSuccessUrl("/home"));

        // * default oAuth2Client()
        http.authorizeHttpRequests(authRequest ->
                authRequest
                    .antMatchers("/home", "/client").permitAll()
                    .anyRequest().authenticated())
            // ! OAuth 2.0 인가 서버로부터 인가, 최종 클라이언트가 인증 처리까지 받을 수 있음
//            .oauth2Login(Customizer.withDefaults())
            // ! OAuth 2.0 인가 요청까지만 가능, 최종 사용자의 인증 처리까지 하지 않음 - 별도로 custom 하게 추가해야함
            .oauth2Client(Customizer.withDefaults())
            .logout(logout -> logout.logoutSuccessUrl("/home"));



        return http.build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
            new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        successHandler.setPostLogoutRedirectUri("http://localhost:8081/login");

        return successHandler;
    }

    private OAuth2AuthorizationRequestResolver customOAuth2AuthorizationRequestResolver() {
        return new CustomOAuth2AuthorizationRequestResolver(clientRegistrationRepository, "/oauth2/authorization");
    }


}
