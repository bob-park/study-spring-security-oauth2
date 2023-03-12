package org.example.model;

import lombok.Builder;

@Builder
public record Friend(String name, int age, String gender) {

}
