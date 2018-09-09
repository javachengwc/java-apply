package com.mybatis.pseudocode.mybatis.type;


import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class TypeReference<T>
{
    private final Type rawType;

    protected TypeReference()
    {
        this.rawType = getSuperclassTypeParameter(getClass());
    }

    public Type getSuperclassTypeParameter(Class<?> clazz) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if ((genericSuperclass instanceof Class))
        {
            if (TypeReference.class != genericSuperclass) {
                return getSuperclassTypeParameter(clazz.getSuperclass());
            }

            throw new TypeException("'" + getClass() + "' extends TypeReference but misses the type parameter. " +
                    "Remove the extension or add a type parameter to it.");
        }

        Type rawType = ((ParameterizedType)genericSuperclass).getActualTypeArguments()[0];

        if ((rawType instanceof ParameterizedType)) {
            rawType = ((ParameterizedType)rawType).getRawType();
        }

        return rawType;
    }

    public final Type getRawType() {
        return this.rawType;
    }

    public String toString()
    {
        return this.rawType.toString();
    }
}
