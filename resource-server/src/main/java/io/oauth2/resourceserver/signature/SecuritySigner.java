package io.oauth2.resourceserver.signature;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

public abstract class SecuritySigner {

    protected String getJwtTokenInternal(JWSSigner jwsSigner, UserDetails user, JWK jwk) throws JOSEException {

        JWSHeader header =
            new JWSHeader.Builder((JWSAlgorithm) jwk.getAlgorithm())
                .keyID(jwk.getKeyID())
                .build();

        JWTClaimsSet jwtClaimsSet =
            new JWTClaimsSet.Builder()
                .subject("user")
                .issuer("http://localhost:8081")
                .claim("username", user.getUsername())
                .claim("authorities",
                    user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList())
                .expirationTime(
                    Date.from(LocalDateTime.now().plusMinutes(5).atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        SignedJWT signedJWT = new SignedJWT(header, jwtClaimsSet);

        signedJWT.sign(jwsSigner);

        return signedJWT.serialize();
    }

    public abstract String getToken(UserDetails user, JWK jwk) throws JOSEException;
}
