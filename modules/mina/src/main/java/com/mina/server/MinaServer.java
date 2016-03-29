package com.mina.server;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.mina.service.InvokeService;
import com.mina.service.impl.InvokeServiceImpl;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.executor.ExecutorFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;

public class MinaServer {

    private static Logger logger = LoggerFactory.getLogger(MinaServer.class);

    private final static int BUFFER_SIZE = 1024;

    private final static int IDLE_TIME = 10;

    private int port = 10000;

    private MinaServer()
    {

    }

    public void start()
    {
        try {

            IoAcceptor ioAcceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors() + 1);
            ioAcceptor.getFilterChain().addLast("logger", new LoggingFilter());
            ioAcceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
            ioAcceptor.getFilterChain().addLast("threadPool", new ExecutorFilter(Executors.newCachedThreadPool()));

            InvokeService invokeService = new InvokeServiceImpl();

            ioAcceptor.setHandler(new SocketHandler(invokeService));
            ioAcceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, IDLE_TIME);
            ioAcceptor.getSessionConfig().setReadBufferSize(BUFFER_SIZE);
            ioAcceptor.bind(new InetSocketAddress(port));
        } catch (Exception e) {
            logger.error("启动SOCKET服务失败:", e);
        }
    }

    public static void main(String args [])
    {
         new MinaServer().start();
    }

}
