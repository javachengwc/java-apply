package com.mina.client;

import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.transport.socket.SocketConnector;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import java.net.InetSocketAddress;

public abstract class SocketSender extends SocketClientHandler {

    private long connectTimeoutMillis = 1000L * 30;

    /**
     * 发送消息
     * @param host
     * @param port
     */
    public void sendMessage(String host, int port) throws Exception {
        SocketConnector socketConnector = new NioSocketConnector();
        DefaultIoFilterChainBuilder defaultIoFilterChainBuilder = socketConnector.getFilterChain();
        defaultIoFilterChainBuilder.addLast("defaultChin", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
        socketConnector.setHandler(this);

        socketConnector.setConnectTimeoutMillis(connectTimeoutMillis);
        ConnectFuture connectFuture = socketConnector.connect(new InetSocketAddress(host, port));
        connectFuture.awaitUninterruptibly();

        IoSession ioSession = connectFuture.getSession();
        ioSession.write(sender());

        ioSession.getCloseFuture().awaitUninterruptibly();
        socketConnector.dispose();
    }

    public abstract InvokeBean sender();
}
