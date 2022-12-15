package com.bob.oauth2client.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class OAuth2ClientConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests ->
            requests
                .antMatchers("/loginPage").permitAll()
                .antMatchers("/user").permitAll()
                .antMatchers("/oidc").permitAll()
                .anyRequest().authenticated());

//        http.oauth2Login(oauth2 -> oauth2.loginPage("/loginPage"));
        http.oauth2Login(Customizer.withDefaults());

        return http.build();
    }


}
