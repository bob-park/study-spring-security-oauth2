package com.bob.oauth2client.configure;

import java.util.Map;
import java.util.function.Consumer;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest.Builder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.google.common.collect.Maps;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {


    private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";

    private final DefaultOAuth2AuthorizationRequestResolver defaultOAuth2AuthorizationRequestResolver;
    private final ClientRegistrationRepository clientRegistrationRepository;
    private final AntPathRequestMatcher authorizationRequestMatcher;

    private static final Consumer<OAuth2AuthorizationRequest.Builder> DEFAULT_PKCE_APPLIER =
        OAuth2AuthorizationRequestCustomizers.withPkce();

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository,
        String baseUri) {

        this.clientRegistrationRepository = clientRegistrationRepository;

        this.authorizationRequestMatcher = new AntPathRequestMatcher(
            baseUri + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}");

        this.defaultOAuth2AuthorizationRequestResolver =
            new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, baseUri);

    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {

        String registrationId = resolveRegistrationId(request);
        if (registrationId == null) {
            return null;
        }

        // keycloak1, keycloak2 인 경우 DefaultOAuth2AuthorizationRequestResolver 사용

        if ("keycloakWithPKCE".equals(registrationId)) {
            OAuth2AuthorizationRequest oAuth2AuthorizationRequest =
                defaultOAuth2AuthorizationRequestResolver.resolve(request);

            return customResolve(oAuth2AuthorizationRequest, registrationId);
        }

        return defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }


    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {

        if (registrationId == null) {
            return null;
        }

        // keycloak1, keycloak2 인 경우 DefaultOAuth2AuthorizationRequestResolver 사용

        if ("keycloakWithPKCE".equals(registrationId)) {
            OAuth2AuthorizationRequest oAuth2AuthorizationRequest =
                defaultOAuth2AuthorizationRequestResolver.resolve(request);

            return customResolve(oAuth2AuthorizationRequest, registrationId);
        }

        return defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }

    private OAuth2AuthorizationRequest customResolve(OAuth2AuthorizationRequest authorizationRequest,
        String registrationId) {

        Map<String, Object> extraParams = Maps.newHashMap();
        extraParams.put("customName1", "customValue1");
        extraParams.put("customName2", "customValue2");
        extraParams.put("customName3", "customValue3");

        Builder builder =
            OAuth2AuthorizationRequest.from(authorizationRequest)
                .additionalParameters(extraParams);

        DEFAULT_PKCE_APPLIER.accept(builder);

        return builder.build();
    }

    private String resolveRegistrationId(HttpServletRequest request) {
        if (this.authorizationRequestMatcher.matches(request)) {
            return this.authorizationRequestMatcher.matcher(request).getVariables()
                .get(REGISTRATION_ID_URI_VARIABLE_NAME);
        }
        return null;
    }
}
