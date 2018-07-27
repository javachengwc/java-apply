package com.spring.pseudocode.core.core.env;

public abstract interface Environment
{
    public abstract String[] getActiveProfiles();

    public abstract String[] getDefaultProfiles();

    public abstract boolean acceptsProfiles(String[] paramArrayOfString);
}
