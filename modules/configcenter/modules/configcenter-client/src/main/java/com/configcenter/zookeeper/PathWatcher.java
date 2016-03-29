package com.configcenter.zookeeper;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 节点变更的watcher
 */
public class PathWatcher  implements Watcher {

    private static Logger logger = LoggerFactory.getLogger(PathWatcher.class);

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

    public PathWatcher()
    {

    }

    public PathWatcher(String path)
    {
        this.path=path;
    }

    public PathWatcher(String path,boolean continueWatch)
    {
        this.path=path;
        this.continueWatch=continueWatch;
    }

    public PathWatcher(String path,ZooKeeper zookeeper,boolean continueWatch)
    {
        this.path=path;
        this.zookeeper=zookeeper;
        this.continueWatch=continueWatch;
    }

    @Override
    public void process(WatchedEvent watchedEvent) {

        if ( Event.EventType.NodeChildrenChanged == watchedEvent.getType() ) {

            List<String> children=null;
            try {
                if (zookeeper == null) {
                    throw new RuntimeException("zookeeper is null");
                }
                Stat stat = zookeeper.exists(path, false);
                if (stat != null) {
                    if (continueWatch) {
                        //重新监听,监听节点的变化只能监听到当前节点的子节点变化，而不能嵌套监听到子节点的子节点
                        children = zookeeper.getChildren(path, this);
                    }
                    else {
                        children = zookeeper.getChildren(path, null);
                    }
                }
            } catch (KeeperException.NoNodeException ex)
            {
                //先判断节点是否存在，在stat有值也就是存在的情况下再获取节点的子节点，同样可能报节点不存在的异常
                //因为exists, getChildren是非原子操作的
                logger.error("PathWatcher path getChildren error,path="+path+" is not node");
                //不中断
            }
            catch(Exception e)
            {
                logger.error("PathWatcher getChildren error,path="+path,e);
                return;
            }

            //数据变更
            handleChildrenChange(path,children);
        }
        if(Event.EventType.NodeDeleted==watchedEvent.getType())
        {
            handlePathDelete(path);
        }

    }

    public void handleChildrenChange(String path,List<String> children)
    {

    }

    public void handlePathDelete(String path)
    {

    }
}
