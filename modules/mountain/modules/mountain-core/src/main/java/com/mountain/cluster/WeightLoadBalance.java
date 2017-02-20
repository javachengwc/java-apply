package com.mountain.cluster;

import com.mountain.core.Invoker;
import com.util.base.RandomUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * 权重负载均衡
 */
public class WeightLoadBalance extends AbstractLoadBalance {

    protected Invoker doSelect(List<Invoker> invokers) {
        int count = invokers.size();
        int totalWeight=0;
        Map<Integer, Integer> probs = new HashMap<Integer,Integer>();
        for (int i = 0; i < count; i++) {
            int weight = getWeight(invokers.get(i));
            totalWeight+=weight;
            probs.put(i,weight);
        }
        if(totalWeight<=0)
        {
            return invokers.get( new Random().nextInt(count));
        }
        int selectId = RandomUtil.randomGetInIntMap(probs,totalWeight);
        probs.clear();
        return invokers.get(selectId);
    }
}