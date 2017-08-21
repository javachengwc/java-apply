package com.component.microservice;

public interface IServer {

    public ServerInfo getServerInfo();

    public ServerInfo register(ServerInfo serverInfo);

    public boolean isAlive();
}
