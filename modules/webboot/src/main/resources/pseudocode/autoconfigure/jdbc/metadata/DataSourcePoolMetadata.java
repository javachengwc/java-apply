package com.boot.pseudocode.autoconfigure.jdbc.metadata;

public abstract interface DataSourcePoolMetadata
{
    public abstract Float getUsage();

    public abstract Integer getActive();

    public abstract Integer getMax();

    public abstract Integer getMin();

    public abstract String getValidationQuery();
}
