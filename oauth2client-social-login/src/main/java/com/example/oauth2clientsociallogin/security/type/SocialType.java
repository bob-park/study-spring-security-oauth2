package com.example.oauth2clientsociallogin.security.type;

import java.util.Arrays;
import java.util.stream.Stream;

public enum SocialType {
    GOOGLE("google"),
    NAMER("naver"),
    KAKAO("kakao");

    private final String name;

    SocialType(String name) {
        this.name = name;
    }

    public static SocialType findByName(String name) {
        return Arrays.stream(SocialType.values())
            .filter(socialType -> socialType.getName().equals(name))
            .findAny()
            .orElse(null);
    }

    public String getName() {
        return name;
    }


}
