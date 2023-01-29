package com.example.oauth2resourceserver.configure.custom;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@ConfigurationPropertiesScan("com.example.oauth2resourceserver.configure.custom")
@Configuration
public class CustomConfiguration {

    private final CustomProperties properties;

    @Bean
    @Conditional(CustomKeyValueCondition.class)
    public Custom custom() {
        return new Custom(properties.getCustomStr());
    }

    @ToString
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Custom {

        private String customStr;
    }

}
