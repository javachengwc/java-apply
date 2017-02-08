package com.util.enh;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class BeanCopyUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanCopyUtil.class);

    private BeanCopyUtil(){}

    public static void copyProperties(Object target, Object src) {

        Map<String, Method> targetMtdMap = new HashMap<String, Method>();
        for (Method targetMtd : target.getClass().getDeclaredMethods()) {
            if (targetMtd.getName().startsWith("set")) {
                targetMtdMap.put(targetMtd.getName().substring(targetMtd.getName().indexOf("set") + 3).toLowerCase(), targetMtd);
            }
        }

        Map<String, Method> srcMtdMap =  new HashMap<String, Method>();
        for (Method srcMtd : src.getClass().getDeclaredMethods()) {
            String mtdKey = null;
            if (srcMtd.getName().startsWith("get")) {
                mtdKey = srcMtd.getName().substring(3).toLowerCase();
            } else if (srcMtd.getName().startsWith("is")) {
                mtdKey = srcMtd.getName().substring(2).toLowerCase();
            }

            if (mtdKey != null) {
                srcMtdMap.put(mtdKey, srcMtd);
            }
        }

        for (String mtdKey : srcMtdMap.keySet()) {
            try {
                if (targetMtdMap.containsKey(mtdKey)) {
                    //得到setter方法的参数
                    Type[] ts = targetMtdMap.get(mtdKey).getParameterTypes();
                    String xclass = ts[0].toString();
                    Object proValue = srcMtdMap.get(mtdKey).invoke(src);
                    if (proValue != null) {
                        try {
                            String vclass = proValue.getClass().getName();
                            if(xclass.equals("class java.lang.Long") && vclass.equals("java.lang.Integer")){
                                targetMtdMap.get(mtdKey).invoke(target, (long)Integer.parseInt(proValue.toString()));
                            }else {
                                targetMtdMap.get(mtdKey).invoke(target, proValue);
                            }
                        } catch (IllegalAccessException e) {
                            LOGGER.error("copyProperties is error, ", e);
                        } catch (InvocationTargetException e) {
                            LOGGER.error("copyProperties is error,", e);
                        } catch (IllegalArgumentException e){
                            LOGGER.error("copyProperties is error," + mtdKey);
                        }
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("copyProperties is error", ex);
            }
        }

    }

}
