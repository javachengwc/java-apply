package com.mybatis.pseudocode.mybatis.binding;


import com.mybatis.pseudocode.mybatis.session.SqlSession;
import org.springframework.lang.UsesJava7;

import java.io.Serializable;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Map;

//mapper代理类
public class MapperProxy<T> implements InvocationHandler, Serializable
{
    private static final long serialVersionUID = -6398559729838L;
    private final SqlSession sqlSession;
    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache;

    public MapperProxy(SqlSession sqlSession, Class<T> mapperInterface, Map<Method, MapperMethod> methodCache)
    {
        this.sqlSession = sqlSession;
        this.mapperInterface = mapperInterface;
        this.methodCache = methodCache;
    }

    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
    {
        try {
            if (Object.class.equals(method.getDeclaringClass()))
                return method.invoke(this, args);
            if (isDefaultMethod(method))
                return invokeDefaultMethod(proxy, method, args);
        }
        catch (Throwable t) {
           // throw ExceptionUtil.unwrapThrowable(t);
            throw t;
        }
        MapperMethod mapperMethod = cachedMapperMethod(method);
        //具体mapper方法调用入口
        return mapperMethod.execute(this.sqlSession, args);
    }

    private MapperMethod cachedMapperMethod(Method method) {
        MapperMethod mapperMethod = (MapperMethod)this.methodCache.get(method);
        if (mapperMethod == null) {
            mapperMethod = new MapperMethod(this.mapperInterface, method, this.sqlSession.getConfiguration());
            this.methodCache.put(method, mapperMethod);
        }
        return mapperMethod;
    }

    @UsesJava7
    private Object invokeDefaultMethod(Object proxy, Method method, Object[] args) throws Throwable
    {
        Constructor constructor = MethodHandles.Lookup.class.getDeclaredConstructor(new Class[] { Class.class, Integer.TYPE });

        if (!constructor.isAccessible()) {
            constructor.setAccessible(true);
        }
        Class declaringClass = method.getDeclaringClass();
        return
                ((MethodHandles.Lookup)constructor.newInstance(new Object[] { declaringClass, Integer.valueOf(15) }))
                        .unreflectSpecial(method, declaringClass)
                        .bindTo(proxy).invokeWithArguments(args);
    }

    private boolean isDefaultMethod(Method method)
    {
        if ((method.getModifiers() & 0x409) == 1);
        return method.getDeclaringClass().isInterface();
    }
}
