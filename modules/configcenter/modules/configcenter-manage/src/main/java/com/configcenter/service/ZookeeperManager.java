package com.configcenter.service;

import com.configcenter.util.SysConfig;
import com.util.base.NumberUtil;
import com.util.lang.RunTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zk管理器
 */
public class ZookeeperManager {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperManager.class);

    public static String APP_ROOT="/configcenter/app";

    private static ZookeeperManager instance = new ZookeeperManager();

    public static ZookeeperManager getInstance()
    {
        return instance;
    }

    private ZooKeeper zk;
    private boolean connected=false;
    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private long waitConnectTime =5*1000;//等待zookeeper连接成功的时间,毫秒


    private ZookeeperManager()
    {
        init();
        RunTimeUtil.addShutdownHook(new Runnable() {
            public void run() {
                ZookeeperManager.getInstance().close();
            }
        });
    }

    public void init()
    {
        logger.info("ZookeeperManager init start");

        String zkHosts= SysConfig.getValue("zookeeper.hosts");
        String timeoutStr = SysConfig.getValue("zookeeper.sessionTimeout");
        int timeout= NumberUtil.isNumeric(timeoutStr)?Integer.parseInt(timeoutStr):5000;

        if(StringUtils.isBlank(zkHosts))
        {
            logger.error("ZookeeperManager init param zookeeper.hosts is null");
            return;
        }

        try {
            //差不多要执行10秒
            zk = new ZooKeeper(zkHosts, timeout, new Watcher() {
                @Override
                public void process(WatchedEvent event) {
                    if (event.getState() == Event.KeeperState.SyncConnected) {
                        connected=true;
                        countDownLatch.countDown();
                        logger.info("ZookeeperManager zookeeper connected..");
                    }
                }
            });
        } catch (Exception e) {
            logger.error("ZookeeperManager init error,", e);
        }

        logger.info("ZookeeperManager init end");
    }

    //等待zookeeper连接成功，也就zookeeper创建后,在连接成功前需要等待下
    public void waitConnect()
    {
        if(!connected) {
            try{
                countDownLatch.await(waitConnectTime, TimeUnit.MILLISECONDS);
            }catch(Exception e)
            {
                logger.error("ZookeeperManager waitConnect error,",e);
            }
        }
    }

    //路径是否存在
    public boolean isExist(String path)
    {
        waitConnect();
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                return false;
            }
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperManager isExist error,path="+path, e);
        }
        return false;
    }

    //创建路径
    public boolean createPath(String path)
    {
        return createPath(path,true);
    }

    //创建路径
    public boolean createPath(String path,boolean persistent)
    {
        waitConnect();
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                return createNestPath(path,persistent);
            }
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperManager createPath error,path="+path, e);
        }
        return false;
    }

    //嵌套创建层级路径
    private boolean createNestPath(String path,boolean persistent)
    {
        int len=path.length();
        int layerIndex = path.indexOf("/",1);
        String curPath=null;
        boolean rt =true;
        while(layerIndex>0 && layerIndex<(len-1))
        {
            curPath = path.substring(0,layerIndex);
            boolean done =createSimplePath(curPath,persistent);
            if(!done)
            {
                rt=false;
            }
            layerIndex = path.indexOf("/",layerIndex+1);
        }
        if(!StringUtils.isBlank(curPath) && curPath.length()<len)
        {
            boolean done =createSimplePath(path,persistent);
            if(!done)
            {
                rt=false;
            }
        }
        return rt;
    }

    private boolean createSimplePath(String path,boolean persistent)
    {
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE,(persistent ? CreateMode.PERSISTENT : CreateMode.EPHEMERAL));
            }
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperManager createSimplePath error,path="+path, e);
        }
        return false;
    }

    //获取子节点
    public List<String> getChildrenPath(String path)
    {
        waitConnect();
        try {
            Stat stat = zk.exists(path, false);
            if(stat!=null)
            {
                List<String> list = zk.getChildren(path, null);
                return list;
            }
        }catch(Exception e)
        {
            logger.error("ZookeeperManager getChildrenPath error,path="+path, e);
        }
        return null;
    }

    //清除子节点
    public boolean cleanChildren(String path)
    {
        waitConnect();
        try {
            Stat stat = zk.exists(path, false);
            if(stat!=null)
            {
                List<String> list = zk.getChildren(path, null);
                if ( list!=null && list.size()>0) {

                    for(String per:list)
                    {
                        zk.delete(path+"/"+per, -1);
                    }
                }
            }
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperManager cleanChildren error,path="+path, e);
        }
        return false;
    }

    //删除路径
    //如果节点有子节点，直接删除父节点会报错
    public boolean deletePath(String path)
    {
        waitConnect();
        try {
            Stat stat = zk.exists(path, false);
            if(stat!=null)
            {
                return deleteNestPath(path);
            }
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperManager deletePath error,path="+path, e);
        }
        return false;
    }

    //嵌套删除路径
    private boolean deleteNestPath(String path)
    {
        boolean rt =true;
        try {
            List<String> list = zk.getChildren(path, null);
            if ( list!=null && list.size()>0) {

                for(String per:list)
                {
                    boolean done =deleteNestPath(path+"/"+per);
                    if(!done)
                    {
                        rt=false;
                    }
                }
            }
            zk.delete(path, -1);
        }catch(Exception e)
        {
            logger.error("ZookeeperManager deleteNestPath error,path="+path, e);
            rt=false;
        }
        return rt;
    }

    //写入数据
    public boolean writeData(String path,String data)
    {
        return writeData(path,data,true);
    }

    //写入数据
    public boolean writeData(String path, String data,boolean persistent){

        waitConnect();
        //写节点数据，首先得节点存在，节点只能一级一级创建
        try {
            Stat stat = zk.exists(path, false);
            if (stat == null) {
                // 先创建
                createNestPath(path, persistent);
            }
            //更新
            zk.setData(path, data.getBytes(), -1);
            return true;
        }catch(Exception e)
        {
            logger.error("ZookeeperManager writeData error,path="+path, e);
        }
        return false;
    }

    //读取数据
    public String readData(String path) throws Exception{

        waitConnect();
        //直接读不存在的节点数据 zookeeper会报错
        try {
            Stat stat = zk.exists(path, false);
            if (stat != null) {
                String s = new String(zk.getData(path, null, null));
                return s;
            }else
            {
                logger.info("ZookeeperManager readData null ,not this path:"+path);
            }
        }catch(Exception e)
        {
            logger.error("ZookeeperManager readData error,path="+path, e);
            throw new RuntimeException("获取zookeeper中对应数据失败");
        }
        return null;
    }

    //关闭zookeeper连接
    public void close() {
        try {
            if(zk!=null) {
                zk.close();
            }
        } catch (Exception e) {
            logger.error("ZookeeperManager close error,", e);
        }

    }


}
