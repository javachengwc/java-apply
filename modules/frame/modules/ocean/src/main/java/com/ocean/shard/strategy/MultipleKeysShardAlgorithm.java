package com.ocean.shard.strategy;

import com.ocean.shard.model.ShardValue;

import java.util.Collection;

/**
 * 多片键分片法接口
 */
public interface MultipleKeysShardAlgorithm extends ShardAlgorithm {

    /**
     * 根据分片值计算分片结果名称集合
     */
    public Collection<String> doShard(Collection<String> availableTargetNames, Collection<ShardValue<?>> shardingValues);
}
