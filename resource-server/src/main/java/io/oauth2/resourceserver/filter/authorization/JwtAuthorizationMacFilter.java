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
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JwtAuthorizationMacFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";

    private final OctetSequenceKey octetSequenceKey;

    public JwtAuthorizationMacFilter(OctetSequenceKey octetSequenceKey) {
        this.octetSequenceKey = octetSequenceKey;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = header.replace(AUTHORIZATION_HEADER_PREFIX, "");

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            boolean verify = signedJWT.verify(new MACVerifier(octetSequenceKey.toSecretKey()));

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
}
