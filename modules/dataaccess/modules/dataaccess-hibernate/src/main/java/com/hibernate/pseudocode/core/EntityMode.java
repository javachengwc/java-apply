package com.hibernate.pseudocode.core;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntityMode implements Serializable
{
    private static final Map INSTANCES = new HashMap();

    public static final EntityMode POJO = new EntityMode("pojo");
    public static final EntityMode DOM4J = new EntityMode("dom4j");
    public static final EntityMode MAP = new EntityMode("dynamic-map");
    private final String name;

    public EntityMode(String name)
    {
        this.name = name;
    }

    public String toString() {
        return this.name;
    }

    private Object readResolve() {
        return INSTANCES.get(this.name);
    }

    public static EntityMode parse(String name) {
        EntityMode rtn = (EntityMode)INSTANCES.get(name);
        if (rtn == null)
        {
            rtn = POJO;
        }
        return rtn;
    }

    static
    {
        INSTANCES.put(POJO.name, POJO);
        INSTANCES.put(DOM4J.name, DOM4J);
        INSTANCES.put(MAP.name, MAP);
    }
}
