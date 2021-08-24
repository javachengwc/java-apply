package com.dubbo.pseudocode.remoting.transport.mina;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.remoting.*;
import org.apache.dubbo.remoting.transport.mina.MinaClient;
import org.apache.dubbo.remoting.transport.mina.MinaServer;

public class MinaTransporter implements Transporter {

    public static final String NAME = "mina";

    @Override
    public Server bind(URL url, ChannelHandler handler) throws RemotingException {
        return new MinaServer(url, handler);
    }

    @Override
    public Client connect(URL url, ChannelHandler handler) throws RemotingException {
        return new MinaClient(url, handler);
    }

}