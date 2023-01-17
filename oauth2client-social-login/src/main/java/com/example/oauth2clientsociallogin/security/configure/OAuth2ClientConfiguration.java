package com.example.oauth2clientsociallogin.security.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class OAuth2ClientConfiguration {

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web ->
            web.ignoring()
                .antMatchers(
                    "/static/js/**",
                    "/static/images/**",
                    "/static/css/**",
                    "/static/scss/**",
                    "/static/icommon/**");
    }

    @Bean
    public SecurityFilterChain http(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authRequest ->
            authRequest.antMatchers("/").permitAll()
                .anyRequest().authenticated());

        http.oauth2Login(Customizer.withDefaults());

        http.logout().logoutSuccessUrl("/");

        return http.build();
    }

}
