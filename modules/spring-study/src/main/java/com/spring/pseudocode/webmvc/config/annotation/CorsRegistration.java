package com.spring.pseudocode.webmvc.config.annotation;

import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.Arrays;

public class CorsRegistration
{
    private final String pathPattern;
    private final CorsConfiguration config;

    public CorsRegistration(String pathPattern)
    {
        this.pathPattern = pathPattern;
        this.config = new CorsConfiguration();
        this.config.setAllowedOrigins(Arrays.asList(CrossOrigin.DEFAULT_ORIGINS));
        this.config.setAllowedMethods(Arrays.asList(new String[] { HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name() }));
        this.config.setAllowedHeaders(Arrays.asList(CrossOrigin.DEFAULT_ALLOWED_HEADERS));
        this.config.setAllowCredentials(Boolean.valueOf(true));
        this.config.setMaxAge(Long.valueOf(1800L));
    }

    public CorsRegistration allowedOrigins(String[] origins) {
        this.config.setAllowedOrigins(new ArrayList(Arrays.asList(origins)));
        return this;
    }

    public CorsRegistration allowedMethods(String[] methods) {
        this.config.setAllowedMethods(new ArrayList(Arrays.asList(methods)));
        return this;
    }

    public CorsRegistration allowedHeaders(String[] headers) {
        this.config.setAllowedHeaders(new ArrayList(Arrays.asList(headers)));
        return this;
    }

    public CorsRegistration exposedHeaders(String[] headers) {
        this.config.setExposedHeaders(new ArrayList(Arrays.asList(headers)));
        return this;
    }

    public CorsRegistration maxAge(long maxAge) {
        this.config.setMaxAge(Long.valueOf(maxAge));
        return this;
    }

    public CorsRegistration allowCredentials(boolean allowCredentials) {
        this.config.setAllowCredentials(Boolean.valueOf(allowCredentials));
        return this;
    }

    protected String getPathPattern() {
        return this.pathPattern;
    }

    protected CorsConfiguration getCorsConfiguration() {
        return this.config;
    }
}
