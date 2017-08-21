package com.component.microservice.loadbalance.rule;

import com.component.microservice.IServer;
import com.component.microservice.loadbalance.ILoadBalancer;

import java.util.List;
import java.util.Random;

/**
 * 负载均衡规则--随机
 */
public class RandomRule extends AbstractRule {

    private Random rand;

    public RandomRule() {
        rand =new Random(System.currentTimeMillis());
    }

    @Override
    public IServer choose(Object key) {
        return choose(getLoadBalancer(), key);
    }

    public IServer choose(ILoadBalancer loadBalancer, Object key) {
        if (loadBalancer == null) {
            return null;
        }
        IServer server = null;
        while (server == null) {
            if (Thread.interrupted()) {
                return null;
            };
            List<IServer> allList = loadBalancer.getAll();
            int serverCount = allList==null?0:allList.size();
            if (serverCount == 0) {
                return null;
            }
            int index = rand.nextInt(serverCount);
            server = allList.get(index);
            if (server == null) {
                //线程让步。就是说当线程使用了此方法后，它就会把自己CPU执行的时间让出来，让其它线程或自己可运行。
                //此线程从运行状态变为就绪状态。
                Thread.yield();
                continue;
            }
            if (server.isAlive()) {
                return server;
            }
            server=null;
        }
        return server;
    }
}
