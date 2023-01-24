package com.example.oauth2clientsociallogin.security.utils;

import java.util.Map;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.oauth2clientsociallogin.security.model.Attributes;

public interface OAuth2Utils {

    static Attributes getMainAttributes(OAuth2User oAuth2User) {

        return Attributes.builder()
            .mainAttributes(oAuth2User.getAttributes())
            .build();
    }

    static Attributes getSubAttributes(OAuth2User oAuth2User, String subAttributesKey) {

        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        return Attributes.builder()
            .subAttributes(subAttributes)
            .build();
    }

    static Attributes getOtherAttributes(OAuth2User oAuth2User, String subAttributesKey, String otherAttributesKey) {

        Map<String, Object> subAttributes = (Map<String, Object>) oAuth2User.getAttributes().get(subAttributesKey);
        Map<String, Object> otherAttributes = (Map<String, Object>) subAttributes.get(otherAttributesKey);

        return Attributes.builder()
            .subAttributes(subAttributes)
            .otherAttributes(otherAttributes)
            .build();
    }

}
