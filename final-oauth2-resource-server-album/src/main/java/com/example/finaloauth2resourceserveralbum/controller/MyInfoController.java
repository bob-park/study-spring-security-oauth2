package com.example.finaloauth2resourceserveralbum.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.finaloauth2resourceserveralbum.service.PhotoService;
import org.example.model.Friend;
import org.example.model.MyInfo;
import org.example.model.Photo;

@RequiredArgsConstructor
@RestController
public class MyInfoController {

    private final RestTemplate restTemplate;
    private final PhotoService photoService =
        (photoId, photoTitle, description, user) ->
            Photo.builder()
                .photoId(photoId)
                .photoTitle(photoTitle)
                .photoDescription(description)
                .userId(user)
                .build();

    @GetMapping("/myInfo")
    public MyInfo myInfo(JwtAuthenticationToken authenticationToken) {

        HttpHeaders header = new HttpHeaders();
        header.add("Authorization", "Bearer " + authenticationToken.getToken().getTokenValue());
        HttpEntity<?> entity = new HttpEntity<>(header);
        String url = "http://localhost:8083/friends";
        ResponseEntity<List<Friend>> response = restTemplate.exchange(url, HttpMethod.GET, entity,
            new ParameterizedTypeReference<>() {
            });

        Photo photo1 = photoService.build("1 ", "Album1 title ", "Album is nice ", "user1");
        Photo photo2 = photoService.build("2 ", "Album2 title ", "Album is beautiful ", "user2");

        return MyInfo.builder().photos(List.of(photo1, photo2)).friends(response.getBody()).build();
    }

}
