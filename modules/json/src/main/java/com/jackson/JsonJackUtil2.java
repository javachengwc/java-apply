package com.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 使用Jackson 2.x版本的工具类
 */
public class JsonJackUtil2 {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T json2Obj(String json, Class<T> cls) {
        T pojo = null;
        try {
            pojo = objectMapper.readValue(json, cls);
        } catch (IOException e) {
            return null;
        }
        return pojo;
    }

    public static <T> T json2Obj(String json, Class<T> outerClazz, Class<?>... innerClazz) {
        T pojo = null;
        JavaType javaType = objectMapper.getTypeFactory().constructParametrizedType(outerClazz, outerClazz,innerClazz);
        try {
           pojo = objectMapper.readValue(json, javaType);
        } catch (IOException e) {
            return null;
        }
        return pojo;
    }

    public static String obj2Json(Object pojo) {
        String json = null;
        try {
            json = objectMapper.writeValueAsString(pojo);
        } catch (Exception  e) {
            e.printStackTrace();
        }
        return json;
    }

    public static void main(String  [] args) {
        Map<String,Object> obj=new HashMap<String,Object>();
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("aa",obj);
        String str = obj2Json(map);
        System.out.println(str);
    }
}
