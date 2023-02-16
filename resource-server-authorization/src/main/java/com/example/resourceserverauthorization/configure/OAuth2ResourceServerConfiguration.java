package com.example.resourceserverauthorization.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class OAuth2ResourceServerConfiguration {

    // scope include photo
    @Bean
    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {

        // ! SecurityFilterChain 에서 경로 패턴을 완전히 분리 시킬려면 securityMatcher(..) 로 해야 적용된다.
        http.securityMatcher("/photo/1")
            .authorizeHttpRequests(requests ->
                requests
                    .requestMatchers("/photo/1").hasAuthority("SCOPE_photo")
                    .anyRequest().authenticated());
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChain2(HttpSecurity http) throws Exception {

        http.securityMatcher("/photo/2")
            .authorizeHttpRequests(requests ->
                requests
                    .requestMatchers(HttpMethod.GET, "/photo/2").permitAll());
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

}
