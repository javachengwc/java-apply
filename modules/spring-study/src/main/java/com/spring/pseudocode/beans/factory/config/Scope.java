package com.spring.pseudocode.beans.factory.config;

import com.spring.pseudocode.beans.factory.ObjectFactory;

public abstract interface Scope
{
    public abstract Object get(String param, ObjectFactory<?> objectFactory);

    public abstract Object remove(String param);

    public abstract void registerDestructionCallback(String param, Runnable runnable);

    public abstract Object resolveContextualObject(String param);

    public abstract String getConversationId();
}
