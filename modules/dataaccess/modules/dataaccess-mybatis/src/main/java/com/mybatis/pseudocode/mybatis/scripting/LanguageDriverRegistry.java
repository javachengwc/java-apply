package com.mybatis.pseudocode.mybatis.scripting;

import java.util.HashMap;
import java.util.Map;

public class LanguageDriverRegistry {

    private final Map<Class<?>, LanguageDriver> LANGUAGE_DRIVER_MAP = new HashMap();

    private Class<?> defaultDriverClass;

    public void register(Class<?> cls)
    {
        if (cls == null) {
            throw new IllegalArgumentException("null is not a valid Language Driver");
        }
        if (!this.LANGUAGE_DRIVER_MAP.containsKey(cls))
            try {
                this.LANGUAGE_DRIVER_MAP.put(cls, (LanguageDriver)cls.newInstance());
            } catch (Exception ex) {
                throw new ScriptingException("Failed to load language driver for " + cls.getName(), ex);
            }
    }

    public void register(LanguageDriver instance)
    {
        if (instance == null) {
            throw new IllegalArgumentException("null is not a valid Language Driver");
        }
        Class cls = instance.getClass();
        if (!this.LANGUAGE_DRIVER_MAP.containsKey(cls))
            this.LANGUAGE_DRIVER_MAP.put(cls, instance);
    }

    public LanguageDriver getDriver(Class<?> cls)
    {
        return (LanguageDriver)this.LANGUAGE_DRIVER_MAP.get(cls);
    }

    public LanguageDriver getDefaultDriver() {
        return getDriver(getDefaultDriverClass());
    }

    public Class<?> getDefaultDriverClass() {
        return this.defaultDriverClass;
    }

    public void setDefaultDriverClass(Class<?> defaultDriverClass) {
        register(defaultDriverClass);
        this.defaultDriverClass = defaultDriverClass;
    }
}
