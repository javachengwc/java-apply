package com.mina.client;


import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.service.TransportMetadata;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.transport.socket.SocketSessionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class SocketClientHandler extends IoHandlerAdapter {
    private Logger logger = LoggerFactory.getLogger(SocketClientHandler.class);
    private final static String METADATA_NAME = "socket";
    private final static int RECEIVE_BUFFER_SIZE = 1024;

    @Override
    public void sessionCreated(IoSession ioSession) throws Exception {
        TransportMetadata transportMetadata = ioSession.getTransportMetadata();
        if (transportMetadata != null && transportMetadata.getName() != null && !"".equals(transportMetadata.getName().trim())) {
            if (METADATA_NAME.equalsIgnoreCase(transportMetadata.getName().trim())) {
                SocketSessionConfig socketSessionConfig = (SocketSessionConfig) ioSession.getConfig();
                socketSessionConfig.setReceiveBufferSize(RECEIVE_BUFFER_SIZE);
            }
        }
    }

    @Override
    public void messageReceived(IoSession session, Object message) throws Exception {
        received(message);
        session.close(true);
    }

    @Override
    public void exceptionCaught(IoSession ioSession, Throwable cause) throws Exception {
        if (ioSession.isConnected()) {
            ioSession.close(true);
        }
        logger.error("there is error at socketclient:", cause);
    }

    public abstract void received(Object message) throws Exception;
}
