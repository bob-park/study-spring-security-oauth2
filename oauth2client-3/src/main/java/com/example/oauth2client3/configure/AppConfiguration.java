package com.example.oauth2client3.configure;

import java.time.Duration;
import java.util.Map;
import java.util.function.Function;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.OAuth2AuthorizationContext;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

@Configuration
public class AppConfiguration {

    @Bean
    public DefaultOAuth2AuthorizedClientManager oAuth2AuthorizedClientManager(
        ClientRegistrationRepository clientRegistrationRepository,
        OAuth2AuthorizedClientRepository authorizedClientRepository) {

        OAuth2AuthorizedClientProvider auth2AuthorizedClientProvider =
            OAuth2AuthorizedClientProviderBuilder.builder()
                .authorizationCode()
                .password(password -> password.clockSkew(Duration.ofSeconds(3_600)))
                .clientCredentials()
                .refreshToken(refresh -> refresh.clockSkew(Duration.ofSeconds(3_600)))
                .build();

        DefaultOAuth2AuthorizedClientManager auth2AuthorizedClientManager =
            new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

        auth2AuthorizedClientManager.setAuthorizedClientProvider(auth2AuthorizedClientProvider);
        auth2AuthorizedClientManager.setContextAttributesMapper(contextAttributeMapper());

        return auth2AuthorizedClientManager;
    }

    private Function<OAuth2AuthorizeRequest, Map<String, Object>> contextAttributeMapper() {
        return oauth2request -> {

            Map<String, Object> contextAttribute = Maps.newHashMap();

            HttpServletRequest request =
                oauth2request.getAttribute(HttpServletRequest.class.getName()); // HttpServletRequest 얻어오기

            String username = request.getParameter(OAuth2ParameterNames.USERNAME);
            String password = request.getParameter(OAuth2ParameterNames.PASSWORD);

            if (StringUtils.isNotBlank(username) && StringUtils.isNotBlank(password)) {
                contextAttribute.put(OAuth2AuthorizationContext.USERNAME_ATTRIBUTE_NAME, username);
                contextAttribute.put(OAuth2AuthorizationContext.PASSWORD_ATTRIBUTE_NAME, password);
            }

            return contextAttribute;
        };
    }

}
