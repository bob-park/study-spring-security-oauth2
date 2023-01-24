package com.example.oauth2clientsociallogin.security.converters;

import com.example.oauth2clientsociallogin.security.model.FormUser;
import com.example.oauth2clientsociallogin.security.model.ProviderUser;
import com.example.oauth2clientsociallogin.security.model.User;

public class UserDetailsProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {


    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (providerUserRequest.user() == null) {
            return null;
        }

        User user = providerUserRequest.user();

        return FormUser.builder()
            .id(user.getId())
            .username(user.getUsername())
            .password(user.getPassword())
            .email(user.getEmail())
            .authorities(user.getAuthorities())
            .provider("none")
            .build();
    }
}
