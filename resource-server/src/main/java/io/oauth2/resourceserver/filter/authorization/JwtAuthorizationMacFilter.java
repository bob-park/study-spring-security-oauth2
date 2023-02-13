package io.oauth2.resourceserver.filter.authorization;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;

public class JwtAuthorizationMacFilter extends JwtAuthorizationFilter {


    public JwtAuthorizationMacFilter(OctetSequenceKey octetSequenceKey) throws JOSEException {
        super(new MACVerifier(octetSequenceKey.toSecretKey()));
    }
}
