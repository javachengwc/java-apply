package com.spring.pseudocode.beans.factory.support;

import com.spring.pseudocode.beans.factory.ObjectFactory;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Set;

public abstract class AutowireUtils {

    public static void sortConstructors(Constructor<?>[] constructors)
    {
        Arrays.sort(constructors, new Comparator<Constructor<?>>()
        {
            public int compare(Constructor<?> c1, Constructor<?> c2) {
                boolean p1 = Modifier.isPublic(c1.getModifiers());
                boolean p2 = Modifier.isPublic(c2.getModifiers());
                if (p1 != p2) {
                    return p1 ? -1 : 1;
                }
                int c1pl = c1.getParameterTypes().length;
                int c2pl = c2.getParameterTypes().length;
                return c1pl > c2pl ? -1 : c1pl < c2pl ? 1 : 0;
            }
        });
    }

    public static void sortFactoryMethods(Method[] factoryMethods)
    {
        Arrays.sort(factoryMethods, new Comparator<Method>()
        {
            public int compare(Method fm1, Method fm2) {
                boolean p1 = Modifier.isPublic(fm1.getModifiers());
                boolean p2 = Modifier.isPublic(fm2.getModifiers());
                if (p1 != p2) {
                    return p1 ? -1 : 1;
                }
                int c1pl = fm1.getParameterTypes().length;
                int c2pl = fm2.getParameterTypes().length;
                return c1pl > c2pl ? -1 : c1pl < c2pl ? 1 : 0;
            }
        });
    }

    public static boolean isExcludedFromDependencyCheck(PropertyDescriptor pd)
    {
        Method wm = pd.getWriteMethod();
        if (wm == null) {
            return false;
        }
        if (!wm.getDeclaringClass().getName().contains("$$"))
        {
            return false;
        }

        Class superclass = wm.getDeclaringClass().getSuperclass();
        return !ClassUtils.hasMethod(superclass, wm.getName(), wm.getParameterTypes());
    }

    public static boolean isSetterDefinedInInterface(PropertyDescriptor pd, Set<Class<?>> interfaces)
    {
        Method setter = pd.getWriteMethod();
        Class targetClass;
        if (setter != null) {
            targetClass = setter.getDeclaringClass();
            for (Class ifc : interfaces) {
                if ((ifc.isAssignableFrom(targetClass)) &&
                        (ClassUtils.hasMethod(ifc, setter
                                .getName(), setter.getParameterTypes()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Object resolveAutowiringValue(Object autowiringValue, Class<?> requiredType)
    {
        if (((autowiringValue instanceof ObjectFactory)) && (!requiredType.isInstance(autowiringValue))) {
            ObjectFactory factory = (ObjectFactory)autowiringValue;
            if (((autowiringValue instanceof Serializable)) && (requiredType.isInterface())) {
                autowiringValue = Proxy.newProxyInstance(requiredType.getClassLoader(), new Class[] { requiredType },
                        new ObjectFactoryDelegatingInvocationHandler(factory));
            }
            else
            {
                return factory.getObject();
            }
        }
        return autowiringValue;
    }

    private static class ObjectFactoryDelegatingInvocationHandler implements InvocationHandler, Serializable
    {
        private final ObjectFactory<?> objectFactory;

        public ObjectFactoryDelegatingInvocationHandler(ObjectFactory<?> objectFactory)
        {
            this.objectFactory = objectFactory;
        }

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            String methodName = method.getName();
            if (methodName.equals("equals"))
            {
                return Boolean.valueOf(proxy == args[0]);
            }
            if (methodName.equals("hashCode"))
            {
                return Integer.valueOf(System.identityHashCode(proxy));
            }
            if (methodName.equals("toString"))
                return this.objectFactory.toString();
            try
            {
                return method.invoke(this.objectFactory.getObject(), args);
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }
}
