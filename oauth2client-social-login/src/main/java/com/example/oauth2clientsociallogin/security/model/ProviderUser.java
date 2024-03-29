package com.example.oauth2clientsociallogin.security.model;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;

public interface ProviderUser {

    String getId();

    String getUsername();

    String getPassword();

    String getEmail();

    String getProvider();

    String getPicture();

    List<? extends GrantedAuthority> getAuthorities();

    Map<String, Object> getAttributes();

}
