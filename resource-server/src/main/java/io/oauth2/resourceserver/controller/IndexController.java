package io.oauth2.resourceserver.controller;

import java.net.URI;
import java.net.URISyntaxException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
public class IndexController {

    @GetMapping("")
    public String index() {
        return "index";
    }

    @GetMapping("api/user")
    public Authentication user(Authentication authentication, @AuthenticationPrincipal Jwt principal)
        throws URISyntaxException {

        JwtAuthenticationToken authenticationToken = (JwtAuthenticationToken) authentication;

        String sub = (String) authenticationToken.getTokenAttributes().get("sub");
        String email = (String) authenticationToken.getTokenAttributes().get("email");
        String scope = (String) authenticationToken.getTokenAttributes().get("scope");

        String sub1 = principal.getClaim("sub");
        String email1 = principal.getClaim("email");
        String scope1 = principal.getClaim("scope");

        log.debug("sub={}, sub1={}", sub, sub1);
        log.debug("email={}, email1={}", email, email1);
        log.debug("scope={}, scope1={}", scope, scope1);

        // * 해당 Token 을 가지고 다른 resource server 로부터 데이터를 요청할 수 있다.
        String tokenValue = principal.getTokenValue();

        // ex
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(tokenValue);

        RequestEntity<String> requestEntity =
            new RequestEntity<>(headers, HttpMethod.GET, new URI("http://localhost:8082"));

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);

        String body = responseEntity.getBody();

        log.debug("body={}", body);

        return authentication;
    }

}
