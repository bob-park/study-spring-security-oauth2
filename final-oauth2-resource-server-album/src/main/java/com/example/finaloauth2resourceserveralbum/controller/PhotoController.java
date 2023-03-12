package com.example.finaloauth2resourceserveralbum.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.finaloauth2resourceserveralbum.service.PhotoService;
import org.example.model.Photo;

@RestController
public class PhotoController {

    private final PhotoService photoService =
        (photoId, photoTitle, description, user) ->
            Photo.builder()
                .photoId(photoId)
                .photoTitle(photoTitle)
                .photoDescription(description)
                .userId(user)
                .build();

    @GetMapping("/photos")
    public List<Photo> photos() {

        Photo photo1 = photoService.build("1 ", "Photo1 title ", "Photo is nice ", "user1 ");
        Photo photo2 = photoService.build("2 ", "Photo2 title ", "Photo is beautiful ", "user2 ");

        return List.of(photo1, photo2);
    }


    @GetMapping("/tokenExpire")
    public Map<String, Object> tokenExpire() {

        Map<String, Object> result = new HashMap<>();
        result.put("error", new OAuth2Error("invalid token", "token is expired", null));

        return result;
    }

}
