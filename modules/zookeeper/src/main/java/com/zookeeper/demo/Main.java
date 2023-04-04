package com.zookeeper.demo;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;


public class Main {
	
	public static void main(String args []) throws Exception 
	{
		ZooKeeper zk=new ZooKeeper("xxx:2181", 5000, new Watcher() {
            
			CountDownLatch down=new CountDownLatch(1);//同步阻塞状态  
            @Override  
            public void process(WatchedEvent event) {  
	             if(event.getState()==Event.KeeperState.SyncConnected){  
	                 down.countDown();//连接上之后，释放计数器  
	             }  
	             System.out.println("连接---成功："+event.toString());  
             
            }  
        });  
          
        System.out.println("连接成功："+zk.toString());
		Stat stat = zk.exists("/dubbo/com.xxx.XxxService",false);
		System.out.println(stat!=null);
		Stat stat2 = zk.exists("/dubbo/com.xxx.XxxService/consumers",false);
		System.out.println(stat2!=null);

		List<String> list  = zk.getChildren("/dubbo/com.xxx.XxxService/providers",false);
		int cnt = list ==null ? 0: list.size();
		System.out.println("list.size="+cnt);
		if(cnt>0) {
			for(String per: list) {
				System.out.println(per);
			}
		}
        zk.close();//关闭连接  
	}

}
