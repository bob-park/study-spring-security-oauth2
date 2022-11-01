package com.bob.cors2.model;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class User {

    private final String name;
    private final int age;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
