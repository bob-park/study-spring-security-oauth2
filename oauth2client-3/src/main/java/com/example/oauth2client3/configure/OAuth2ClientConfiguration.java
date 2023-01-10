package com.example.oauth2client3.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.oauth2client3.filter.CustomOAuth2AuthenticationFilter;

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class OAuth2ClientConfiguration {

    private final DefaultOAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientRepository authorizedClientRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRequest ->
            authRequest.antMatchers("/", "/oauth2Login", "/client", "/v2/oauth2Login").permitAll()
                .anyRequest().authenticated());

        http.oauth2Client(Customizer.withDefaults());

        http.addFilterBefore(customOAuth2AuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    private CustomOAuth2AuthenticationFilter customOAuth2AuthenticationFilter() {

        CustomOAuth2AuthenticationFilter authenticationFilter =
            new CustomOAuth2AuthenticationFilter(authorizedClientManager, authorizedClientRepository);

        authenticationFilter.setAuthenticationSuccessHandler(
            ((request, response, authentication) ->
                response.sendRedirect("/home")));

        return authenticationFilter;
    }


}
