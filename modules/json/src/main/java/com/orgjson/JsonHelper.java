package com.orgjson;

import java.lang.reflect.Field;
import java.util.*;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper {

    /**
     * 将json字符串转换为List集合
     */
    public static List<Map<String, Object>> json2ListMap(String jsonArrStr) {
        List<Map<String, Object>> jsonList = new ArrayList<Map<String, Object>>();
        JSONArray jsonArr = null;
        try {
            jsonArr = new JSONArray(jsonArrStr);
            jsonList = (List<Map<String, Object>>)jsonToList(jsonArr);
        } catch (JSONException e) {
            System.out.println("Json字符串转换异常！");
            e.printStackTrace();
        }
        return jsonList;
    }

    public static List<?> jsonToList(JSONArray jsonArr)
            throws JSONException {
        List<Object> jsonToMapList = new ArrayList<Object>();
        for (int i = 0; i < jsonArr.length(); i++) {
            Object object = jsonArr.get(i);
            if (object instanceof JSONArray) {
                jsonToMapList.add(jsonToList((JSONArray) object));
            } else if (object instanceof JSONObject) {
                jsonToMapList.add(json2Map((JSONObject) object));
            } else {
                jsonToMapList.add(object);
            }
        }
        return jsonToMapList;
    }
    /**
     * json-->map
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Object> json2Map(JSONObject jsonObj)
            throws JSONException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        Iterator<String> jsonKeys = jsonObj.keys();
        while (jsonKeys.hasNext()) {
            String jsonKey = jsonKeys.next();
            Object jsonValObj = jsonObj.get(jsonKey);
            if (jsonValObj instanceof JSONArray) {
                jsonMap.put(jsonKey,jsonToList((JSONArray) jsonValObj));
            } else if (jsonValObj instanceof JSONObject) {
                jsonMap.put(jsonKey,json2Map((JSONObject) jsonValObj));
            } else {
                jsonMap.put(jsonKey, jsonValObj);
            }
        }
        return jsonMap;
    }

    /**
     * 给对象注入值
     * @param obj
     * @param map
     */
    public static <T> T injectValue(T obj,Map<String,Object> map)
    {
        if(map==null || map.size()<=0)
        {
            return obj;
        }
        try {

            Class clazz = obj.getClass();
            while(clazz!=null && clazz!=Object.class)
            {
                Field[] fields = clazz.getDeclaredFields();
                for(Field field :fields)
                {
                    String key = field.getName();
                    Object valueObj = map.get(key);
                    if(valueObj!=null)
                    {
                        BeanUtils.setProperty(obj, field.getName(), valueObj);
                    }
                }
                clazz = clazz.getSuperclass();
            }

        } catch (Exception e) {
        }
        return obj;
    }

    /**json-->bean**/
    public  static <T> T  json2Bean(JSONObject jsonObj,Class<T> clazz) throws Exception {
        if(jsonObj==null)
        {
            return null;
        }
        Map<String, Object> map = json2Map(jsonObj);
        T obj = clazz.newInstance();
        return injectValue(obj,map);
    }

    /**json-->listbean**/
    public static <T> List<T> json2ListBean(JSONArray jsonArray,Class<T> clazz) throws Exception {

        if(jsonArray==null)
        {
            return null;
        }
        if(jsonArray.length()<=0)
        {
            return Collections.EMPTY_LIST;
        }

        List<T> list = new LinkedList<T>();

        for(int i=0;i<jsonArray.length();i++)
        {
            JSONObject obj = jsonArray.getJSONObject(i);
            T t = json2Bean(obj,clazz);
            list.add(t);
        }
        return list;
    }
}