package com.storefront.manage.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListenerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShiroSessionListener extends SessionListenerAdapter{

    private static final Logger logger = LoggerFactory.getLogger(ShiroSessionListener.class);

    @Override
    public void onStart(Session session) {
        logger.info("ShiroSessionListener onStart, sessionId={}", session.getId());
    }

    @Override
    public void onStop(Session session) {
        logger.info("ShiroSessionListener onStop, sessionId={}", session.getId());
    }


}
