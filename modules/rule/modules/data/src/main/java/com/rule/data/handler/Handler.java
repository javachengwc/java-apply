package com.rule.data.handler;

import org.jboss.netty.handler.codec.http.HttpResponse;

public interface Handler {
    /**
     * 处理请求事件，返回结果
     * @param content 请求参数
     * @return http返回数据
     */
    public HttpResponse handleReq(String content) throws Throwable;
}
