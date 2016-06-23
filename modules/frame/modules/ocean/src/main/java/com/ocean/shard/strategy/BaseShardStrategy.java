package com.ocean.shard.strategy;

import com.ocean.shard.model.ShardValue;

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
    /**
     * 根据分片值计算数据源名称集合.
     *
     * @param availableTargetNames 所有的可用数据源名称集合
     * @param shardingValues 分库片值集合
     * @return 分库后指向的数据源名称集合
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Collection<String> doShard(final Collection<String> availableTargetNames, final Collection<ShardValue<?>> shardingValues) {
        if (shardingValues.isEmpty()) {
            return availableTargetNames;
        }
        if (shardAlgorithm instanceof SingleKeyShardAlgorithm) {
            SingleKeyShardAlgorithm<?> singleKeyShardingAlgorithm = (SingleKeyShardAlgorithm<?>) shardAlgorithm;
            ShardValue shardingValue = shardingValues.iterator().next();
            switch (shardingValue.getType()) {
                case SINGLE:
                    return Arrays.asList(singleKeyShardingAlgorithm.doEqualShard(availableTargetNames, shardingValue));
                case LIST:
                    return singleKeyShardingAlgorithm.doInShard(availableTargetNames, shardingValue);
                case RANGE:
                    return singleKeyShardingAlgorithm.doBetweenShard(availableTargetNames, shardingValue);
                default:
                    throw new UnsupportedOperationException(shardingValue.getType().getClass().getName());
            }
        }
        if (shardAlgorithm instanceof MultipleKeysShardAlgorithm) {
            return ((MultipleKeysShardAlgorithm) shardAlgorithm).doShard(availableTargetNames, shardingValues);
        }
        throw new UnsupportedOperationException(shardAlgorithm.getClass().getName());
    }
}
