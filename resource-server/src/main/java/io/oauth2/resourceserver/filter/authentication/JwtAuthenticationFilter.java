package io.oauth2.resourceserver.filter.authentication;

import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWK;
import io.oauth2.resourceserver.model.LoginRequest;
import io.oauth2.resourceserver.signature.SecuritySigner;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final SecuritySigner securitySigner;
    private final JWK jwk;

    public JwtAuthenticationFilter(SecuritySigner securitySigner, JWK jwk) {
        this.securitySigner = securitySigner;
        this.jwk = jwk;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
        throws AuthenticationException {

        ObjectMapper mapper = new ObjectMapper();

        LoginRequest loginRequest;

        try {
            loginRequest =
                mapper.readValue(request.getInputStream(), LoginRequest.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password());

        return getAuthenticationManager().authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
        Authentication authResult) throws IOException, ServletException {

        User user = (User) authResult.getPrincipal();

        // 토큰 발행
        ;
        try {
            String jwt = securitySigner.getToken(user, jwk);

            response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }

    }
}
