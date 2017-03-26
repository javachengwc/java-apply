package com.zookeeper.demo;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 分布式锁
 */
public class DistributedLock implements Watcher {

    private static final Logger logger = LoggerFactory.getLogger(DistributedLock.class);

    private static String Host = "127.0.0.1:2181";
    private static int TimeOut = 5000;
    private static String ParentPath = "/lock";
    private static String SubPath = "/lock/sub";
    private static int ThreadCnt=100;//
    private static CountDownLatch cd = new CountDownLatch(ThreadCnt);

    private int threadId;
    private ZooKeeper zk = null;
    private String selfPath;
    private String watchPath;

    public DistributedLock(int threadId) {
        this.threadId = threadId;
        try{
            createConnection(Host, TimeOut);
        }catch (Exception e)
        {
        }
    }

    public static void main(String[] args) {
        for(int i=0; i < ThreadCnt; i++){
            final int threadId = i+1;
            new Thread(){
                @Override
                public void run() {
                    try{
                        DistributedLock dc = new DistributedLock(threadId);
                        dc.initParentNode();//创建父节点
                        dc.getLock();
                    } catch (Exception e){
                        logger.error("线程"+threadId+"执行异常,",e);
                    }
                }
            }.start();
        }
        try {
            cd.await();
            logger.info("全部线程运行完...........");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //创建zk连接
    public void createConnection(String host, int timeout ) throws Exception {
        zk = new ZooKeeper( host, timeout, this);
    }

    //关闭zk连接
    public void closeConnection() {
        if ( this.zk !=null ) {
            try {
                this.zk.close();
            } catch ( Exception e ) {}
        }
        logger.info("线程"+threadId+"关闭zookeeper连接");
    }

    public void initParentNode() throws Exception
    {
        //父节点只需要创建一次
        synchronized (DistributedLock.class) {
            if(zk.exists(ParentPath, false)==null){
                this.zk.create( ParentPath,"lock".getBytes(),ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.PERSISTENT );
            }
        }
    }

    //获取锁
    public void getLock() throws Exception {
        selfPath = zk.create(SubPath,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        logger.info("线程"+threadId+"创建锁路径:"+selfPath);
        if(isMinPath()){
            getLockSuccess();
        }
    }

    //检查自己是否最小节点
    public boolean isMinPath() throws Exception {
        List<String> subNodes = zk.getChildren(ParentPath, false);
        Collections.sort(subNodes);
        int index = subNodes.indexOf( selfPath.substring(ParentPath.length()+1));
        switch (index){
            case -1:{
                logger.info("线程" + threadId + "的节点" + selfPath + "已不存在");
                return false;
            }
            case 0:{
                logger.info("线程"+threadId+"的节点"+selfPath+"是最小节点");
                return true;
            }
            default:{
                this.watchPath = ParentPath +"/"+ subNodes.get(index - 1);
                logger.info("线程"+threadId+"获取锁节点中，排在前面的节点:"+watchPath);
                try{
                    zk.getData(watchPath, this, new Stat());
                    return false;
                }catch(Exception e){
                    if(zk.exists(watchPath,false) == null){
                        logger.info("线程"+threadId+"排在前面的节点:"+watchPath+"已不存在");
                        return isMinPath();
                    }else
                    {
                        throw e;
                    }
                }
            }
        }
    }

    //获取锁成功
    public void getLockSuccess() throws KeeperException, InterruptedException {
        if(zk.exists(this.selfPath,false) == null){
            logger.error(selfPath+"节点已不存在");
            return;
        }
        logger.info("线程"+threadId+"获取锁成功.............");
        Thread.sleep(2000);
        logger.info("线程"+threadId+"删除本节点："+selfPath);
        zk.delete(this.selfPath, -1);
        closeConnection();
        cd.countDown();
    }


    @Override
    public void process(WatchedEvent event) {
        if(event == null){
            return;
        }
        Event.KeeperState keeperState = event.getState();
        Event.EventType eventType = event.getType();
        if ( Event.KeeperState.SyncConnected == keeperState) {
            if ( Event.EventType.None == eventType ) {
                logger.info("线程" + threadId + "成功连接上zk服务");
            }else if (event.getType() == Event.EventType.NodeDeleted && event.getPath().equals(watchPath)) {
                logger.info( "线程"+threadId+"收到消息，前面的node已删，可再次去获取锁...");
                try {
                    if(isMinPath()){
                        getLockSuccess();
                    }
                } catch (Exception  e) {
                    e.printStackTrace();
                }
            }
        }else if ( Event.KeeperState.Disconnected == keeperState ) {
            logger.info( "线程"+threadId+"与zk服务断开连接" );
        } else if ( Event.KeeperState.AuthFailed == keeperState ) {
            logger.info( "线程"+threadId+"权限检查失败" );
        } else if ( Event.KeeperState.Expired == keeperState ) {
            logger.info( "线程"+threadId+"会话过期" );
        }
    }
}
