package com.example.oauth2clientsociallogin.security.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import com.example.oauth2clientsociallogin.security.service.CustomOAuth2UserService;
import com.example.oauth2clientsociallogin.security.service.CustomOidcUserService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class OAuth2ClientConfiguration {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final CustomOidcUserService customOidcUserService;

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
            authRequest
                .antMatchers("/api/user").hasAnyRole("SCOPE_profile", "SCOPE_email")
                .antMatchers("/api/oidc").hasAnyRole("SCOPE_openid")
                .antMatchers("/").permitAll()
                .anyRequest().authenticated());

        http.formLogin()
            .loginPage("/login")
            .loginProcessingUrl("/loginProc")
            .defaultSuccessUrl("/")
            .permitAll();

        http.oauth2Login(oauth2 ->
            oauth2.userInfoEndpoint(endpoint ->
                endpoint
                    .userService(customOAuth2UserService)
                    .oidcUserService(customOidcUserService)));

        http.logout().logoutSuccessUrl("/");

        http.exceptionHandling(exception ->
            exception.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login")));

        return http.build();
    }

    @Bean
    public GrantedAuthoritiesMapper customAuthorityMapper() {
        return new CustomAuthorityMapper();
    }

}
