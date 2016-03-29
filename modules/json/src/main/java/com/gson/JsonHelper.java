package com.gson;

import com.google.gson.Gson;

import java.util.List;
import java.util.Map;

public class JsonHelper {

    /**
     * 将传入的json字符串解析为Map集合
     * @param jsonStr
     * @return
     */
    public static <K,T> Map<K, T> json2Map(String jsonStr) {
        Map<K, T> ObjectMap = null;
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<Map<K,T>>() {}.getType();
        ObjectMap = gson.fromJson(jsonStr, type);
        return ObjectMap;
    }

    public static <T> T json2Bean(String jsonStr,Class<T> clazz)
    {
        T t =null ;
        Gson gson = new Gson();
        t= gson.fromJson(jsonStr, clazz);
        return t;
    }

    /**
     * 将传入的json字符串转换为元素为map集合的List集合
     * @param jsonArrStr
     * @return
     */
    public static List<Map<String, Object>> json2ListMap(String jsonArrStr) {
        List<Map<String, Object>> jsonObjList =null;
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Map<String,Object>>>() {}.getType();
        jsonObjList = gson.fromJson(jsonArrStr, type);
        return jsonObjList;
    }

//有问题，得到的结果遍历的时候会抛错
//    public static <T> List<T> json2ListBean(String jsonArrayStr,Class<T> clazz) {
//        List<T> objectList = null;
//        Gson gson = new Gson();
//        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<T>>() {}.getType();
//        objectList = gson.fromJson(jsonArrayStr,type);
//        return objectList;
//    }

    public static <K,T> String map2Json(Map<K,T> map)
    {
        Gson gson = new Gson();
        String rt =gson.toJson(map);
        return rt;
    }

    public static <T> String bean2Json(T bean)
    {
        Gson gson = new Gson();
        String rt =gson.toJson(bean);
        return rt;
    }

    public static <T> String listBean2Json(List<T> list)
    {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<T>>() {}.getType();
        String rt=gson.toJson(list,type);
        return rt;
    }

    public static <K,T> String listMap2Json(List<Map<K,T>> list)
    {
        Gson gson = new Gson();
        java.lang.reflect.Type type = new com.google.gson.reflect.TypeToken<List<Map<K,T>>>() {}.getType();
        String rt=gson.toJson(list,type);
        return rt;
    }
}
