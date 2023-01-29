package com.example.oauth2resourceserver.configure;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class OAuth2ResourceServerConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests(request -> request.anyRequest().authenticated());
        http.oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();
    }

    /**
     * ! JwtDecoder 는 1개만 Bean 을 생성할 수 있다.
     *
     * @param properties
     * @return
     */
//    @Bean
//    public JwtDecoder jwtDecoder1(OAuth2ResourceServerProperties properties) {
//        return JwtDecoders.fromIssuerLocation(properties.getJwt().getIssuerUri());
//    }

    /**
     * * OIDC Issuer URI
     *
     * @param properties
     * @return
     */
//    @Bean
//    public JwtDecoder jwtDecoder2(OAuth2ResourceServerProperties properties) {
//        return JwtDecoders.fromOidcIssuerLocation(properties.getJwt().getIssuerUri());
//    }

    /**
     * * NimubsJwtDecoder 를 바로 생성
     * <p>
     * - 별도의 설정을 추가로 할 수 있다.
     *
     * @param properties
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder2(OAuth2ResourceServerProperties properties) {
        return NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri())
            .jwsAlgorithm(SignatureAlgorithm.RS512)
            .build();
    }

}
