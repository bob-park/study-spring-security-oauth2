package com.bob.basicoauth2.security.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// @Configuration
public class SecurityConfiguration {

    // @Bean
    // public SecurityFilterChain httpSecurity1(HttpSecurity http) throws Exception {
    //
    //     http.authorizeHttpRequests().anyRequest().authenticated();
    //     http.formLogin();
    //     // http.apply(new CustomSecurityConfigurer().setFlag(true));
    //
    //     return http.build();
    // }

    @Bean
    public SecurityFilterChain httpSecurity2(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests().anyRequest().authenticated();
        http.httpBasic();

        return http.build();
    }


}
