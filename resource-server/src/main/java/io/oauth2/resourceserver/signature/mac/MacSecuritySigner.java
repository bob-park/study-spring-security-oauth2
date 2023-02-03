package io.oauth2.resourceserver.signature.mac;

import org.springframework.security.core.userdetails.UserDetails;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import io.oauth2.resourceserver.signature.SecuritySigner;

public class MacSecuritySigner extends SecuritySigner {

    @Override
    public String getToken(UserDetails user, JWK jwk) throws JOSEException {

        MACSigner jwsSigner = new MACSigner(((OctetSequenceKey) jwk).toSecretKey());
        return super.getJwtTokenInternal(jwsSigner, user, jwk);
    }
}
