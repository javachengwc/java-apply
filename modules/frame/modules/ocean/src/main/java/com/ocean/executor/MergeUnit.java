package com.ocean.executor;

import java.util.List;

/**
 * 合并单元
 */
public interface MergeUnit<I, O> {

   public O merge(final List<I> params);
}
