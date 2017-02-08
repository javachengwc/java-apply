package com.kafka.service;

import com.util.lang.RunTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * zookeeper client
 */
public class ZookeeperClient {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    private static ZookeeperClient inst =null;

    public static ZookeeperClient getInstance()
    {
        if(inst==null)
        {
            synchronized (ZookeeperClient.class) {
                if(inst==null) {
                    inst = new ZookeeperClient();
                    inst.init("127.0.0.1", 2181, 60000);
                }
            }
        }
        return inst;
    }

    private ZooKeeper zooKeeper;

    private String host;

    private int port;

    private int timeout;

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public void init(String host,int port,int timeout){
        this.host=host;
        this.port=port;
        this.timeout=timeout;
        try {
            zooKeeper = new ZooKeeper(host + ":" + port, timeout, new Watcher() {
                public void process(WatchedEvent event) {

                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        logger.info("ZookeeperClient zookeeper connected..");
                    }
                }
            });

            RunTimeUtil.addShutdownHook(new Runnable() {
                public void run() {
                    ZookeeperClient.getInstance().destory();
                }
            });
        } catch (Exception e) {
            logger.error("ZookeeperClient init error,", e);
            throw new RuntimeException("ZookeeperClient init error");
        }
    }

    public void destory() {
        try {
            zooKeeper.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String getData(String path) throws Exception {
        if(!isExistNode(path))
        {
            return null;
        }
        byte[] data = zooKeeper.getData(path, false, null);
        return new String(data);
    }

    public List<String> getChildren(String path) throws Exception {
        if(!isExistNode(path))
        {
            return null;
        }
        return zooKeeper.getChildren(path, false);
    }

    public List<String> getChildrenByRecursive(String path) throws Exception {
        List<String> children = new ArrayList<String>();
        recursiveChildren(path, children);
        return children;
    }

    public boolean isExistNode(String path) throws Exception {
        Stat exists = zooKeeper.exists(path, false);
        return exists != null;
    }

    public void createNode(String path, String data) throws Exception {
        if (!isExistNode(path)) {
            Transaction transaction = zooKeeper.transaction();
            transaction.create(path, data.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
            transaction.commit();
        }
    }

    //写入数据
    public boolean writeData(String path,String data)
    {
        //写节点数据，首先得节点存在，节点只能一级一级创建
        try {
            Stat stat = zooKeeper.exists(path, false);
            if (stat == null) {
                // 创建
                createNode(path, data);
            }else {
                //更新
                zooKeeper.setData(path, data.getBytes(), -1);
            }
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperClient writeData error,path="+path, e);
        }
        return false;
    }

    private List<String> recursiveChildren(String path, List<String> children) throws Exception {
        List<String> subChildren = zooKeeper.getChildren(path, false);
        for (String child : subChildren) {
            String childPath = StringUtils.equals(path, "/") ? path + child : path + "/" + child;
            children.add(childPath);
            recursiveChildren(childPath, children);
        }
        return children;
    }

}
