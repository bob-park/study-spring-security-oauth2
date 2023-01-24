package com.example.oauth2clientsociallogin.security.social.kakao.converter;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import com.example.oauth2clientsociallogin.security.converters.ProviderUserConverter;
import com.example.oauth2clientsociallogin.security.converters.ProviderUserRequest;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.social.kakao.model.KakaoOidcUser;
import com.example.oauth2clientsociallogin.security.social.kakao.model.KakaoUser;
import com.example.oauth2clientsociallogin.security.type.SocialType;
import com.example.oauth2clientsociallogin.security.utils.OAuth2Utils;

public class OAuth2KakaoOidcProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        ClientRegistration clientRegistration = providerUserRequest.clientRegistration();

        SocialType socialType = SocialType.findByName(clientRegistration.getRegistrationId());

        if (socialType != SocialType.KAKAO) {
            return null;
        }

        if (!(providerUserRequest.oAuth2User() instanceof OidcUser)) {
            return null;
        }

        return new KakaoOidcUser(
            OAuth2Utils.getMainAttributes(providerUserRequest.oAuth2User()),
            providerUserRequest.oAuth2User(),
            clientRegistration);
    }
}
