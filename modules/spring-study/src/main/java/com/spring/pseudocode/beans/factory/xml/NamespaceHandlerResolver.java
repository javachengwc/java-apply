package com.spring.pseudocode.beans.factory.xml;

public abstract interface NamespaceHandlerResolver
{
    public abstract NamespaceHandler resolve(String paramString);
}
