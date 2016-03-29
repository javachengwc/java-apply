package com.rule.data.exception;


public class ServiceNotFoundException extends RengineException {

    public ServiceNotFoundException(String serviceName) {
        super(serviceName, "数据源未找到");
    }
}
