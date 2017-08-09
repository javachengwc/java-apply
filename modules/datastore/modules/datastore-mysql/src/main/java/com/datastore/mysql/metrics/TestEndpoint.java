package com.datastore.mysql.metrics;

import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.stereotype.Component;

//新建的一个测试监控端点
@Component
public class TestEndpoint implements Endpoint<String> {
    public String getId() {
        return "test";
    }
    public boolean isEnabled() {
        return true;
    }
    public boolean isSensitive() {
        return true;
    }
    public String invoke() {
        return "test";
    }
}