package com.spring.pseudocode.core.core.convert;

import com.spring.pseudocode.core.core.MethodParameter;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

public final class Property
{
    private static Map<Property, Annotation[]> annotationCache = new ConcurrentReferenceHashMap();
    private final Class<?> objectType;
    private final Method readMethod;
    private final Method writeMethod;
    private String name;
    private final MethodParameter methodParameter;
    private Annotation[] annotations;

    public Property(Class<?> objectType, Method readMethod, Method writeMethod)
    {
        this(objectType, readMethod, writeMethod, null);
    }

    public Property(Class<?> objectType, Method readMethod, Method writeMethod, String name) {
        this.objectType = objectType;
        this.readMethod = readMethod;
        this.writeMethod = writeMethod;
        this.methodParameter = resolveMethodParameter();
        //this.name = (name != null ? name : resolveName());
    }

    public Class<?> getObjectType()
    {
        return this.objectType;
    }

    public String getName()
    {
        return this.name;
    }

    public Class<?> getType()
    {
        return this.methodParameter.getParameterType();
    }

    public Method getReadMethod()
    {
        return this.readMethod;
    }

    public Method getWriteMethod()
    {
        return this.writeMethod;
    }

    MethodParameter getMethodParameter()
    {
        return this.methodParameter;
    }

    Annotation[] getAnnotations() {
        if (this.annotations == null) {
            this.annotations = resolveAnnotations();
        }
        return this.annotations;
    }

    private MethodParameter resolveMethodParameter()
    {
        MethodParameter read = resolveReadMethodParameter();
        MethodParameter write = resolveWriteMethodParameter();
        if (write == null) {
            if (read == null) {
                throw new IllegalStateException("Property is neither readable nor writeable");
            }
            return read;
        }
        if (read != null) {
            Class readType = read.getParameterType();
            Class writeType = write.getParameterType();
            if ((!writeType.equals(readType)) && (writeType.isAssignableFrom(readType))) {
                return read;
            }
        }
        return write;
    }

    private MethodParameter resolveReadMethodParameter() {
        if (getReadMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getReadMethod(), -1));
    }

    private MethodParameter resolveWriteMethodParameter() {
        if (getWriteMethod() == null) {
            return null;
        }
        return resolveParameterType(new MethodParameter(getWriteMethod(), 0));
    }

    private MethodParameter resolveParameterType(MethodParameter parameter)
    {
        //GenericTypeResolver.resolveParameterType(parameter, getObjectType());
        return parameter;
    }

    private Annotation[] resolveAnnotations() {
        Annotation[] annotations = (Annotation[])annotationCache.get(this);
        if (annotations == null) {
            Map annotationMap = new LinkedHashMap();
            addAnnotationsToMap(annotationMap, getReadMethod());
            addAnnotationsToMap(annotationMap, getWriteMethod());
            addAnnotationsToMap(annotationMap, getField());
            annotations = (Annotation[])annotationMap.values().toArray(new Annotation[annotationMap.size()]);
            annotationCache.put(this, annotations);
        }
        return annotations;
    }

    private void addAnnotationsToMap(Map<Class<? extends Annotation>, Annotation> annotationMap, AnnotatedElement object)
    {
        if (object == null)
        {
            return;
        }
        for (Annotation annotation : object.getAnnotations()) {
            annotationMap.put(annotation.annotationType(), annotation);
        }
    }

    private Field getField()
    {
        String name = getName();
        if (!StringUtils.hasLength(name)) {
            return null;
        }
        Class declaringClass = declaringClass();
        Field field = ReflectionUtils.findField(declaringClass, name);
        if (field == null)
        {
            field = ReflectionUtils.findField(declaringClass, name.substring(0, 1).toLowerCase() + name.substring(1));
            if (field == null) {
                field = ReflectionUtils.findField(declaringClass, name.substring(0, 1).toUpperCase() + name.substring(1));
            }
        }
        return field;
    }

    private Class<?> declaringClass() {
        if (getReadMethod() != null) {
            return getReadMethod().getDeclaringClass();
        }
        return getWriteMethod().getDeclaringClass();
    }

    public int hashCode()
    {
        return ObjectUtils.nullSafeHashCode(this.objectType) * 31 + ObjectUtils.nullSafeHashCode(this.name);
    }
}
