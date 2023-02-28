package com.example.oauth2authorization01.configure;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;


/**
 * OAuth 2.0 Authorization Server 구성 방법
 * <p>
 * 1. import 방식으로 OAuth2AuthorizationServerConfigurer 활성화
 */
//@Import(OAuth2AuthorizationServerConfiguration.class)
@Configuration
public class OAuth2AuthorizationServerConfiguration01 {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // 2. OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(..) 호출 -- 공식문서에서 사용
//        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        // 3. 사용자 정의
        OAuth2AuthorizationServerConfigurer authorizationServerConfiguration = new OAuth2AuthorizationServerConfigurer();

        //! 기본적으로 oidc 가 disabled 되어 있음
        // /userinfo 에 대한 request matcher 가 oidc 에 들어있음
        authorizationServerConfiguration.oidc(Customizer.withDefaults());

        http.apply(authorizationServerConfiguration);

        return http.build();
    }

    @Bean
    public AuthorizationServerSettings settings() {
        return AuthorizationServerSettings.builder()
            .issuer("http://localhost:9000")
            .build();
    }

    /**
     * OAuth 2.0 Authorization Server 는 반드시 클라이언트 등록 처리
     *
     * @return RegisteredClientRepository
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("oauth2-client-app")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://127.0.0.1:8081/login/oauth2/code/oauth2-client-app")
            .redirectUri("http://127.0.0.1:8081")
            .scope(OidcScopes.OPENID)
            .scope("read")
            .scope("write")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build();

        return new InMemoryRegisteredClientRepository(registeredClient);
    }
}
