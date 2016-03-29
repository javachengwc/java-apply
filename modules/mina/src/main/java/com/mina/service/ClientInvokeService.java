package com.mina.service;

import com.mina.client.SocketSender;

public interface ClientInvokeService {

    public void invokeRemoteMethod(SocketSender socketSender) throws Exception;

    public void invokeRemoteMethod(SocketSender socketSender, String host, int port) throws Exception;
}
