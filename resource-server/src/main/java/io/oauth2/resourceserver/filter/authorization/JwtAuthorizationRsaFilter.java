package io.oauth2.resourceserver.filter.authorization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.RSAKey;

public class JwtAuthorizationRsaFilter extends JwtAuthorizationFilter {

    public JwtAuthorizationRsaFilter(RSAKey rsaKey) throws JOSEException {
        super(new RSASSAVerifier(rsaKey.toRSAPublicKey()));
    }
}
