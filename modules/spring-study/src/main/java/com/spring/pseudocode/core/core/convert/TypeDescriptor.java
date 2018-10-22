package com.spring.pseudocode.core.core.convert;

import com.spring.pseudocode.core.core.MethodParameter;
import org.springframework.core.ResolvableType;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//类型描述符
public class TypeDescriptor implements Serializable
{
    static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

    private static final boolean streamAvailable = ClassUtils.isPresent("java.util.stream.Stream", TypeDescriptor.class.getClassLoader());

    private static final Map<Class<?>, TypeDescriptor> commonTypesCache = new HashMap(18);

    private static final Class<?>[] CACHED_COMMON_TYPES =
            { Boolean.TYPE, Boolean.class,
              Byte.TYPE, Byte.class,
              Character.TYPE, Character.class,
              Double.TYPE, Double.class,
              Integer.TYPE, Integer.class,
              Long.TYPE, Long.class,
              Float.TYPE, Float.class,
              Short.TYPE, Short.class,
              String.class, Object.class };
    private Class<?> type;
    private ResolvableType resolvableType;

    static
    {
        for (Class preCachedClass : CACHED_COMMON_TYPES)
            commonTypesCache.put(preCachedClass, valueOf(preCachedClass));
    }

    public TypeDescriptor(MethodParameter methodParameter)
    {
        //this.resolvableType = ResolvableType.forMethodParameter(methodParameter);
        //this.type = this.resolvableType.resolve(methodParameter.getParameterType());
//        this.annotatedElement =new AnnotatedElementAdapter(methodParameter.getParameterIndex() == -1
//        ? methodParameter.getMethodAnnotations() : methodParameter.getParameterAnnotations());
    }

    public TypeDescriptor(Field field)
    {
        this.resolvableType = ResolvableType.forField(field);
        this.type = this.resolvableType.resolve(field.getType());
    }

    public TypeDescriptor(Property property)
    {
        //this.resolvableType = ResolvableType.forMethodParameter(property.getMethodParameter());
        this.type = this.resolvableType.resolve(property.getType());
    }

    protected TypeDescriptor(ResolvableType resolvableType, Class<?> type, Annotation[] annotations)
    {
        this.resolvableType = resolvableType;
        this.type = (type != null ? type : resolvableType.resolve(Object.class));
    }

    public Class<?> getObjectType()
    {
        return ClassUtils.resolvePrimitiveIfNecessary(getType());
    }

    public Class<?> getType()
    {
        return this.type;
    }

    public ResolvableType getResolvableType()
    {
        return this.resolvableType;
    }

    public Object getSource()
    {
        return this.resolvableType != null ? this.resolvableType.getSource() : null;
    }

    public TypeDescriptor narrow(Object value)
    {
        if (value == null) {
            return this;
        }
        ResolvableType narrowed = ResolvableType.forType(value.getClass(), getResolvableType());
        return new TypeDescriptor(narrowed, value.getClass(), getAnnotations());
    }

    public TypeDescriptor upcast(Class<?> superType)
    {
        if (superType == null) {
            return null;
        }
        return new TypeDescriptor(getResolvableType().as(superType), superType, getAnnotations());
    }

    public String getName()
    {
        return ClassUtils.getQualifiedName(getType());
    }

    public boolean isPrimitive()
    {
        return getType().isPrimitive();
    }

    public Annotation[] getAnnotations()
    {
        //return this.annotatedElement.getAnnotations();
        return null;
    }

    public boolean hasAnnotation(Class<? extends Annotation> annotationType)
    {
        return false;
    }

    public boolean isCollection()
    {
        return Collection.class.isAssignableFrom(getType());
    }

    public boolean isArray()
    {
        return getType().isArray();
    }

    public boolean isMap()
    {
        return Map.class.isAssignableFrom(getType());
    }

    public TypeDescriptor getMapKeyTypeDescriptor()
    {
        return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 0 }));
    }

    public TypeDescriptor getMapKeyTypeDescriptor(Object mapKey)
    {
        return narrow(mapKey, getMapKeyTypeDescriptor());
    }

    public TypeDescriptor getMapValueTypeDescriptor()
    {
        return getRelatedIfResolvable(this, getResolvableType().asMap().getGeneric(new int[] { 1 }));
    }

    public TypeDescriptor getMapValueTypeDescriptor(Object mapValue)
    {
        return narrow(mapValue, getMapValueTypeDescriptor());
    }

    private TypeDescriptor narrow(Object value, TypeDescriptor typeDescriptor) {
        if (typeDescriptor != null) {
            return typeDescriptor.narrow(value);
        }
        if (value != null) {
            return narrow(value);
        }
        return null;
    }

    private boolean annotationsMatch(TypeDescriptor otherDesc)
    {
        Annotation[] anns = getAnnotations();
        Annotation[] otherAnns = otherDesc.getAnnotations();
        if (anns == otherAnns) {
            return true;
        }
        if (anns.length != otherAnns.length) {
            return false;
        }
        if (anns.length > 0) {
            for (int i = 0; i < anns.length; i++) {
                if (!annotationEquals(anns[i], otherAnns[i])) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean annotationEquals(Annotation ann, Annotation otherAnn)
    {
        return (ann == otherAnn) || ((ann.getClass() == otherAnn.getClass()) && (ann.equals(otherAnn)));
    }

    public static TypeDescriptor forObject(Object source)
    {
        return source != null ? valueOf(source.getClass()) : null;
    }

    public static TypeDescriptor valueOf(Class<?> type)
    {
        if (type == null) {
            type = Object.class;
        }
        TypeDescriptor desc = (TypeDescriptor)commonTypesCache.get(type);
        return desc != null ? desc : new TypeDescriptor(ResolvableType.forClass(type), null, null);
    }

    public static TypeDescriptor collection(Class<?> collectionType, TypeDescriptor elementTypeDescriptor)
    {
        if (!Collection.class.isAssignableFrom(collectionType)) {
            throw new IllegalArgumentException("Collection type must be a [java.util.Collection]");
        }
        ResolvableType element = elementTypeDescriptor != null ? elementTypeDescriptor.resolvableType : null;
        return new TypeDescriptor(ResolvableType.forClassWithGenerics(collectionType, new ResolvableType[] { element }), null, null);
    }

    public static TypeDescriptor map(Class<?> mapType, TypeDescriptor keyTypeDescriptor, TypeDescriptor valueTypeDescriptor)
    {
        if (!Map.class.isAssignableFrom(mapType)) {
            throw new IllegalArgumentException("Map type must be a [java.util.Map]");
        }
        ResolvableType key = keyTypeDescriptor != null ? keyTypeDescriptor.resolvableType : null;
        ResolvableType value = valueTypeDescriptor != null ? valueTypeDescriptor.resolvableType : null;
        return new TypeDescriptor(ResolvableType.forClassWithGenerics(mapType, new ResolvableType[] { key, value }), null, null);
    }

    public static TypeDescriptor array(TypeDescriptor elementTypeDescriptor)
    {
        if (elementTypeDescriptor == null) {
            return null;
        }
        return new TypeDescriptor(ResolvableType.forArrayComponent(elementTypeDescriptor.resolvableType),
                null, elementTypeDescriptor.getAnnotations());
    }

    public static TypeDescriptor nested(MethodParameter methodParameter, int nestingLevel)
    {
        //...
        return nested(new TypeDescriptor(methodParameter), nestingLevel);
    }

    public static TypeDescriptor nested(Field field, int nestingLevel)
    {
        return nested(new TypeDescriptor(field), nestingLevel);
    }

    public static TypeDescriptor nested(Property property, int nestingLevel)
    {
        return nested(new TypeDescriptor(property), nestingLevel);
    }

    private static TypeDescriptor nested(TypeDescriptor typeDescriptor, int nestingLevel) {
        ResolvableType nested = typeDescriptor.resolvableType;
        for (int i = 0; i < nestingLevel; i++) {
            if (Object.class == nested.getType())
            {
                continue;
            }

            nested = nested.getNested(2);
        }

        if (nested == ResolvableType.NONE) {
            return null;
        }
        return getRelatedIfResolvable(typeDescriptor, nested);
    }

    private static TypeDescriptor getRelatedIfResolvable(TypeDescriptor source, ResolvableType type) {
        if (type.resolve() == null) {
            return null;
        }
        return new TypeDescriptor(type, null, source.getAnnotations());
    }

}
