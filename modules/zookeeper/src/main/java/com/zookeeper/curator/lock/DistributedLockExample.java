package com.zookeeper.curator.lock;

import com.google.common.collect.Lists;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessLock;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;

/**
 * 分布式锁实例
 */
public class DistributedLockExample {

    private static CuratorFramework client = null;
    private static final String PATH = "/locks";

    // 进程内部（可重入）读写锁
    private static final InterProcessReadWriteLock lock;
    // 读锁
    private static final InterProcessLock readLock;
    // 写锁
    private static final InterProcessLock writeLock;

    static {
        String zkhost="127.0.0.1:2181";//zk的host
        RetryPolicy rp=new ExponentialBackoffRetry(1000, 3);//重试机制
        CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost)
                .connectionTimeoutMs(5000)
                .sessionTimeoutMs(5000)
                .retryPolicy(rp);
        client = builder.build();
        client.start();
        lock = new InterProcessReadWriteLock(client, PATH);
        readLock = lock.readLock();
        writeLock = lock.writeLock();
    }

    public static void main(String[] args) {
        try {
            List<Thread> jobs = Lists.newArrayList();
            for (int i = 0; i < 10; i++) {
                Thread t = new Thread(new ParallelJob("Parallel任务" + i, readLock));
                jobs.add(t);
            }

            for (int i = 0; i < 10; i++) {
                Thread t = new Thread(new MutexJob("Mutex任务" + i, writeLock));
                jobs.add(t);
            }

            for (Thread t : jobs) {
                t.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(client!=null) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }
}
