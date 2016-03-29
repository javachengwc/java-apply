package com.configcenter.zookeeper;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据变更的watcher
 */
public class DataWatcher implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(DataWatcher.class);

    public ZooKeeper zookeeper;

    private String path;

    private boolean continueWatch=false;

    public ZooKeeper getZookeeper() {
        return zookeeper;
    }

    public void setZookeeper(ZooKeeper zookeeper) {
        this.zookeeper = zookeeper;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isContinueWatch() {
        return continueWatch;
    }

    public void setContinueWatch(boolean continueWatch) {
        this.continueWatch = continueWatch;
    }

    public DataWatcher()
    {

    }

    public DataWatcher(String path)
    {
        this.path=path;
    }

    public DataWatcher(String path,boolean continueWatch)
    {
        this.path=path;
        this.continueWatch=continueWatch;
    }

    public DataWatcher(String path,ZooKeeper zookeeper,boolean continueWatch)
    {
        this.path=path;
        this.zookeeper=zookeeper;
        this.continueWatch=continueWatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        if ( Event.EventType.NodeDataChanged == watchedEvent.getType() ) {

            String newValue =null;
            try {
                if(zookeeper ==null)
                {
                    throw new RuntimeException("zookeeper is null");
                }
                if (continueWatch) {
                    //重新监听
                    newValue = new String(zookeeper.getData(path, this, null));
                }else
                {
                    newValue =new String(zookeeper.getData(path, false, null));
                }
            }catch(Exception e)
            {
                logger.error("DataWatcher getData error,path="+path,e);
                return;
            }

            //数据变更
            handleDataChange(path,newValue);
        }
        if(Event.EventType.NodeDeleted==watchedEvent.getType())
        {
            handlePathDelete(path);
        }
    }

    public void handleDataChange(String path,String newValue)
    {

    }

    public void handlePathDelete(String path)
    {

    }
}
