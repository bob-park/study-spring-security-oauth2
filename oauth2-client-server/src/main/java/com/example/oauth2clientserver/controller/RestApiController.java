package com.example.oauth2clientserver.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.oauth2clientserver.model.AccessToken;
import com.example.oauth2clientserver.model.Photo;


@RequiredArgsConstructor
@RestController
public class RestApiController {

    private final RestTemplate restTemplate;


    @GetMapping("/token")
    public OAuth2AccessToken token(@RegisteredOAuth2AuthorizedClient("keycloak") OAuth2AuthorizedClient client) {
        return client.getAccessToken();
    }

    @GetMapping("/photos")
    public List<Photo> photos(AccessToken accessToken) {

        HttpHeaders headers = new HttpHeaders();

        headers.setBearerAuth(accessToken.token());

        HttpEntity<?> entity = new HttpEntity<>(headers);

        String url = "http://localhost:8082/photos";

        ResponseEntity<List<Photo>> response =
            restTemplate.exchange(url, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {
                });

        return response.getBody();
    }

}
