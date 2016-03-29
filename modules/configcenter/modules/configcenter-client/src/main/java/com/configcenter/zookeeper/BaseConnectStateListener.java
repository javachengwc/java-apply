package com.configcenter.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CuratorFramework客户端监听器
 * CuratorFramework.getConnectionStateListenable().addListener(new NodeEventListener());
 * 能监听到客户端的连接状态的细节变化
 */
public class BaseConnectStateListener implements ConnectionStateListener {

    private static Logger logger = LoggerFactory.getLogger(BaseConnectStateListener.class);

    private boolean losted=false;

    private boolean connected=false;

    private ZookeeperClient zookeeperClient;

    public boolean isLosted() {
        return losted;
    }

    public boolean isConnected() {
        return connected;
    }

    public BaseConnectStateListener()
    {
        super();
    }

    public BaseConnectStateListener(ZookeeperClient zookeeperClient)
    {
        super();
        this.zookeeperClient=zookeeperClient;
    }

    @Override
    public void stateChanged(CuratorFramework client, ConnectionState connectionState){

        logger.info("====================BaseConnectStateListener stateChanged connectionState:"+connectionState);

        if(connectionState== ConnectionState.LOST)
        {
            //zookeeper的会话丢失会在此监听到,session过期，之前的watch将都失效
            logger.info("====================BaseConnectStateListener connection lost");
            losted=true;
            handleConnectLost();
        }

        if(connectionState== ConnectionState.CONNECTED)
        {
            //启动后zookeeper就连接会在此监听到
            logger.info("====================BaseConnectStateListener connection connected");
            connected=true;
            handleConnected();
            zookeeperClient.setDataInit(true);
        }


        if(connectionState== ConnectionState.SUSPENDED)
        {
            //zookeeper中途断开，会在此监听到 suspended暂停的
            logger.info("====================BaseConnectStateListener connection suspended");
        }


        if(connectionState== ConnectionState.RECONNECTED)
        {
            //zookeeper断后的重连接，会在此监听到
            logger.info("====================BaseConnectStateListener connection reconnected");
            handleReconnected();
        }

    }

    public void handleConnectLost()
    {

    }

    public void handleConnected()
    {

    }

    public void handleReconnected()
    {
        if(losted)
        {
            handleNewSession();
        }
        losted=false;
    }

    public void handleNewSession()
    {

    }
}
