package io.oauth2.resourceserver.filter.authorization;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public abstract class JwtAuthorizationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final JWSVerifier jwsVerifier;

    protected JwtAuthorizationFilter(JWSVerifier jwsVerifier) {
        this.jwsVerifier = jwsVerifier;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (tokenResolve(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verify = signedJWT.verify(jwsVerifier);

            if (verify) {
                // 인증 처리
                JWTClaimsSet jwtClaimsSet = signedJWT.getJWTClaimsSet();

                String username = (String) jwtClaimsSet.getClaim("username");
                List<String> authorities = (List<String>) jwtClaimsSet.getClaim("authorities");

                if (isNotBlank(username)) {
                    UserDetails user =
                        User.withUsername(username)
                            .password(UUID.randomUUID().toString())
                            .authorities(authorities.toArray(String[]::new))
                            .build();

                    Authentication authentication =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }


        } catch (ParseException | JOSEException e) {
            throw new RuntimeException(e);
        }

        filterChain.doFilter(request, response);

    }

    protected String getToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION).replace(AUTHORIZATION_HEADER_PREFIX, "");
    }

    protected boolean tokenResolve(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        return header == null || !header.startsWith(AUTHORIZATION_HEADER_PREFIX);
    }
}
