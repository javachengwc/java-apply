package com.dubbo.client;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import java.util.List;
import java.util.concurrent.CountDownLatch;

public class ZookeeperCheck {

    //检查zookeeper
    public static void main(String args []) throws Exception
    {
        ZooKeeper zk=connectZk("ip:2181");
        System.out.println("连接成功："+zk.toString());
        Stat stat =pathStat(zk,"/dubbo/com.xxx.service.XxxService");
        System.out.println(stat!=null);
        Stat stat2 = pathStat(zk,"/dubbo/com.xxx.service.XxxService/consumers");
        System.out.println(stat2!=null);

        List<String> list  = listPathChild(zk,"/dubbo/com.xxx.service.XxxService/providers");
        int cnt = list ==null ? 0: list.size();
        System.out.println("list.size="+cnt);
        if(cnt>0) {
            for(String per: list) {
                System.out.println(per);
            }
        }
        //关闭连接
        zk.close();
    }

    //连接zk, xxx:2181
    private static ZooKeeper connectZk(String zkUrl) throws Exception {
        ZooKeeper zk=new ZooKeeper(zkUrl, 5000, new Watcher() {
            CountDownLatch down=new CountDownLatch(1);//同步阻塞状态
            @Override
            public void process(WatchedEvent event) {
                if(event.getState()==Event.KeeperState.SyncConnected){
                    down.countDown();//连接上之后，释放计数器
                }
                System.out.println("连接---成功："+event.toString());

            }
        });
        return zk;
    }

    //path状态, /dubbo/xxx.xxxService
    private static Stat pathStat(ZooKeeper zk,String path) throws Exception {
        Stat stat = zk.exists(path,false);
        return stat;
    }

    //列出path子节点, /dubbo/xxx.xxxService/providers
    private static List<String> listPathChild(ZooKeeper zk,String path) throws Exception {
        List<String> list  = zk.getChildren(path,false);
        return list;
    }

}
