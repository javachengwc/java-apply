package com.zookeeper.ioitec;


import java.util.UUID;

import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;


public class MasterApp {

    private ZkClient zkClient;

    public ZkClient getZkClient() {
        return zkClient;
    }

    public void setZkClient(ZkClient zkClient) {
        this.zkClient = zkClient;
    }


    /**
     * 函数入口
     * @param args
     */
    public static void main( String[] args ) {

        MasterApp bootStrap=new MasterApp();
        bootStrap.initialize();

        try {
            Thread.sleep(100000000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    /**
     * 初始化zookeeper
     */
    public void initialize() {

        String connectionString="192.168.19.128:2181";
        int connectionTimeout=50000;

        zkClient=new ZkClient(connectionString, connectionTimeout);

        if(!zkClient.exists("/root1")) {
            zkClient.create("/root1", new Long(System.currentTimeMillis()), CreateMode.EPHEMERAL);
        }

        new Thread(new RootNodeChangeThread()).start();
    }

    /**
     * 每20s改变一次 'root1'节点的数据
     * @author yang
     *
     */
    private class RootNodeChangeThread implements Runnable{

        public void run() {

            while(true) {

                try {
                    Thread.sleep(20000);
                } catch (InterruptedException e) {
                    //ignore
                }

                String uuidStr=UUID.randomUUID().toString();

                System.out.println(">>>>>>>>>> 产生随机的 uuid string,'uuidStr'===>"+uuidStr);

                zkClient.writeData("/root1", uuidStr);

            }

        }

    }
}