package com.example.resourceserverauthorization.configure;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class OAuth2ResourceServerConfiguration {

    // scope include photo
    @Bean
    public SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
            jwt -> {
                String scopes = jwt.getClaim("scope");
                Map<String, Object> realmAccess = jwt.getClaimAsMap("realm_access");

                if (scopes == null || realmAccess == null) {
                    return Collections.emptyList();
                }

                Collection<? extends GrantedAuthority> authorities1 =
                    Arrays.stream(scopes.split("\\s"))
                        .map(roleName -> "ROLE_" + roleName)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                Collection<? extends GrantedAuthority> authorities2 =
                    ((List<String>) realmAccess.get("roles")).stream()
                        .map(roleName -> "ROLE_" + roleName)
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                return Stream.concat(authorities1.stream(), authorities2.stream())
                    .toList();
            });

        // ! SecurityFilterChain 에서 경로 패턴을 완전히 분리 시킬려면 securityMatcher(..) 로 해야 적용된다.
        http.securityMatcher("/photo/{id:[13]}")
            .authorizeHttpRequests(requests ->
                requests
                    .requestMatchers("/photo/1").hasAuthority("ROLE_photo")
                    .requestMatchers("/photo/3").hasAuthority("ROLE_default-roles-auth2")
                    .anyRequest().authenticated());

        http.oauth2ResourceServer(resourceServer ->
            resourceServer.jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter)));

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
