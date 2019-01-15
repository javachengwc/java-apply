package com.esearch6.util;

import com.esearch6.annotation.IndexKey;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class IndexKeyUtil {

    public static String [] tipIndexKey(Class beanClass) {

        List<String> indexKeyList = new ArrayList<String>();

        Class curClass = beanClass;
        while(curClass!=null && curClass!=Object.class) {
            Field[] fields = curClass.getDeclaredFields();
            if(fields!=null) {
                for(Field field:fields) {
                    IndexKey annotation = field.getAnnotation(IndexKey.class);
                    if(annotation!=null)
                    {
                        String fieldName= field.getName();
                        indexKeyList.add(fieldName);
                    }
                }
            }
            curClass = curClass.getSuperclass();
        }

        String [] arrays = indexKeyList.toArray(new String[indexKeyList.size()]);
        return arrays;
    }
}
