package io.oauth2.resourceserver.signature.rsa;

import java.security.PrivateKey;

import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.jwk.JWK;
import io.oauth2.resourceserver.signature.SecuritySigner;

public class RsaPublicKeySecuritySinger extends SecuritySigner {

    private PrivateKey privateKey;


    @Override
    public String getToken(UserDetails user, JWK jwk) throws JOSEException {
        RSASSASigner jwsSigner = new RSASSASigner(privateKey);
        return super.getJwtTokenInternal(jwsSigner, user, jwk);
    }

    public void setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
    }
}
