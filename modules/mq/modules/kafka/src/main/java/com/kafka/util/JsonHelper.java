package com.kafka.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.List;
import java.util.Map;

public class JsonHelper {

    public static <T> T json2Bean(String jsonStr,Class<T> clazz)
    {
        return JSON.parseObject(jsonStr,clazz);
    }

    public static <T> List<T> json2List(String jsonStr,Class<T> clazz)
    {
        return JSON.parseArray(jsonStr,clazz);
    }

    public static <K,T> Map<K,T> json2Map(String jsonStr){
        Map<K,T> map = (Map<K,T>)JSON.parse(jsonStr);
        return  map;
    }

    public static <K,T> List<Map<K,T>> json2ListMap(String jsonStr)
    {
        List<Map<K,T>> list = (List<Map<K,T>>)JSON.parse(jsonStr);
        return  list;
    }

    public static JSONObject strToJson(String jsonStr)
    {
        return JSON.parseObject(jsonStr);
    }

    public static JSONArray  strToJsonArray(String jsonStr)
    {
        return JSON.parseArray(jsonStr);
    }

    public static <K,T> String map2JsonStr(Map<K,T> map)
    {
        return JSON.toJSONString(map);
    }

    public static <T> String bean2JsonStr(T bean)
    {
        return JSON.toJSONString(bean);
    }

    public static <T> String list2JsonStr(List<T> list)
    {
        return JSON.toJSONString(list);
    }

    public static <K,T> String listMap2JsonStr(List<Map<K,T>> list)
    {
        return JSON.toJSONString(list);
    }
}
