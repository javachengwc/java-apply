package com.rocketmq.service.impl;

import com.rocketmq.service.MessageService;
import org.springframework.stereotype.Service;

@Service
public class MessageServiceImpl implements MessageService {

    public boolean pushMsg(String msg,Integer type) {
        return true;
    }
}
