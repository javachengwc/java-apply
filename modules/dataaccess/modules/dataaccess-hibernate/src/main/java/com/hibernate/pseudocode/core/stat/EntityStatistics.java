package com.hibernate.pseudocode.core.stat;

import java.io.Serializable;

public abstract interface EntityStatistics extends Serializable
{
    public abstract long getDeleteCount();

    public abstract long getInsertCount();

    public abstract long getLoadCount();

    public abstract long getUpdateCount();

    public abstract long getFetchCount();

    public abstract long getOptimisticFailureCount();
}
