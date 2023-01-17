package com.example.oauth2clientsociallogin.security.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.security.core.GrantedAuthority;

@ToString
@Getter
@Setter
@NoArgsConstructor
public class User {

    private String registrationId;
    private String id;
    private String username;
    private String password;
    private String provider;
    private String email;
    private List<? extends GrantedAuthority> authorities;

    @Builder
    private User(String registrationId, String id, String username, String password, String provider, String email,
        List<? extends GrantedAuthority> authorities) {
        this.registrationId = registrationId;
        this.id = id;
        this.username = username;
        this.password = password;
        this.provider = provider;
        this.email = email;
        this.authorities = authorities;
    }
}
