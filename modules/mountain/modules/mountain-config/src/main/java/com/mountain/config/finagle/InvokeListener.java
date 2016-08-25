package com.mountain.config.finagle;

import com.twitter.util.FutureEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 调用结果监听
 * 可以用来统计调用计数
 */
public class InvokeListener implements FutureEventListener {

    private static Logger logger = LoggerFactory.getLogger(InvokeListener.class);

    private String host;

    private String consumerId;

    private String serviceName;

    private String api;

    private String serviceUrl;

    private String version;

    private String className;

    private String methodName;

    public InvokeListener(String host,String consumerId, String serviceName, String serviceUrl,String api,
                          String version,String className,String methodName) {
        this.host = host;
        this.consumerId=consumerId;
        this.serviceName = serviceName;
        this.serviceUrl = serviceUrl;
        this.api = api;
        this.version = version;
        this.className = className;
        this.methodName = methodName;
    }
    @Override
    public void onSuccess(Object value) {

    }

    @Override
    public void onFailure(Throwable cause) {

    }

}