package com.pseudocode.cloud.zuul.filters;


import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class Route {

    private String id;

    private String fullPath;

    private String path;

    private String location;

    private String prefix;

    private Boolean retryable;

    private Set<String> sensitiveHeaders = new LinkedHashSet<String>();

    private boolean customSensitiveHeaders;

    private boolean prefixStripped = true;

    public Route(String id, String path, String location, String prefix,
                 Boolean retryable, Set<String> ignoredHeaders) {
        this.id = id;
        this.prefix = StringUtils.hasText(prefix) ? prefix : "";
        this.path = path;
        this.fullPath = prefix + path;
        this.location = location;
        this.retryable = retryable;
        this.sensitiveHeaders = new LinkedHashSet<>();
        if (ignoredHeaders != null) {
            this.customSensitiveHeaders = true;
            for (String header : ignoredHeaders) {
                this.sensitiveHeaders.add(header.toLowerCase());
            }
        }
    }

    public Route(String id, String path, String location, String prefix,
                 Boolean retryable, Set<String> ignoredHeaders, boolean prefixStripped) {
        this(id, path, location, prefix, retryable, ignoredHeaders);
        this.prefixStripped = prefixStripped;
    }


    public boolean isCustomSensitiveHeaders() {
        return this.customSensitiveHeaders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public Boolean getRetryable() {
        return retryable;
    }

    public void setRetryable(Boolean retryable) {
        this.retryable = retryable;
    }

    public Set<String> getSensitiveHeaders() {
        return sensitiveHeaders;
    }

    public void setSensitiveHeaders(Set<String> sensitiveHeaders) {
        this.sensitiveHeaders = sensitiveHeaders;
    }

    public void setCustomSensitiveHeaders(boolean customSensitiveHeaders) {
        this.customSensitiveHeaders = customSensitiveHeaders;
    }

    public boolean isPrefixStripped() {
        return prefixStripped;
    }

    public void setPrefixStripped(boolean prefixStripped) {
        this.prefixStripped = prefixStripped;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route that = (Route) o;
        return customSensitiveHeaders == that.customSensitiveHeaders &&
                prefixStripped == that.prefixStripped &&
                Objects.equals(id, that.id) &&
                Objects.equals(fullPath, that.fullPath) &&
                Objects.equals(path, that.path) &&
                Objects.equals(location, that.location) &&
                Objects.equals(prefix, that.prefix) &&
                Objects.equals(retryable, that.retryable) &&
                Objects.equals(sensitiveHeaders, that.sensitiveHeaders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullPath, path, location, prefix, retryable,
                sensitiveHeaders, customSensitiveHeaders, prefixStripped);
    }

}
