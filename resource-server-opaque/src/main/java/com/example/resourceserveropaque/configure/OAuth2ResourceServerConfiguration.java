package com.example.resourceserveropaque.configure;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Opaquetoken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.server.resource.introspection.NimbusOpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;

import com.example.resourceserveropaque.instrospector.CustomOpaqueTokenIntrospector;

@Configuration
public class OAuth2ResourceServerConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests.anyRequest().authenticated());
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::opaqueToken);
        return http.build();
    }

    @Bean
    public OpaqueTokenIntrospector opaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
        Opaquetoken opaquetoken = properties.getOpaquetoken();

        return new NimbusOpaqueTokenIntrospector(
            opaquetoken.getIntrospectionUri(),
            opaquetoken.getClientId(),
            opaquetoken.getClientSecret());
    }

    public OpaqueTokenIntrospector customOpaqueTokenIntrospector(OAuth2ResourceServerProperties properties) {
        return new CustomOpaqueTokenIntrospector(properties);
    }


}
