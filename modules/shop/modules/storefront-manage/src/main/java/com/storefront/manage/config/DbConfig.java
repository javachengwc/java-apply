package com.storefront.manage.config;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spring.datasource")
public class DbConfig {

    //最大连接数
    private int maxActive;

    //最小连接数
    private int minIdle;

    //获取连接时最大等待时间
    private int maxWait;

    //查询超时时间
    private int queryTimeout;

    //门店
    private Source storefront;

    public static class Source {
        private String driverClassName;
        private String url;
        private String username;
        private String password;

        public Source() {
        }

        public String getDriverClassName() {
            return driverClassName;
        }

        public void setDriverClassName(String driverClassName) {
            this.driverClassName = driverClassName;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
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
    }

    public int getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public int getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(int minIdle) {
        this.minIdle = minIdle;
    }

    public int getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }

    public Source getStorefront() {
        return storefront;
    }

    public void setStorefront(Source storefront) {
        this.storefront = storefront;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
