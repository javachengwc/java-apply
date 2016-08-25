package com.djy.cluster;

import com.djy.core.Invoker;

import java.util.List;

public abstract class AbstractLoadBalance implements LoadBalance {

    public Invoker select(List<Invoker> invokers) {
        if (invokers == null || invokers.size() == 0)
            return null;
        if (invokers.size() == 1)
            return invokers.get(0);
        return doSelect(invokers);
    }

    protected abstract Invoker doSelect(List<Invoker> invokers);

    protected int getWeight(Invoker invoker) {
        int weight = Integer.parseInt(invoker.getUrl().getParameter("weight", "100"));
        return weight;
    }
}