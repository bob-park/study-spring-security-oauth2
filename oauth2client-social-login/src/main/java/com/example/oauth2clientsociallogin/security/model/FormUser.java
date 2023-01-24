package com.example.oauth2clientsociallogin.security.model;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.springframework.security.core.GrantedAuthority;

@ToString
@Getter
public class FormUser implements ProviderUser{

    private final String id;
    private final String username;
    private final String password;
    private final String email;
    private final List<? extends GrantedAuthority> authorities;

    private final String provider;

    @Builder
    private FormUser(String id, String username, String password, String email,
        List<? extends GrantedAuthority> authorities, String provider) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
        this.provider = provider;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getProvider() {
        return provider;
    }

    @Override
    public String getPicture() {
        return null;
    }

    @Override
    public List<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }
}
