package com.rocketmq.service;

import com.rocketmq.model.req.MessageReq;

public interface MessageService {

    public boolean pushMsg(MessageReq msgReq);
}
