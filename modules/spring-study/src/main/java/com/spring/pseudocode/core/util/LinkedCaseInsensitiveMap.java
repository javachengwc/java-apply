package com.spring.pseudocode.core.util;

import java.io.Serializable;
import java.util.*;

//key值不区分大小写的LinkedMap
public class LinkedCaseInsensitiveMap <V> implements Map<String, V>, Serializable, Cloneable
{
    private final LinkedHashMap<String, V> targetMap;
    private final HashMap<String, String> caseInsensitiveKeys;
    private final Locale locale;

    public LinkedCaseInsensitiveMap()
    {
        this((Locale)null);
    }

    public LinkedCaseInsensitiveMap(Locale locale)
    {
        this(16, locale);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity)
    {
        this(initialCapacity, null);
    }

    public LinkedCaseInsensitiveMap(int initialCapacity, Locale locale)
    {
        this.targetMap = new LinkedHashMap<String, V>(initialCapacity)
        {
            public boolean containsKey(Object key) {
                return LinkedCaseInsensitiveMap.this.containsKey(key);
            }

            protected boolean removeEldestEntry(Map.Entry<String, V> eldest) {
                boolean doRemove = LinkedCaseInsensitiveMap.this.removeEldestEntry(eldest);
                if (doRemove) {
                    LinkedCaseInsensitiveMap.this.caseInsensitiveKeys.remove(LinkedCaseInsensitiveMap.this.convertKey((String)eldest.getKey()));
                }
                return doRemove;
            }
        };
        this.caseInsensitiveKeys = new HashMap<String, String>(initialCapacity);
        this.locale = (locale != null ? locale : Locale.getDefault());
    }

    private LinkedCaseInsensitiveMap(LinkedCaseInsensitiveMap<V> other)
    {
        this.targetMap = ((LinkedHashMap)other.targetMap.clone());
        this.caseInsensitiveKeys = ((HashMap)other.caseInsensitiveKeys.clone());
        this.locale = other.locale;
    }

    public int size()
    {
        return this.targetMap.size();
    }

    public boolean isEmpty()
    {
        return this.targetMap.isEmpty();
    }

    public boolean containsKey(Object key)
    {
        return ((key instanceof String)) && (this.caseInsensitiveKeys.containsKey(convertKey((String)key)));
    }

    public boolean containsValue(Object value)
    {
        return this.targetMap.containsValue(value);
    }

    public V get(Object key)
    {
        if ((key instanceof String)) {
            String caseInsensitiveKey = (String)this.caseInsensitiveKeys.get(convertKey((String)key));
            if (caseInsensitiveKey != null) {
                return this.targetMap.get(caseInsensitiveKey);
            }
        }
        return null;
    }

    public V getOrDefault(Object key, V defaultValue)
    {
        if ((key instanceof String)) {
            String caseInsensitiveKey = (String)this.caseInsensitiveKeys.get(convertKey((String)key));
            if (caseInsensitiveKey != null) {
                return this.targetMap.get(caseInsensitiveKey);
            }
        }
        return defaultValue;
    }

    public V put(String key, V value)
    {
        String oldKey = (String)this.caseInsensitiveKeys.put(convertKey(key), key);
        if ((oldKey != null) && (!oldKey.equals(key))) {
            this.targetMap.remove(oldKey);
        }
        return this.targetMap.put(key, value);
    }

    public void putAll(Map<? extends String, ? extends V> map)
    {
        if (map.isEmpty()) {
            return;
        }
        for (Map.Entry<? extends String, ? extends V> entry : map.entrySet()) {
            put((String) entry.getKey(), entry.getValue());
        }
    }

    public V remove(Object key)
    {
        if ((key instanceof String)) {
            String caseInsensitiveKey = (String)this.caseInsensitiveKeys.remove(convertKey((String)key));
            if (caseInsensitiveKey != null) {
                return this.targetMap.remove(caseInsensitiveKey);
            }
        }
        return null;
    }

    public void clear()
    {
        this.caseInsensitiveKeys.clear();
        this.targetMap.clear();
    }

    public Set<String> keySet()
    {
        return this.targetMap.keySet();
    }

    public Collection<V> values()
    {
        return this.targetMap.values();
    }

    public Set<Map.Entry<String, V>> entrySet()
    {
        return this.targetMap.entrySet();
    }

    public LinkedCaseInsensitiveMap<V> clone()
    {
        return new LinkedCaseInsensitiveMap(this);
    }

    public boolean equals(Object obj)
    {
        return this.targetMap.equals(obj);
    }

    public int hashCode()
    {
        return this.targetMap.hashCode();
    }

    public String toString()
    {
        return this.targetMap.toString();
    }

    protected String convertKey(String key)
    {
        return key.toLowerCase(this.locale);
    }

    protected boolean removeEldestEntry(Map.Entry<String, V> eldest)
    {
        return false;
    }
}