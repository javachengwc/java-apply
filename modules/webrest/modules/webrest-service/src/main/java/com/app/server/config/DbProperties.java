package com.app.server.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("db")
public class DbProperties {

    private String driverClassName;

    private String username;

    private String password;

    private String url;

    @Value("${db.driverClassName2}")
    private String driverClassName2;

    @Value("${db.username2}")
    private String username2;

    @Value("${db.password2}")
    private String password2;

    @Value("${db.url2}")
    private String url2;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriverClassName2() {
        return driverClassName2;
    }

    public void setDriverClassName2(String driverClassName2) {
        this.driverClassName2 = driverClassName2;
    }

    public String getUsername2() {
        return username2;
    }

    public void setUsername2(String username2) {
        this.username2 = username2;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
