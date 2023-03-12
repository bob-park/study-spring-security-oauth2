package com.example.finaloauth2resourceserveralbum.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class OAuth2ResourceServerConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain1(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(
            requests ->
                requests.requestMatchers("/photos", "/remotePhotos", "/myInfo").hasAuthority("SCOPE_photo")
                    .anyRequest().authenticated());

        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        http.cors().configurationSource(corsConfigurationSource());
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;

    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
