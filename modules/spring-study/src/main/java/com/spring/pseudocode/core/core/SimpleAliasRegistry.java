package com.spring.pseudocode.core.core;

import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleAliasRegistry implements AliasRegistry
{
    private final Map<String, String> aliasMap = new ConcurrentHashMap<String,String>(16);

    public void registerAlias(String name, String alias)
    {
        if (alias.equals(name)) {
            this.aliasMap.remove(alias);
        }
        else {
            String registeredName = (String)this.aliasMap.get(alias);
            if (registeredName != null) {
                if (registeredName.equals(name))
                {
                    return;
                }
            }

            checkForAliasCircle(name, alias);
            this.aliasMap.put(alias, name);
        }
    }

    public boolean hasAlias(String name, String alias)
    {
        for (Map.Entry entry : this.aliasMap.entrySet()) {
            String registeredName = (String)entry.getValue();
            if (registeredName.equals(name)) {
                String registeredAlias = (String)entry.getKey();
                return (registeredAlias.equals(alias)) || (hasAlias(registeredAlias, alias));
            }
        }
        return false;
    }

    public void removeAlias(String alias)
    {
        String name = (String)this.aliasMap.remove(alias);
    }

    public boolean isAlias(String name)
    {
        return this.aliasMap.containsKey(name);
    }

    public String[] getAliases(String name)
    {
        List result = new ArrayList();
        synchronized (this.aliasMap) {
            retrieveAliases(name, result);
        }
        return StringUtils.toStringArray(result);
    }

    private void retrieveAliases(String name, List<String> result)
    {
        for (Map.Entry entry : this.aliasMap.entrySet()) {
            String registeredName = (String)entry.getValue();
            if (registeredName.equals(name)) {
                String alias = (String)entry.getKey();
                result.add(alias);
                retrieveAliases(alias, result);
            }
        }
    }

    protected void checkForAliasCircle(String name, String alias)
    {
        if (hasAlias(alias, name)) {
            throw new IllegalStateException("Cannot register alias '" + alias + "' for name '" + name);
        }
    }

    public String canonicalName(String name) {
        String canonicalName = name;
        String resolvedName;
        do {
            resolvedName = (String)this.aliasMap.get(canonicalName);
            if (resolvedName != null) {
                canonicalName = resolvedName;
            }
        }
        while (resolvedName != null);
        return canonicalName;
    }
}