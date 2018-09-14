package com.boot.pseudocode.autoconfigure;

import java.util.Set;

public abstract interface AutoConfigurationMetadata
{
    public abstract boolean wasProcessed(String paramString);

    public abstract Integer getInteger(String paramString1, String paramString2);

    public abstract Integer getInteger(String paramString1, String paramString2, Integer paramInteger);

    public abstract Set<String> getSet(String paramString1, String paramString2);

    public abstract Set<String> getSet(String paramString1, String paramString2, Set<String> paramSet);

    public abstract String get(String paramString1, String paramString2);

    public abstract String get(String paramString1, String paramString2, String paramString3);
}
