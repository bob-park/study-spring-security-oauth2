package com.example.oauth2resourceserververify;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import static com.example.oauth2resourceserververify.MessageDigestUtils.messageDigest;
import static com.example.oauth2resourceserververify.SignatureUtils.signature;

@SpringBootApplication
public class Oauth2ResourceServerVerifyApplication {

    public static void main(String[] args) throws Exception {

//        messageDigest("Spring Security");
        signature("Spring Security");

//		SpringApplication.run(Oauth2ResourceServerVerifyApplication.class, args);

    }

}
