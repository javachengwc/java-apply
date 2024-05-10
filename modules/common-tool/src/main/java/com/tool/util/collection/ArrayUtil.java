package com.tool.util.collection;

public class ArrayUtil {

    public static boolean containsArray(Object[] objects, Object[] array) {
        if (objects.length >= 0 && array.length == 0) {
            return true;
        }
        int match = 0;
        for (Object obj : array) {
            if (org.apache.commons.lang3.ArrayUtils.contains(objects, obj))
                match++;
        }
        if (match == array.length)
            return true;
        else
            return false;
    }
}
