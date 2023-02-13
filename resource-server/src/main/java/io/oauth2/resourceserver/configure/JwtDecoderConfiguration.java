package io.oauth2.resourceserver.configure;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;

@Configuration
public class JwtDecoderConfiguration {

    @Bean
    @ConditionalOnProperty(prefix = "spring.security.oauth2.resourceserver.jwt", name = "jws-algorithms", havingValue = "HS256")
    public JwtDecoder jwtDecoderBySecretKeyValue(OctetSequenceKey octetSequenceKey,
        OAuth2ResourceServerProperties properties) {
        return NimbusJwtDecoder.withSecretKey(octetSequenceKey.toSecretKey())
            .macAlgorithm(MacAlgorithm.from(properties.getJwt().getJwsAlgorithms().get(0)))
            .build();
    }

    @Bean
    @ConditionalOnProperty(prefix = "spring.security.oauth2.resourceserver.jwt", name = "jws-algorithms", havingValue = "RS256")
    public JwtDecoder jwtDecoderByPublicKeyValue(RSAKey rsaKey, OAuth2ResourceServerProperties properties)
        throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey())
            .signatureAlgorithm(SignatureAlgorithm.from(properties.getJwt().getJwsAlgorithms().get(0)))
            .build();
    }
}
