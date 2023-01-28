package com.example.oauth2resourceserver.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class WebLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws Exception {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);

        String requestData = logging(requestWrapper.getInputStream());

        log.debug("request={}", requestData);

        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {

        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        responseWrapper.copyBodyToResponse();

        String responseData = logging(responseWrapper.getContentInputStream());

        log.debug("response={}", responseData);

        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }


    private String logging(InputStream is) throws IOException {
        return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
    }
}
