package com.datastore.mysql.server;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DbProperties {

    @Value("${database.driverClassName}")
    private String driverClassName;

    @Value("${database.username}")
    private String userName;

    @Value("${database.password}")
    private String pwd;

    @Value("${database.url}")
    private String url;

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
