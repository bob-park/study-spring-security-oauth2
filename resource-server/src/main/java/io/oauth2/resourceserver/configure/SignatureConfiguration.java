package io.oauth2.resourceserver.configure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.gen.OctetSequenceKeyGenerator;
import com.nimbusds.jose.jwk.gen.RSAKeyGenerator;
import io.oauth2.resourceserver.signature.mac.MacSecuritySigner;
import io.oauth2.resourceserver.signature.rsa.RsaPublicKeySecuritySinger;
import io.oauth2.resourceserver.signature.rsa.RsaSecuritySinger;

@Configuration
public class SignatureConfiguration {

    @Bean
    public MacSecuritySigner macSecuritySigner() {
        return new MacSecuritySigner();
    }

    @Bean
    public OctetSequenceKey octetSequenceKey() throws JOSEException {
        return new OctetSequenceKeyGenerator(256)
            .keyID("macKey")
            .algorithm(JWSAlgorithm.HS256)
            .generate();
    }

    //    @Primary
    @Bean
    public RsaSecuritySinger rsaSecuritySinger() {
        return new RsaSecuritySinger();
    }


    @Primary
    @Bean
    public RSAKey rsaKey() throws JOSEException {
        return new RSAKeyGenerator(2_048)
            .keyID("rsaKey")
            .algorithm(JWSAlgorithm.RS512)
            .generate();
    }

    @Primary
    @Bean
    public RsaPublicKeySecuritySinger rsaPublicKeySecuritySinger() {
        return new RsaPublicKeySecuritySinger();
    }

}
