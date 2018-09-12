package com.mybatis.pseudocode.mybatis.type;


import org.apache.ibatis.io.Resources;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.*;

//别名注册中心
public class TypeAliasRegistry
{
    //别名map, key为别名，value为对应的类
    private final Map<String, Class<?>> TYPE_ALIASES = new HashMap();

    public TypeAliasRegistry() {
        registerAlias("string", String.class);

        registerAlias("byte", Byte.class);
        registerAlias("long", Long.class);
        registerAlias("short", Short.class);
        registerAlias("int", Integer.class);
        registerAlias("integer", Integer.class);
        registerAlias("double", Double.class);
        registerAlias("float", Float.class);
        registerAlias("boolean", Boolean.class);

        //...

        registerAlias("_byte", Byte.TYPE);
        registerAlias("_long", Long.TYPE);
        registerAlias("_short", Short.TYPE);
        registerAlias("_int", Integer.TYPE);
        registerAlias("_integer", Integer.TYPE);
        registerAlias("_double", Double.TYPE);
        registerAlias("_float", Float.TYPE);
        registerAlias("_boolean", Boolean.TYPE);

        //...

        registerAlias("date", Date.class);
        registerAlias("decimal", BigDecimal.class);
        registerAlias("bigdecimal", BigDecimal.class);
        registerAlias("biginteger", BigInteger.class);
        registerAlias("object", Object.class);

        //...

        registerAlias("map", Map.class);
        registerAlias("hashmap", HashMap.class);
        registerAlias("list", List.class);
        registerAlias("arraylist", ArrayList.class);
        registerAlias("collection", Collection.class);
        registerAlias("iterator", Iterator.class);

        registerAlias("ResultSet", ResultSet.class);
    }

    public <T> Class<T> resolveAlias(String string)
    {
        try
        {
            if (string == null) {
                return null;
            }
            String key = string.toLowerCase(Locale.ENGLISH);
            Class<T> value=null;
            if (this.TYPE_ALIASES.containsKey(key))
                value = (Class)this.TYPE_ALIASES.get(key);
            else {
                value=(Class<T>)Resources.classForName(string);
            }
            return value;
        } catch (Exception e) {
            throw new TypeException("Could not resolve type alias '" + string + "'.  Cause: " + e, e);
        }
    }

    //注册别名，对指定包下的所有类起一个别名，该别名为首字母小写的类名
    public void registerAliases(String packageName)
    {
        registerAliases(packageName, Object.class);
    }

    public void registerAliases(String packageName, Class<?> superType) {
//        ResolverUtil resolverUtil = new ResolverUtil();
//        resolverUtil.find(new ResolverUtil.IsA(superType), packageName);
//        Set<Class> typeSet = resolverUtil.getClasses();
        Set<Class> typeSet =null;
        for (Class type : typeSet)
        {
            if ((!type.isAnonymousClass()) && (!type.isInterface()) && (!type.isMemberClass()))
                registerAlias(type);
        }
    }

    //注册别名，默认别名为类名，如果有Alias注解，去此注解中的值做为别名
    public void registerAlias(Class<?> type)
    {
        String alias = type.getSimpleName();
        Alias aliasAnnotation = (Alias)type.getAnnotation(Alias.class);
        if (aliasAnnotation != null) {
            alias = aliasAnnotation.value();
        }
        registerAlias(alias, type);
    }

    public void registerAlias(String alias, Class<?> value) {
        if (alias == null) {
            throw new TypeException("The parameter alias cannot be null");
        }

        String key = alias.toLowerCase(Locale.ENGLISH);
        if ((this.TYPE_ALIASES.containsKey(key)) && (this.TYPE_ALIASES.get(key) != null) && (!((Class)this.TYPE_ALIASES.get(key)).equals(value))) {
            throw new TypeException("The alias '" + alias + "' is already mapped to the value '" + ((Class)this.TYPE_ALIASES.get(key)).getName() + "'.");
        }
        this.TYPE_ALIASES.put(key, value);
    }

    public void registerAlias(String alias, String value) {
        try {
            registerAlias(alias, Resources.classForName(value));
        } catch (Exception e) {
            throw new TypeException("Error registering type alias " + alias + " for " + value + ". Cause: " + e, e);
        }
    }

    public Map<String, Class<?>> getTypeAliases()
    {
        return Collections.unmodifiableMap(this.TYPE_ALIASES);
    }
}
