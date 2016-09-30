package com.mountain.zookeeper;

import org.I0Itec.zkclient.IZkStateListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 用I0Itec的zookeeper客户端
 */
public class ZookeeperIoiClient {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperIoiClient.class);

    private ZkClient client;

    private Watcher.Event.KeeperState curState=Watcher.Event.KeeperState.Disconnected;

    private Set<StateListener> stateListeners = new CopyOnWriteArraySet<StateListener>();

    public ZookeeperIoiClient(String url,int timeout) {
        client = new ZkClient(url,timeout);
        client.subscribeStateChanges(new IZkStateListener() {

            public void handleStateChanged(Watcher.Event.KeeperState state) throws Exception {
                logger.info("ZookeeperIoiClient handleStateChanged state="+state);
                ZookeeperIoiClient.this.curState=state;
                if (state == Watcher.Event.KeeperState.Disconnected || state == Watcher.Event.KeeperState.SyncConnected) {
                    stateChanged(state);
                }
            }
            public void handleNewSession() throws Exception {
                logger.info("ZookeeperIoiClient handleNewSession");
                reconnected();
            }
        });
    }

    public ZkClient getClient() {
        return client;
    }

    public boolean isConnected()
    {
        return curState == Watcher.Event.KeeperState.SyncConnected;
    }

    public void create(String path, boolean ephemeral)
    {
        System.out.println("path="+path);
        int i = path.lastIndexOf('/');
        if (i > 0) {
            create(path.substring(0, i), false);
        }
        try {
            if (client.exists(path))
            {
                return;
            }
            if (ephemeral) {
                client.createEphemeral(path);
            } else {
                client.createPersistent(path, true);
            }
        }catch(Exception e)
        {
            logger.error("ZookeeperIoiClient create error,path=" + path, e);
        }
    }

    public void delete(String path)
    {
        try {
            if (client.exists(path)) {
                client.deleteRecursive(path);
            }
        } catch (Exception  e) {
            logger.error("ZookeeperIoiClient delete error,path="+path,e);
        }
    }

    public boolean checkExist(String path)
    {
        return client.exists(path);
    }

    boolean writeData(String path, Object value)
    {
        boolean flag = false;
        try {
            if (client.exists(path)) {
                client.writeData(path, value);
            } else {
                client.createPersistent(path, true);
                client.writeData(path, value);
            }
            flag = true;
        } catch (Exception e) {
            logger.error("ZookeeperIoiClient writeData error,path="+path+",value="+value,e);
        }
        return flag;
    }

    public String getData(String path)
    {
        String data = client.readData(path);
        return data;
    }

    public List<String> getChildren(String path)
    {
        try {
            return client.getChildren(path);
        } catch (Exception e) {
            logger.error("ZookeeperIoiClient getChildren error,path="+path,e);
            return null;
        }
    }

    public void close()
    {
        if(client!=null)
        {
            client.close();
        }
    }

    public void stateChanged(Watcher.Event.KeeperState state)
    {
        if(stateListeners!=null)
        {
            for(StateListener listener:stateListeners)
            {
                listener.stateChanged(state);
            }
        }
    }

    public void reconnected()
    {
        if(stateListeners!=null)
        {
            for(StateListener listener:stateListeners)
            {
                listener.reconnected();
            }
        }
    }

    public static void main(String args []) throws Exception
    {
        ZookeeperIoiClient client = new ZookeeperIoiClient("127.0.0.1:2181",60000);
        System.out.println(client.checkExist("/aa/bb/ca"));
        client.writeData("/zk/cc334/zzz/ab/cd","ccc");
        System.out.println(client.getData("/zk/cc334/zzz/ab/cd"));

        Thread.sleep(10*1000l);
        client.create("/ab/ba/cb",true);
        client.create("/aa/bb/ca",true);
        //client.delete("/aa/bb/cc");

        try {
            System.out.println(client.getData("/zk/cc334/zzz"));
        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }

        Thread.sleep(100*1000l);
        client.close();
    }

}
