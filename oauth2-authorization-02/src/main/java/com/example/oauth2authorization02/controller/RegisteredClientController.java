package com.example.oauth2authorization02.controller;

import java.util.List;

import lombok.RequiredArgsConstructor;

import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("registered/client")
public class RegisteredClientController {

    private final RegisteredClientRepository clientRepository;

    @GetMapping(path = "list")
    public List<RegisteredClient> clients() {
        RegisteredClient client1 = clientRepository.findByClientId("oauth2-client-app");

        return List.of(client1);
    }

}
