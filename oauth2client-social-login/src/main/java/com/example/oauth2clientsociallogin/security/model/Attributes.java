package com.example.oauth2clientsociallogin.security.model;

import java.util.Map;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Attributes {

    private final Map<String, Object> mainAttributes;
    private final Map<String, Object> subAttributes;
    private final Map<String, Object> otherAttributes;

    @Builder
    private Attributes(Map<String, Object> mainAttributes, Map<String, Object> subAttributes,
        Map<String, Object> otherAttributes) {
        this.mainAttributes = mainAttributes;
        this.subAttributes = subAttributes;
        this.otherAttributes = otherAttributes;
    }
}
