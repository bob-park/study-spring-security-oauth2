package com.example.oauth2client2.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity
@Configuration
public class OAuth2ClientConfiguration {


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRequest ->
                authRequest
                    .antMatchers("/", "/oauth2Login", "/client").permitAll()
                    .anyRequest().authenticated())
            .oauth2Client(Customizer.withDefaults());

        return http.build();
    }


}
