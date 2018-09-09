package com.hibernate.pseudocode.core.stat;


public abstract interface Statistics
{
    public abstract void clear();

    public abstract EntityStatistics getEntityStatistics(String param);

    //...

    public abstract long getEntityDeleteCount();

    public abstract long getEntityInsertCount();

    public abstract long getEntityLoadCount();

    public abstract long getEntityFetchCount();

    public abstract long getEntityUpdateCount();

    public abstract long getQueryExecutionCount();

    public abstract long getQueryExecutionMaxTime();

    public abstract String getQueryExecutionMaxTimeQueryString();

    public abstract long getQueryCacheHitCount();

    public abstract long getQueryCacheMissCount();

    public abstract long getQueryCachePutCount();

    public abstract long getFlushCount();

    public abstract long getConnectCount();

    public abstract long getSecondLevelCacheHitCount();

    public abstract long getSecondLevelCacheMissCount();

    public abstract long getSecondLevelCachePutCount();

    public abstract long getSessionCloseCount();

    public abstract long getSessionOpenCount();

    public abstract long getCollectionLoadCount();

    public abstract long getCollectionFetchCount();

    public abstract long getCollectionUpdateCount();

    public abstract long getCollectionRemoveCount();

    public abstract long getCollectionRecreateCount();

    public abstract long getStartTime();

    public abstract void logSummary();

    public abstract boolean isStatisticsEnabled();

    public abstract void setStatisticsEnabled(boolean paramBoolean);

    public abstract String[] getQueries();

    public abstract String[] getEntityNames();

    public abstract String[] getCollectionRoleNames();

    public abstract String[] getSecondLevelCacheRegionNames();

    public abstract long getSuccessfulTransactionCount();

    public abstract long getTransactionCount();

    public abstract long getPrepareStatementCount();

    public abstract long getCloseStatementCount();

    public abstract long getOptimisticFailureCount();
}
