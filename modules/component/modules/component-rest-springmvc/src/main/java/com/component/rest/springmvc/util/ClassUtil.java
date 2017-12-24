package com.component.rest.springmvc.util;

import java.math.BigDecimal;

public class ClassUtil {

    public static boolean isSimpleClass(Class clazz) {

        if(clazz==String.class)
        {
            return true;
        }
        if(clazz==boolean.class || clazz==Boolean.class)
        {
            return true;
        }
        if(clazz==short.class || clazz==Short.class)
        {
            return true;
        }
        if(clazz==int.class || clazz==Integer.class)
        {
            return true;
        }
        if(clazz==long.class || clazz==Long.class)
        {
            return true;
        }
        if(clazz==float.class || clazz==Float.class)
        {
            return true;
        }
        if(clazz==double.class || clazz==Double.class)
        {
            return true;
        }
        if(clazz==byte.class || clazz==Byte.class )
        {
            return true;
        }
        if(clazz==char.class || clazz==Character.class)
        {
            return true;
        }
        if(clazz==Number.class)
        {
            return true;
        }
        if(clazz==BigDecimal.class)
        {
            return true;
        }
        return false;
    }
}
