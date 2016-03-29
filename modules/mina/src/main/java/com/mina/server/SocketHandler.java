package com.mina.server;

import com.mina.service.InvokeService;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SocketHandler extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(SocketHandler.class);

    private InvokeService invokeService;

    public SocketHandler() {
    }

    public SocketHandler(InvokeService invokeService) {
        this.invokeService = invokeService;
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {

        RemoteInvokeResult remoteInvokeResult = new RemoteInvokeResult();
        try {
            Object obj = invokeService.invoke(message);
            remoteInvokeResult.setObj(obj);
        } catch (Exception e) {
            logger.error("任务执行失败:", e);

            remoteInvokeResult.setStatus(RemoteInvokeResult.FAIL);
            remoteInvokeResult.setThrowable(e);
        }

        session.write(remoteInvokeResult);
        session.close(true);
    }

    @Override
    public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
        if (session.isConnected()) {
            session.close(true);
        }

        logger.error("there is a error on SocketServer:", cause);
    }
}