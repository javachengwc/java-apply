package com.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;
import org.apache.zookeeper.data.Stat;

/**
 * curator常用操作例子
 * create()增
 * delete(): 删
 * setData():  改
 * getData(): 查
 * checkExists(): 判断是否存在
 * 所有这些方法都以forpath()结尾，辅以watch(监听)，withMode（指定模式），和inBackground（后台运行）等方法使用
 */
public class CrudExample {

    private static final String PATH = "/ma";

    public static void main(String[] args) {
        CuratorFramework client=null;
        try {
            String zkhost="127.0.0.1:2181";//zk的host

            RetryPolicy rp=new ExponentialBackoffRetry(1000, 3);//重试机制
            CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost)
                    .connectionTimeoutMs(5000)
                    .sessionTimeoutMs(5000)
                    .retryPolicy(rp);
            client = builder.build();
            client.start();


            client.create().forPath(PATH, "yaya".getBytes());

            byte[] bs = client.getData().forPath(PATH);
            System.out.println("新建的节点，data为:" + new String(bs));

            client.setData().forPath(PATH, "another".getBytes());

            // 由于是在background模式下获取的data，此时的bs可能为null
            byte[] bs2 = client.getData().watched().inBackground().forPath(PATH);
            System.out.println("修改后的data为" + new String(bs2 != null ? bs2 : new byte[0]));

            client.delete().forPath(PATH);

            Stat stat = client.checkExists().forPath(PATH);
            System.out.println((stat==null)?"stat is null":stat);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(client!=null) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }
}
