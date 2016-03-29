package com.zookeeper.curator.leader;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

/**
 * leader选举
 */
public class LeaderSelectMain {

    public static void main(String[] args) {

        String zkhost="127.0.0.1:2181";//zk的host

        List<CuratorFramework> clients = Lists.newArrayList();
        List<LeaderSelectorClient> examples = Lists.newArrayList();
        try {
            for (int i = 0; i < 10; i++) {
                CuratorFramework client = CuratorFrameworkFactory.newClient(zkhost, new ExponentialBackoffRetry(1000, 3));
                LeaderSelectorClient example = new LeaderSelectorClient(client, "Client #" + i);
                clients.add(client);
                examples.add(example);

                client.start();
                example.start();
            }

            System.out.println("----------先观察一会选举的结果-----------");
            Thread.sleep(10000);

            System.out.println("----------关闭前5个客户端，再观察选举的结果-----------");
            for (int i = 0; i < 5; i++) {
                clients.get(i).close();
            }

            // 让main程序一直监听控制台输入，异步的代码就可以一直在执行。
            // 不同于while(ture)的是，按回车或esc可退出
            new BufferedReader(new InputStreamReader(System.in)).readLine();

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            for (LeaderSelectorClient exampleClient : examples) {
                CloseableUtils.closeQuietly(exampleClient);
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }
}
