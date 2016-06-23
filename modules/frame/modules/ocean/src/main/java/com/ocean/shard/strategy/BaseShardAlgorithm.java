package com.ocean.shard.strategy;

import com.ocean.shard.model.ShardValue;

import java.util.Collection;

/**
 * 基础的分片算法
 */
public class BaseShardAlgorithm implements SingleKeyShardAlgorithm<String>, MultipleKeysShardAlgorithm {

    @Override
    public Collection<String> doShard(final Collection<String> availableTableNames, Collection<ShardValue<?>> shardValues) {
        return availableTableNames;
    }

    @Override
    public String doEqualShard(final Collection<String> availableTargetNames, ShardValue<String> shardValues) {
        return availableTargetNames.isEmpty() ? null : availableTargetNames.iterator().next();
    }

    @Override
    public Collection<String> doInShard(final Collection<String> availableTargetNames, ShardValue<String> shardValues) {
        return availableTargetNames;
    }

    @Override
    public Collection<String> doBetweenShard(final Collection<String> availableTargetNames, ShardValue<String> shardValues) {
        return availableTargetNames;
    }
}
