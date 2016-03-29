package com.ocean.core.strategy;

import com.ocean.core.model.ShardValue;

import java.util.Collection;

/**
 * 单片键分片法接口
 */
public interface SingleKeyShardAlgorithm<T extends Comparable<?>> extends ShardAlgorithm {

    /**
     * 根据分片值和SQL的=运算符计算分片结果名称集合
     */
    String doEqualShard(Collection<String> availableTargetNames, ShardValue<T> shardingValue);

    /**
     * 根据分片值和SQL的IN运算符计算分片结果名称集合
     */
    Collection<String> doInShard(Collection<String> availableTargetNames, ShardValue<T> shardingValue);

    /**
     * 根据分片值和SQL的BETWEEN运算符计算分片结果名称集合
     */
    Collection<String> doBetweenShard(Collection<String> availableTargetNames, ShardValue<T> shardingValue);
}

