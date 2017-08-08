package com.datastore.mongodb.server;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MongoDbProperties {

    @Value("${mongo.username}")
    private String userName;

    @Value("${mongo.password}")
    private String password;

    @Value("${mongo.ip}")
    private String ip;

    @Value("${mongo.port}")
    private int port;

    @Value("${mongo.database}")
    private String database;

    @Value("${mongo.collection}")
    private String collection;

    @Value("${mongo.maxWaitTime}")
    private Integer maxWaitTime;

    @Value("${mongo.connectTimeout}")
    private Integer connectTimeout;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public Integer getMaxWaitTime() {
        return maxWaitTime;
    }

    public void setMaxWaitTime(Integer maxWaitTime) {
        this.maxWaitTime = maxWaitTime;
    }

    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
