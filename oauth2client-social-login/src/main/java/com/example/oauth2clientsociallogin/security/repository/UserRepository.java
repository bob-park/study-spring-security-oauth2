package com.example.oauth2clientsociallogin.security.repository;

import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.oauth2clientsociallogin.security.model.User;
import com.google.common.collect.Maps;

@Repository
public class UserRepository {

    private Map<String, User> users = Maps.newHashMap();


    public User findByUsername(String username) {
        return users.get(username);
    }

    public void register(User user) {
        if (users.containsKey(user.getUsername())) {
            return;
        }

        users.put(user.getUsername(), user);
    }

}
