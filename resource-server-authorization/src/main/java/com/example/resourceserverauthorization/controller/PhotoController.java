package com.example.resourceserverauthorization.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.resourceserverauthorization.model.Photo;

@RestController
public class PhotoController {

    @GetMapping(path = "photo/1")
    public Photo photo1() {
        return Photo.builder()
            .photoId("1")
            .photoTitle("Photo 1 title")
            .photoDescription("Photo is nice.")
            .userId("user1")
            .build();
    }

    @PreAuthorize("hasAuthority('SCOPE_photo')")
    @GetMapping(path = "photo/2")
    public Photo photo2() {
        return Photo.builder()
            .photoId("2")
            .photoTitle("Photo 2 title")
            .photoDescription("Photo is nice.")
            .userId("user2")
            .build();
    }

    @GetMapping(path = "photo/3")
    public Photo photo3() {
        return Photo.builder()
            .photoId("3")
            .photoTitle("Photo 3 title")
            .photoDescription("Photo is nice.")
            .userId("user3")
            .build();
    }

}
