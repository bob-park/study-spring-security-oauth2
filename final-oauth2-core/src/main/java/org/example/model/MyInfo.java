package org.example.model;

import java.util.List;

import lombok.Builder;

@Builder
public record MyInfo(List<Photo> photos, List<Friend> friends) {

}
