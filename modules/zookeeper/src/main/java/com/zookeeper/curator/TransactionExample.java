package com.zookeeper.curator;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.transaction.CuratorTransaction;
import org.apache.curator.framework.api.transaction.CuratorTransactionFinal;
import org.apache.curator.framework.api.transaction.CuratorTransactionResult;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.Collection;

/**
 * curator事务操作例子
 */
public class TransactionExample {

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

            // 开启事务
            CuratorTransaction transaction = client.inTransaction();

            Collection<CuratorTransactionResult> results = transaction.create()
                    .forPath("/a/path", "some data".getBytes()).and().setData()
                    .forPath("/another/path", "other data".getBytes()).and().delete().forPath("/yet/another/path")
                    .and().commit();

            for (CuratorTransactionResult result : results) {
                System.out.println(result.getForPath() + " - " + result.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 释放客户端连接
            if(client!=null) {
                CloseableUtils.closeQuietly(client);
            }
        }

    }
}
