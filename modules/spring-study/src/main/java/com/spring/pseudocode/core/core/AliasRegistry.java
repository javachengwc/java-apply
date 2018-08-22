package com.spring.pseudocode.core.core;

public abstract interface AliasRegistry
{
    public abstract void registerAlias(String paramString1, String paramString2);

    public abstract void removeAlias(String paramString);

    public abstract boolean isAlias(String paramString);

    public abstract String[] getAliases(String paramString);
}
