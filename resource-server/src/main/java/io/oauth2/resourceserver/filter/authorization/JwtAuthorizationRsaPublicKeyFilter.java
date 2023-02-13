package io.oauth2.resourceserver.filter.authorization;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class JwtAuthorizationRsaPublicKeyFilter extends JwtAuthorizationFilter {

    private final JwtDecoder jwtDecoder;

    public JwtAuthorizationRsaPublicKeyFilter(JwtDecoder jwtDecoder) {
        super(null);
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        if (tokenResolve(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getToken(request);

        if (jwtDecoder != null) {

            Jwt jwt = jwtDecoder.decode(token);

            String username = jwt.getClaim("username");
            List<String> authorities = jwt.getClaimAsStringList("authorities");

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

        filterChain.doFilter(request, response);
    }
}
