package com.example.oauth2clientsociallogin.security.converters;

import java.util.List;

import org.springframework.stereotype.Component;

import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.social.google.converter.OAuth2GoogleProviderUserConverter;
import com.example.oauth2clientsociallogin.security.social.naver.converter.OAuth2NaverProviderUserConverter;

import static com.google.common.base.Preconditions.checkArgument;

@Component
public class DelegatingProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    private final List<ProviderUserConverter<ProviderUserRequest, ProviderUser>> converters;

    public DelegatingProviderUserConverter() {
        this.converters =
            List.of(
                new OAuth2GoogleProviderUserConverter(),
                new OAuth2NaverProviderUserConverter());
    }

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        checkArgument(providerUserRequest != null, "providerUserRequest must be provided.");

        for (ProviderUserConverter<ProviderUserRequest, ProviderUser> converter : converters) {
            ProviderUser providerUser = converter.converter(providerUserRequest);

            if (providerUser != null) {
                return providerUser;
            }
        }

        return null;
    }
}
