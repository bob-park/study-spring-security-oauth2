package com.example.oauth2clientresourceserver.model;

import lombok.Builder;


@Builder
public record Photo(String photoId, String photoTitle, String photoDescription, String userId) {


}
