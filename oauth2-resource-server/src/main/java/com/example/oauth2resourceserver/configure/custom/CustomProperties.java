package com.example.oauth2resourceserver.configure.custom;


import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("custom")
public class CustomProperties {

    /**
     * 이것은 그냥 테스트 용이다.
     */
    private boolean enable = true;
    private List<String> strs = List.of("custom");

    private String customStr;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public List<String> getStrs() {
        return strs;
    }

    public void setStrs(List<String> strs) {
        this.strs = strs;
    }

    public String getCustomStr() {
        return customStr;
    }

    public void setCustomStr(String customStr) {
        this.customStr = customStr;
    }
}
