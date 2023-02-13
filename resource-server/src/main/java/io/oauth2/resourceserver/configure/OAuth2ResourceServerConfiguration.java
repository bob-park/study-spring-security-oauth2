package io.oauth2.resourceserver.configure;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import io.oauth2.resourceserver.filter.authentication.JwtAuthenticationFilter;
import io.oauth2.resourceserver.filter.authorization.JwtAuthorizationFilter;
import io.oauth2.resourceserver.filter.authorization.JwtAuthorizationMacFilter;
import io.oauth2.resourceserver.filter.authorization.JwtAuthorizationRsaFilter;
import io.oauth2.resourceserver.signature.SecuritySigner;

@RequiredArgsConstructor
@Configuration
public class OAuth2ResourceServerConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement(sessionManager ->
            sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests(request ->
            request.requestMatchers("/").permitAll()
                .anyRequest().authenticated());

        http.userDetailsService(userDetailsService());
        http.addFilterBefore(jwtAuthenticationFilter(null, null), UsernamePasswordAuthenticationFilter.class)
//            .addFilterBefore(jwtAuthorizationMacFilter(null), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthorizationRsaFilter(null), UsernamePasswordAuthenticationFilter.class);

//        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    //    @Bean
    public JwtAuthorizationFilter jwtAuthorizationMacFilter(OctetSequenceKey octetSequenceKey) throws JOSEException {
        return new JwtAuthorizationMacFilter(octetSequenceKey);
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationRsaFilter(RSAKey rsaKey) throws JOSEException {
        return new JwtAuthorizationRsaFilter(rsaKey);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(SecuritySigner securitySigner, JWK jwk)
        throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter =
            new JwtAuthenticationFilter(securitySigner, jwk);

        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager(null));

        return jwtAuthenticationFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user =
            User.withUsername("user")
                .password("12345")
                .authorities("ROLE_USER")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
