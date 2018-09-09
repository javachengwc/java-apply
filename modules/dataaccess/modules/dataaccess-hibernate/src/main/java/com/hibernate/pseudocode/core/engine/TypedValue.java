package com.hibernate.pseudocode.core.engine;

import com.hibernate.pseudocode.core.EntityMode;
import com.hibernate.pseudocode.core.type.Type;

import java.io.Serializable;

public final class TypedValue implements Serializable
{
    private final Type type;
    private final Object value;
    private final EntityMode entityMode;

    public TypedValue(Type type, Object value, EntityMode entityMode)
    {
        this.type = type;
        this.value = value;
        this.entityMode = entityMode;
    }

    public Object getValue() {
        return this.value;
    }

    public Type getType() {
        return this.type;
    }
}
