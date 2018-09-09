package com.hibernate.pseudocode.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class FetchMode  implements Serializable
{
    private final String name;

    private static final Map INSTANCES = new HashMap();

    public static final FetchMode DEFAULT = new FetchMode("DEFAULT");

    public static final FetchMode JOIN = new FetchMode("JOIN");

    public static final FetchMode SELECT = new FetchMode("SELECT");

    /** @deprecated */
    public static final FetchMode LAZY = SELECT;

    /** @deprecated */
    public static final FetchMode EAGER = JOIN;

    private FetchMode(String name)
    {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    private Object readResolve()
    {
        return INSTANCES.get(this.name);
    }

    static
    {
        INSTANCES.put(JOIN.name, JOIN);
        INSTANCES.put(SELECT.name, SELECT);
        INSTANCES.put(DEFAULT.name, DEFAULT);
    }
}
