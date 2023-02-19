package com.example.oauth2clientresourceserver.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.oauth2clientresourceserver.model.Photo;

@RestController
public class PhotoController {

    @GetMapping("photos")
    public List<Photo> photos() {
        Photo photo1 = getPhoto("1", "Photo 1 title", "Photo is nice", "user1");
        Photo photo2 = getPhoto("2", "Photo 2 title", "Photo is nice", "user2");

        return List.of(photo1, photo2);
    }

    @GetMapping("remotePhotos")
    public List<Photo> remotePhotos() {
        Photo photo1 = getPhoto("1", "Remote Photo 1 title", "Photo is nice", "user1");
        Photo photo2 = getPhoto("2", "Remote Photo 2 title", "Photo is nice", "user2");

        return List.of(photo1, photo2);
    }

    private Photo getPhoto(String photoId, String photoTitle, String photoDescription, String userId) {
        return Photo.builder()
            .photoId(photoId)
            .photoTitle(photoTitle)
            .photoDescription(photoDescription)
            .userId(userId)
            .build();
    }

}
