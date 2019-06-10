package com.pseudocode.netflix.ribbon.loadbalancer.rule;

import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.ILoadBalancer;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

import java.util.List;
import java.util.Random;

//随机规则
public class RandomRule extends AbstractLoadBalancerRule {

    Random rand;

    public RandomRule() {
        rand = new Random();
    }

    /**
     * Randomly choose from all living servers
     */
    public Server choose(ILoadBalancer lb, Object key) {
        if (lb == null) {
            return null;
        }
        Server server = null;

        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            }
            List<Server> upList = lb.getReachableServers();
            List<Server> allList = lb.getAllServers();

            int serverCount = allList.size();
            if (serverCount == 0) {
                return null;
            }

            int index = rand.nextInt(serverCount);
            server = upList.get(index);

            if (server == null) {
                Thread.yield();
                continue;
            }

            if (server.isAlive()) {
                return (server);
            }

            server = null;
            //Thread.yield()方法告诉虚拟机它乐意让其他线程占用自己的位置。这表明该线程没有在做一些紧急的事情。
            // 它仅能使一个线程从运行状态转到可运行状态，而不是等待或阻塞状态
            Thread.yield();
        }
        return server;
    }

    @Override
    public Server choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {

    }
}
