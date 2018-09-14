package com.spring.pseudocode.webmvc.config.annotation;

import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CorsRegistry
{
    private final List<CorsRegistration> registrations = new ArrayList();

    public CorsRegistration addMapping(String pathPattern)
    {
        CorsRegistration registration = new CorsRegistration(pathPattern);
        this.registrations.add(registration);
        return registration;
    }

    protected Map<String, CorsConfiguration> getCorsConfigurations() {
        Map configs = new LinkedHashMap(this.registrations.size());
        for (CorsRegistration registration : this.registrations) {
            configs.put(registration.getPathPattern(), registration.getCorsConfiguration());
        }
        return configs;
    }
}
