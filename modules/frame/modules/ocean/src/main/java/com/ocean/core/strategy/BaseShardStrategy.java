package com.ocean.core.strategy;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 基础的分片策略
 */
public class BaseShardStrategy implements ShardStrategy{

    private Collection<String> shardObjs;

    private ShardAlgorithm shardAlgorithm;

    public Collection<String> getShardObjs() {
        return shardObjs;
    }

    public void setShardObjs(Collection<String> shardObjs) {
        this.shardObjs = shardObjs;
    }

    public ShardAlgorithm getShardAlgorithm() {
        return shardAlgorithm;
    }

    public void setShardAlgorithm(ShardAlgorithm shardAlgorithm) {
        this.shardAlgorithm = shardAlgorithm;
    }

    public BaseShardStrategy(String shardObjs, ShardAlgorithm shardAlgorithm) {
        this(Arrays.asList(shardObjs), shardAlgorithm);
    }
    public BaseShardStrategy(List<String> shardObjs, ShardAlgorithm shardAlgorithm) {
        this.shardObjs=shardObjs;
        this.shardAlgorithm=shardAlgorithm;
    }

    public Collection<String> doShard() {

       return null;
    }
}
