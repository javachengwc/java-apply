package com.ocean.shard.strategy;

import com.ocean.shard.model.ShardValue;

import java.util.Collection;

/**
 * 分片策略
 */
public interface ShardStrategy {

    public Collection<String> doShard(Collection<String> availableTargetNames, Collection<ShardValue<?>> shardingValues);

    public Collection<String> getShardObjs();

    public ShardAlgorithm getShardAlgorithm();
}
