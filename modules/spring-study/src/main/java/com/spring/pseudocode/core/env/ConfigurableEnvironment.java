package com.spring.pseudocode.core.env;

import java.util.Map;

public abstract interface ConfigurableEnvironment extends Environment
{
    public abstract void setActiveProfiles(String[] paramArrayOfString);

    public abstract void addActiveProfile(String paramString);

    public abstract void setDefaultProfiles(String[] paramArrayOfString);

    public abstract Map<String, Object> getSystemEnvironment();

    public abstract Map<String, Object> getSystemProperties();

    public abstract void merge(ConfigurableEnvironment paramConfigurableEnvironment);
}
