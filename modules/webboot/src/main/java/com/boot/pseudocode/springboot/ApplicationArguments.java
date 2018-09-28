package com.boot.pseudocode.springboot;

import java.util.List;
import java.util.Set;

public abstract interface ApplicationArguments
{
    public abstract String[] getSourceArgs();

    public abstract Set<String> getOptionNames();

    public abstract boolean containsOption(String paramString);

    public abstract List<String> getOptionValues(String paramString);

    public abstract List<String> getNonOptionArgs();
}
