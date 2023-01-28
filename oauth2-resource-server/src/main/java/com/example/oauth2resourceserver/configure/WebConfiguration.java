package com.example.oauth2resourceserver.configure;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.oauth2resourceserver.filter.WebLoggingFilter;

@Configuration
@EnableWebMvc
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    public FilterRegistrationBean<WebLoggingFilter> filter() {
        FilterRegistrationBean<WebLoggingFilter> filter = new FilterRegistrationBean<>();

        filter.addUrlPatterns("*");
        filter.setFilter(new WebLoggingFilter());
        filter.setOrder(Ordered.HIGHEST_PRECEDENCE);

        return filter;
    }

}
