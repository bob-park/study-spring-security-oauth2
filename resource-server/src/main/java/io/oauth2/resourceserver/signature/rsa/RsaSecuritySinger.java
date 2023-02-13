package io.oauth2.resourceserver.signature.rsa;

import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.RSAKey;
import io.oauth2.resourceserver.signature.SecuritySigner;

public class RsaSecuritySinger extends SecuritySigner {

    @Override
    public String getToken(UserDetails user, JWK jwk) throws JOSEException {

        RSASSASigner jwsSigner = new RSASSASigner(((RSAKey) jwk).toPrivateKey());
        return super.getJwtTokenInternal(jwsSigner, user, jwk);
    }
}
