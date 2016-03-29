package com.mina.service.impl;

import com.mina.client.SocketSender;
import com.mina.service.ClientInvokeService;

public class ClientInvokeServiceImpl implements ClientInvokeService{

    private String host;

    private int port=10000;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void invokeRemoteMethod(SocketSender socketSender) throws Exception {
        socketSender.sendMessage(host, port);
    }

    @Override
    public void invokeRemoteMethod(SocketSender socketSender, String host, int port) throws Exception {
        socketSender.sendMessage(host, port);
    }
}
