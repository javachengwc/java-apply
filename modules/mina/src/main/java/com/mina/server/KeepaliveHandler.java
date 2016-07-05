package com.mina.server;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 心跳处理类
 */
public class KeepaliveHandler extends IoHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(KeepaliveHandler.class);

    @Override
    public void sessionOpened(IoSession session) throws Exception {

    }

    @Override
    public void sessionClosed(IoSession session) throws Exception {

    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        String ip = session.getRemoteAddress().toString();
        logger.info("KeepaliveHandler messageReceived ip=" + ip + ",message=" + message);

    }



}