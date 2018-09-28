package com.boot.pseudocode.autoconfigure;

public abstract interface AutoConfigurationImportFilter
{
    public abstract boolean[] match(String[] array, AutoConfigurationMetadata metadata);
}
