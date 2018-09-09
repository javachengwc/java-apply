package com.hibernate.pseudocode.core.stat;

import java.util.Set;

public abstract interface SessionStatistics
{
    public abstract int getEntityCount();

    public abstract int getCollectionCount();

    public abstract Set getEntityKeys();

    public abstract Set getCollectionKeys();
}
