package com.pseudocode.cloud.openfeign.core.support;

import java.util.concurrent.TimeUnit;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "feign.httpclient")
public class FeignHttpClientProperties {
    public static final boolean DEFAULT_DISABLE_SSL_VALIDATION = false;

    public static final int DEFAULT_MAX_CONNECTIONS = 200;

    public static final int DEFAULT_MAX_CONNECTIONS_PER_ROUTE = 50;

    public static final long DEFAULT_TIME_TO_LIVE = 900L;

    public static final TimeUnit DEFAULT_TIME_TO_LIVE_UNIT = TimeUnit.SECONDS;
    public static final boolean DEFAULT_FOLLOW_REDIRECTS = true;

    public static final int DEFAULT_CONNECTION_TIMEOUT = 2000;

    public static final int DEFAULT_CONNECTION_TIMER_REPEAT = 3000;

    private boolean disableSslValidation = DEFAULT_DISABLE_SSL_VALIDATION;
    //最大连接数
    private int maxConnections = DEFAULT_MAX_CONNECTIONS;
    //每个路由连接数
    private int maxConnectionsPerRoute = DEFAULT_MAX_CONNECTIONS_PER_ROUTE;

    private long timeToLive = DEFAULT_TIME_TO_LIVE;
    private TimeUnit timeToLiveUnit = DEFAULT_TIME_TO_LIVE_UNIT;
    private boolean followRedirects = DEFAULT_FOLLOW_REDIRECTS;
    //连接超时时间
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int connectionTimerRepeat = DEFAULT_CONNECTION_TIMER_REPEAT;

    public int getConnectionTimerRepeat() {
        return connectionTimerRepeat;
    }

    public void setConnectionTimerRepeat(int connectionTimerRepeat) {
        this.connectionTimerRepeat = connectionTimerRepeat;
    }

    public boolean isDisableSslValidation() {
        return disableSslValidation;
    }

    public void setDisableSslValidation(boolean disableSslValidation) {
        this.disableSslValidation = disableSslValidation;
    }

    public int getMaxConnections() {
        return maxConnections;
    }

    public void setMaxConnections(int maxConnections) {
        this.maxConnections = maxConnections;
    }

    public int getMaxConnectionsPerRoute() {
        return maxConnectionsPerRoute;
    }

    public void setMaxConnectionsPerRoute(int maxConnectionsPerRoute) {
        this.maxConnectionsPerRoute = maxConnectionsPerRoute;
    }

    public long getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(long timeToLive) {
        this.timeToLive = timeToLive;
    }

    public TimeUnit getTimeToLiveUnit() {
        return timeToLiveUnit;
    }

    public void setTimeToLiveUnit(TimeUnit timeToLiveUnit) {
        this.timeToLiveUnit = timeToLiveUnit;
    }

    public boolean isFollowRedirects() {
        return followRedirects;
    }

    public void setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }
}
