package io.oauth2.resourceserver.init;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

import lombok.RequiredArgsConstructor;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import io.oauth2.resourceserver.signature.rsa.RsaPublicKeySecuritySinger;

@RequiredArgsConstructor
@Component
public class RsaKeyExtractor implements ApplicationRunner {

    private final RsaPublicKeySecuritySinger rsaPublicKeySecuritySigner;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        String path = new ClassPathResource("certs").getFile().getAbsolutePath();

        File file = new File(path + File.separatorChar + "publicKey.txt");

        try (FileInputStream is = new FileInputStream(path + File.separatorChar + "apiKey.jks");
            FileWriter writer = new FileWriter(file, true);) {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(is, "123456".toCharArray());
            String alias = "apiKey";
            Key key = keystore.getKey(alias, "123456".toCharArray());

            if (key instanceof PrivateKey) {

                Certificate certificate = keystore.getCertificate(alias);
                PublicKey publicKey = certificate.getPublicKey();
                KeyPair keyPair = new KeyPair(publicKey, (PrivateKey) key);
                rsaPublicKeySecuritySigner.setPrivateKey(keyPair.getPrivate());

                if (file.length() == 0) {
                    String publicStr = java.util.Base64.getMimeEncoder().encodeToString(publicKey.getEncoded());
                    publicStr = "-----BEGIN PUBLIC KEY-----\r\n" + publicStr + "\r\n-----END PUBLIC KEY-----";

                    writer.write(publicStr);
                    writer.flush();
                }

            }
        }
    }

}
