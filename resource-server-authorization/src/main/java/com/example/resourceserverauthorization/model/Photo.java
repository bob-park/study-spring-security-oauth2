package com.example.resourceserverauthorization.model;

import lombok.Builder;

@Builder
public record Photo(String userId, String photoId, String photoTitle, String photoDescription) {

}
