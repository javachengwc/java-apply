package com.zookeeper.demo;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;


public class Main {
	
	public static void main(String args []) throws Exception 
	{
		ZooKeeper zk=new ZooKeeper("127.0.0.1:5000", 5000, new Watcher() {
            
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
        zk.close();//关闭连接  
	}

}
