package com.example.oauth2clientserver.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2ClientConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
            requests.requestMatchers("/").permitAll()
                .anyRequest().authenticated());

        http.oauth2Login(authLogin ->
            authLogin.defaultSuccessUrl("/"));

        return http.build();
    }

}
