package com.example.oauth2resourceserver.filter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Enumeration;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

@Slf4j
public class WebLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        filterChain.doFilter(requestWrapper, responseWrapper);

        if (log.isDebugEnabled()) {
            logRequest(requestWrapper);
            logResponse(responseWrapper);
        }

        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(HttpServletRequest request) throws IOException {

        StringBuilder builder = new StringBuilder("\n");

        builder.append(request.getMethod()).append(" ").append(request.getRequestURI()).append(" HTTP/1.1")
            .append("\n");

        for (Enumeration<String> headerNames = request.getHeaderNames(); headerNames.hasMoreElements(); ) {
            String headerName = headerNames.nextElement();
            builder.append(headerName).append(": ");

            for (Enumeration<String> values = request.getHeaders(headerName); values.hasMoreElements(); ) {
                String value = values.nextElement();

                builder.append(value).append(" ");
            }

            builder.append("\n");

        }

        builder.append("\n")
            .append(getBody(request.getInputStream()));

        log.debug("request={}", builder);
    }

    private void logResponse(ContentCachingResponseWrapper response) throws IOException {
        StringBuilder builder = new StringBuilder("\n");

        HttpStatus status = HttpStatus.valueOf(response.getStatus());

        builder.append("HTTP/1.1 ").append(status).append("\n");

        Collection<String> headerNames = response.getHeaderNames();

        for (String headerName : headerNames) {
            builder.append(headerName).append(": ").append(response.getHeader(headerName)).append("\n");
        }

        builder.append("\n")
            .append(getBody(response.getContentInputStream()))
            .append("\n");

        log.debug("response={}", builder);
    }

    private String getBody(InputStream is) throws IOException {
        return StreamUtils.copyToString(is, StandardCharsets.UTF_8);
    }
}
