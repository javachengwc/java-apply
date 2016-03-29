package com.configcenter.zookeeper;

import com.util.ThreadUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * zookeeper客户端
 */
public class ZookeeperClient {

    protected static final Logger logger = LoggerFactory.getLogger(ZookeeperClient.class);

    public static String DEF_ZK_HOSTS="127.0.0.1:2181";

    public static long DEF_CONNECT_TIMEOUT=3000l;

    private CuratorFramework client =null;

    //数据是否初始化
    protected boolean dataInit=false;

    protected String zkHosts=null;

    protected Long connectTimeout=null;

    public String getZkHosts() {
        return zkHosts;
    }

    public void setZkHosts(String zkHosts) {
        this.zkHosts = zkHosts;
    }

    public Long getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(Long connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public boolean isDataInit() {
        return dataInit;
    }

    public void setDataInit(boolean dataInit) {
        this.dataInit = dataInit;
    }

    public void init(final BaseConnectStateListener listener)
    {
        logger.info("ZookeeperClient init start ");
        String zkhost= StringUtils.isBlank(zkHosts)?DEF_ZK_HOSTS:zkHosts;

        final long conTimeout = (connectTimeout==null)?DEF_CONNECT_TIMEOUT:connectTimeout;

        RetryPolicy rp=new ExponentialBackoffRetry(2000,1);//重试机制
        //重试机制会在到时把之前的会话丢失ConnectionState.LOST，然后再重建会话，会导致之前的会话的watch失效

        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost)
                .connectionTimeoutMs(new Long(conTimeout).intValue())
                .sessionTimeoutMs(3000)
                .retryPolicy(rp);
        client = builder.build();

        client.start();
        client.getCuratorListenable().addListener(new BaseCuratorEventListener());
        client.getConnectionStateListenable().addListener(listener);
        client.getUnhandledErrorListenable().addListener(new BaseErrorListener());

        //连接超时处理
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                try{
                    if(!listener.isConnected()) {
                        logger.info("ZookeeperClient after " + conTimeout + " ms connect timeout,do handleConnectTimeout");
                        handleConnectTimeout();
                    }else
                    {
                        logger.info("ZookeeperClient after " + conTimeout + " ms connect,zookeeper has connected ,do noting");
                    }
                }catch(Exception e)
                {
                    logger.error("ZookeeperClient handleConnectTimeout error,",e);
                }finally {

                    if(!dataInit) {
                        dataInit = true;
                    }
                }
            }
        },conTimeout);

        //程序关闭钩子
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                //程序关闭时, 回收所有资源
                logger.info("ZookeeperClient shutdown hook now.");
                ZookeeperClient.this.close();
            }
        }, "ZookeeperClientShutdownHook"));

        //阻塞等待数据加载完后再结束此方法，因为做为客户端，需要先加载一片数据，让其他依赖能获取到数据
        //如果没加载数据 就让其他依赖调用获取配置的话，会获取到空数据，导致应用启动时因拿不到配置数据而报错
        while(true)
        {
            logger.info("ZookeeperClient 等待数据第一次加载...............");
            ThreadUtil.sleep(1000l);
            if(dataInit)
            {
                break;
            }
        }
        logger.info("ZookeeperClient init end ");
    }

    public void handleConnectTimeout()
    {
    }

    //节点是否存在
    public boolean isExist(String path) {

        try{
            if(client.checkExists().forPath(path)==null){
                return false;
            }else{
                return true;
            }
        }catch(Exception e)
        {
            logger.error("ZookeeperClient isExist error,path="+path,e);
        }
        return false;
    }

    //获取子节点
    public List<String> getChildren(String path)
    {
        try{
            if(client.checkExists().forPath(path)==null)
            {
                logger.info("ZookeeperClient getChildren null ,not this path:"+path);
                return null;
            }
            List<String> list = client.getChildren().forPath(path);
            return list;

        }catch(Exception e)
        {
            logger.error("ZookeeperClient getChildren error,path="+path,e);
        }
        return null;
    }

    //读取数据
    public String readData(String path) throws Exception{

        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
                String s = new String(client.getData().forPath(path));
                return s;
            }else
            {
                logger.info("ZookeeperClient readData null ,not this path:"+path);
            }
        }catch(Exception e)
        {
            logger.error("ZookeeperClient readData error,path="+path, e);
            throw new RuntimeException("获取zookeeper中对应数据失败,path="+path);
        }
        return null;
    }

    //读数据并监听数据变化
    public String readDataWithWatch(String path,Watcher watcher) throws Exception
    {
        try {
            Stat stat = client.checkExists().forPath(path);
            if (stat != null) {
                String s = new String(client.getData().usingWatcher(watcher).forPath(path));
                return s;
            }else
            {
                logger.info("ZookeeperClient readData null ,not this path:"+path);
            }
        }catch(Exception e)
        {
            logger.error("ZookeeperClient readDataWithWatch error,path="+path, e);
            throw new RuntimeException("获取zookeeper中对应数据失败,path="+path);
        }
        return null;
    }

    //监听节点数据
    public void dataWatch(String path ,DataWatcher watcher)
    {
        try {
            client.getData().usingWatcher(watcher).forPath(path);
        }catch(Exception e)
        {
            logger.error("ZookeeperClient dataWatch error,path="+path, e);
        }
    }

    //监听节点
    public void pathWatch(String path,PathWatcher watcher)
    {
        try {
            client.getChildren().usingWatcher(watcher).forPath(path);
        }catch(Exception e)
        {
            logger.error("ZookeeperClient pathWatch error,path="+path, e);
        }
    }

    public ZooKeeper getZooKeeper()
    {
        if(client==null)
        {
            return null;
        }
        try {
            ZooKeeper zk = client.getZookeeperClient().getZooKeeper();
            return zk;
        }catch(Exception e)
        {
            logger.error("ZookeeperClient getZooKeeper error,",e);
        }
        return null;
    }

    public CuratorFramework getClient()
    {
        return client;
    }

    public void close()
    {
        if(client!=null)
        {
            CloseableUtils.closeQuietly(client);
        }
    }

}

