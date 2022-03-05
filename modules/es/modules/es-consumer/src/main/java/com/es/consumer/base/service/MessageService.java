package com.es.consumer.base.service;

/**
 * 消息服务
 */
public interface MessageService {

    /**
     * 处理消息
     */
    public void handleMessage(String message);
}
