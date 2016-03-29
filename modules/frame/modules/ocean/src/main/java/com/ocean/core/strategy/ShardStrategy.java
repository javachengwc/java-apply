package com.ocean.core.strategy;

import java.util.Collection;

/**
 * 分片策略
 */
public interface ShardStrategy {

    public Collection<String> doShard();
}
