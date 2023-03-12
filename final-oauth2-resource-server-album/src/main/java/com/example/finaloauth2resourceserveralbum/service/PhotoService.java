package com.example.finaloauth2resourceserveralbum.service;

import org.example.model.Photo;

public interface PhotoService {

    Photo build(String photoId, String photoTitle, String description, String user);

}
