package com.mybatis.pseudocode.mybatis.type;


import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class TypeHandlerRegistry
{
    private final Map<JdbcType, TypeHandler<?>> JDBC_TYPE_HANDLER_MAP = new EnumMap(JdbcType.class);
    private final Map<Type, Map<JdbcType, TypeHandler<?>>> TYPE_HANDLER_MAP = new ConcurrentHashMap();
    private final TypeHandler<Object> UNKNOWN_TYPE_HANDLER = new UnknownTypeHandler(this);
    private final Map<Class<?>, TypeHandler<?>> ALL_TYPE_HANDLERS_MAP = new HashMap();

    private static final Map<JdbcType, TypeHandler<?>> NULL_TYPE_HANDLER_MAP = new HashMap();

    private Class<? extends TypeHandler> defaultEnumTypeHandler = EnumTypeHandler.class;

    public TypeHandlerRegistry() {
        //内置的typeHandler注册
        //...

//        register(Short.class, new ShortTypeHandler());
//        register(Short.TYPE, new ShortTypeHandler());
//        register(JdbcType.SMALLINT, new ShortTypeHandler());

        register(Integer.class, new IntegerTypeHandler());
        register(Integer.TYPE, new IntegerTypeHandler());
        register(JdbcType.INTEGER, new IntegerTypeHandler());

        //...
    }

    public void setDefaultEnumTypeHandler(Class<? extends TypeHandler> typeHandler)
    {
        this.defaultEnumTypeHandler = typeHandler;
    }

    public boolean hasTypeHandler(Class<?> javaType) {
        return hasTypeHandler(javaType, null);
    }

    public boolean hasTypeHandler(TypeReference<?> javaTypeReference) {
        return hasTypeHandler(javaTypeReference, null);
    }

    public boolean hasTypeHandler(Class<?> javaType, JdbcType jdbcType) {
        return (javaType != null) && (getTypeHandler(javaType, jdbcType) != null);
    }

    public boolean hasTypeHandler(TypeReference<?> javaTypeReference, JdbcType jdbcType) {
        return (javaTypeReference != null) && (getTypeHandler(javaTypeReference, jdbcType) != null);
    }

    public TypeHandler<?> getMappingTypeHandler(Class<? extends TypeHandler<?>> handlerType) {
        return (TypeHandler)this.ALL_TYPE_HANDLERS_MAP.get(handlerType);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type) {
        return getTypeHandler(type, null);
    }

    public <T> TypeHandler<T> getTypeHandler(TypeReference<T> javaTypeReference) {
        return getTypeHandler(javaTypeReference, null);
    }

    public TypeHandler<?> getTypeHandler(JdbcType jdbcType) {
        return (TypeHandler)this.JDBC_TYPE_HANDLER_MAP.get(jdbcType);
    }

    public <T> TypeHandler<T> getTypeHandler(Class<T> type, JdbcType jdbcType) {
        return getTypeHandler(type, jdbcType);
    }

    public <T> TypeHandler<T> getTypeHandler(TypeReference<T> javaTypeReference, JdbcType jdbcType) {
        return getTypeHandler(javaTypeReference.getRawType(), jdbcType);
    }

    private <T> TypeHandler<T> getTypeHandler(Type type, JdbcType jdbcType)
    {
        Map jdbcHandlerMap = getJdbcHandlerMap(type);
        TypeHandler handler = null;
        if (jdbcHandlerMap != null) {
            handler = (TypeHandler)jdbcHandlerMap.get(jdbcType);
            if (handler == null) {
                handler = (TypeHandler)jdbcHandlerMap.get(null);
            }
            if (handler == null)
            {
                handler = pickSoleHandler(jdbcHandlerMap);
            }
        }

        return handler;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMap(Type type)
    {
        Map jdbcHandlerMap = (Map)this.TYPE_HANDLER_MAP.get(type);
        if (NULL_TYPE_HANDLER_MAP.equals(jdbcHandlerMap)) {
            return null;
        }
        if ((jdbcHandlerMap == null) && ((type instanceof Class))) {
            Class clazz = (Class)type;
            if (clazz.isEnum()) {
                jdbcHandlerMap = getJdbcHandlerMapForEnumInterfaces(clazz, clazz);
                if (jdbcHandlerMap == null) {
                    register(clazz, getInstance(clazz, this.defaultEnumTypeHandler));
                    return (Map)this.TYPE_HANDLER_MAP.get(clazz);
                }
            } else {
                jdbcHandlerMap = getJdbcHandlerMapForSuperclass(clazz);
            }
        }
        this.TYPE_HANDLER_MAP.put(type, jdbcHandlerMap == null ? NULL_TYPE_HANDLER_MAP : jdbcHandlerMap);
        return jdbcHandlerMap;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMapForEnumInterfaces(Class<?> clazz, Class<?> enumClazz) {
        for (Class iface : clazz.getInterfaces()) {
            Map<JdbcType, TypeHandler<?>> jdbcHandlerMap = (Map<JdbcType, TypeHandler<?>>)this.TYPE_HANDLER_MAP.get(iface);
            if (jdbcHandlerMap == null) {
                jdbcHandlerMap = getJdbcHandlerMapForEnumInterfaces(iface, enumClazz);
            }
            if (jdbcHandlerMap == null)
                continue;
            HashMap newMap = new HashMap();
            for (Map.Entry<JdbcType, TypeHandler<?>> entry : jdbcHandlerMap.entrySet())
            {
                newMap.put(entry.getKey(), getInstance(enumClazz, ((TypeHandler)entry.getValue()).getClass()));
            }
            return newMap;
        }

        return null;
    }

    private Map<JdbcType, TypeHandler<?>> getJdbcHandlerMapForSuperclass(Class<?> clazz) {
        Class superclass = clazz.getSuperclass();
        if ((superclass == null) || (Object.class.equals(superclass))) {
            return null;
        }
        Map jdbcHandlerMap = (Map)this.TYPE_HANDLER_MAP.get(superclass);
        if (jdbcHandlerMap != null) {
            return jdbcHandlerMap;
        }
        return getJdbcHandlerMapForSuperclass(superclass);
    }

    private TypeHandler<?> pickSoleHandler(Map<JdbcType, TypeHandler<?>> jdbcHandlerMap)
    {
        TypeHandler soleHandler = null;
        for (TypeHandler handler : jdbcHandlerMap.values()) {
            if (soleHandler == null)
                soleHandler = handler;
            else if (!handler.getClass().equals(soleHandler.getClass()))
            {
                return null;
            }
        }
        return soleHandler;
    }

    public TypeHandler<Object> getUnknownTypeHandler() {
        return this.UNKNOWN_TYPE_HANDLER;
    }

    public void register(JdbcType jdbcType, TypeHandler<?> handler) {
        this.JDBC_TYPE_HANDLER_MAP.put(jdbcType, handler);
    }

    public <T> void register(TypeHandler<T> typeHandler)
    {
        boolean mappedTypeFound = false;
        MappedTypes mappedTypes = (MappedTypes)typeHandler.getClass().getAnnotation(MappedTypes.class);
        if (mappedTypes != null) {
            for (Class handledType : mappedTypes.value()) {
                register(handledType, typeHandler);
                mappedTypeFound = true;
            }
        }

        if ((!mappedTypeFound) && ((typeHandler instanceof TypeReference)))
            try {
                TypeReference typeReference = (TypeReference)typeHandler;
                register(typeReference.getRawType(), typeHandler);
                mappedTypeFound = true;
            }
            catch (Throwable localThrowable1)
            {
            }
        if (!mappedTypeFound)
            register((Class)null, typeHandler);
    }

    public <T> void register(Class<T> javaType, TypeHandler<? extends T> typeHandler)
    {
        register(javaType, typeHandler);
    }

    private <T> void register(Type javaType, TypeHandler<? extends T> typeHandler) {
        MappedJdbcTypes mappedJdbcTypes = (MappedJdbcTypes)typeHandler.getClass().getAnnotation(MappedJdbcTypes.class);
        if (mappedJdbcTypes != null) {
            for (JdbcType handledJdbcType : mappedJdbcTypes.value()) {
                register(javaType, handledJdbcType, typeHandler);
            }
            if (mappedJdbcTypes.includeNullJdbcType())
                register(javaType, null, typeHandler);
        }
        else {
            register(javaType, null, typeHandler);
        }
    }

    public <T> void register(TypeReference<T> javaTypeReference, TypeHandler<? extends T> handler) {
        register(javaTypeReference.getRawType(), handler);
    }

    public <T> void register(Class<T> type, JdbcType jdbcType, TypeHandler<? extends T> handler)
    {
        register(type, jdbcType, handler);
    }

    private void register(Type javaType, JdbcType jdbcType, TypeHandler<?> handler) {
        if (javaType != null) {
            Map map = (Map)this.TYPE_HANDLER_MAP.get(javaType);
            if (map == null) {
                map = new HashMap();
                this.TYPE_HANDLER_MAP.put(javaType, map);
            }
            map.put(jdbcType, handler);
        }
        this.ALL_TYPE_HANDLERS_MAP.put(handler.getClass(), handler);
    }

    public void register(Class<?> typeHandlerClass)
    {
        boolean mappedTypeFound = false;
        MappedTypes mappedTypes = (MappedTypes)typeHandlerClass.getAnnotation(MappedTypes.class);
        if (mappedTypes != null) {
            for (Class javaTypeClass : mappedTypes.value()) {
                register(javaTypeClass, typeHandlerClass);
                mappedTypeFound = true;
            }
        }
        if (!mappedTypeFound)
            register(getInstance(null, typeHandlerClass));
    }

    public void register(String javaTypeClassName, String typeHandlerClassName) throws ClassNotFoundException
    {
        //register(Resources.classForName(javaTypeClassName), Resources.classForName(typeHandlerClassName));
    }

    public void register(Class<?> javaTypeClass, Class<?> typeHandlerClass) {
        register(javaTypeClass, getInstance(javaTypeClass, typeHandlerClass));
    }

    public void register(Class<?> javaTypeClass, JdbcType jdbcType, Class<?> typeHandlerClass)
    {
        register(javaTypeClass, jdbcType, getInstance(javaTypeClass, typeHandlerClass));
    }

    public <T> TypeHandler<T> getInstance(Class<?> javaTypeClass, Class<?> typeHandlerClass)
    {
        if (javaTypeClass != null)
            try {
                Constructor c = typeHandlerClass.getConstructor(new Class[] { Class.class });
                return (TypeHandler)c.newInstance(new Object[] { javaTypeClass });
            } catch (NoSuchMethodException localNoSuchMethodException) {
            }
            catch (Exception e) {
                throw new TypeException("Failed invoking constructor for handler " + typeHandlerClass, e);
            }
        try
        {
            Constructor c = typeHandlerClass.getConstructor(new Class[0]);
            return (TypeHandler)c.newInstance(new Object[0]);
        } catch (Exception e) {
            throw new TypeException("Unable to find a usable constructor for " + typeHandlerClass, e);
        }
    }

    public void register(String packageName)
    {
//        ResolverUtil resolverUtil = new ResolverUtil();
//        resolverUtil.find(new ResolverUtil.IsA(TypeHandler.class), packageName);
//        Set<Class> handlerSet = resolverUtil.getClasses();
        Set<Class> handlerSet=null;
        for (Class type : handlerSet)
        {
            if ((!type.isAnonymousClass()) && (!type.isInterface()) && (!Modifier.isAbstract(type.getModifiers())))
                register(type);
        }
    }

    public Collection<TypeHandler<?>> getTypeHandlers()
    {
        return Collections.unmodifiableCollection(this.ALL_TYPE_HANDLERS_MAP.values());
    }
}
