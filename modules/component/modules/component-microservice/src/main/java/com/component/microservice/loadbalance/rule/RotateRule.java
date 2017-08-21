package com.component.microservice.loadbalance.rule;

import com.component.microservice.IServer;
import com.component.microservice.loadbalance.ILoadBalancer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 负载均衡规则--轮循
 */
public class RotateRule extends AbstractRule{

    private static Integer tryCount=20;

    private AtomicInteger nextIndex;

    public RotateRule() {
        nextIndex = new AtomicInteger(0);
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
        int count = 0;
        List<IServer> allList = loadBalancer.getAll();
        int serverCount = allList==null?0:allList.size();
        int maxTryCount=tryCount;
        if(serverCount>tryCount) {
            maxTryCount=serverCount;
        }
        while (server == null && count<maxTryCount) {
            allList = loadBalancer.getAll();
            serverCount = allList==null?0:allList.size();
            if (serverCount == 0) {
                return null;
            }
            int nextServerIndex = genNextIndex(serverCount);
            server = allList.get(nextServerIndex);
            if (server == null) {
                Thread.yield();
                continue;
            }
            if (server.isAlive()) {
                return server;
            }
            server = null;
            count++;
        }
        if (count >= maxTryCount) {
            logger.info("RotateRule choose no alive server after try count "+count+",over "+maxTryCount);
        }
        return server;
    }

    private int genNextIndex(int serverCount) {
        while(true){
            int current = nextIndex.get();
            int next = (current + 1) % serverCount;
            //比较并交换,原子操作
            if (nextIndex.compareAndSet(current, next)) {
                return next;
            }
        }
    }
}
