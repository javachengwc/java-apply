package com.es.consumer.access.service;

import com.es.consumer.base.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AccessService implements MessageService {

    public static Logger logger= LoggerFactory.getLogger(AccessService.class);

    @Override
    public void handleMessage(String message) {

    }
}
