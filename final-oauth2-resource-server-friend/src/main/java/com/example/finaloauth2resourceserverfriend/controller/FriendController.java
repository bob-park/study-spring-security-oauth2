package com.example.finaloauth2resourceserverfriend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.example.model.Friend;

@RestController
public class FriendController {

    @GetMapping("/friends")
    public List<Friend> friends() {

        Friend friend1 = getFriend("friend1 ", 10, " man ");
        Friend friend2 = getFriend("friend2 ", 11, " woman ");

        return List.of(friend1, friend2);
    }

    private Friend getFriend(String name, int age, String gender) {
        return Friend.builder()
            .name(name)
            .age(age)
            .gender(gender)
            .build();
    }

}
