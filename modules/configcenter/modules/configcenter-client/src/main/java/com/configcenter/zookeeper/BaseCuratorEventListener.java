package com.configcenter.zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CuratorFramework客户端监听器
 * CuratorFramework.getCuratorListenable().addListener(new NodeEventListener());
 * 只能监听到客户端的连接 与端开连接，不能监听到节点的变化
 */
public class BaseCuratorEventListener implements CuratorListener{

    private static Logger logger = LoggerFactory.getLogger(BaseCuratorEventListener.class);

    public BaseCuratorEventListener() {
        super();
    }

    @Override
    public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {

        final WatchedEvent watchedEvent = event.getWatchedEvent();
        if (watchedEvent != null) {
            logger.info("------------CuratorEventListener watched event:" + watchedEvent);

            if (watchedEvent.getState() == Watcher.Event.KeeperState.SyncConnected) {

                //启动后zookeeper连接或则断后的重连接，会在此监听到
                logger.info("--------------CuratorEventListener connected");
            }

            if(watchedEvent.getState()==Watcher.Event.KeeperState.Disconnected)
            {
                //zookeeper断开，会在此监听到
                logger.info("--------------CuratorEventListener disconnected");
            }

            if(watchedEvent.getState()==Watcher.Event.KeeperState.Expired)
            {
                //session过期，之前的watch将都失效, 监听不到
                logger.info("--------------CuratorEventListener expired");
            }

            if(watchedEvent.getState()==Watcher.Event.KeeperState.SaslAuthenticated)
            {
                logger.info("--------------CuratorEventListener saslAuthenticated");
            }
        }
    }
}
