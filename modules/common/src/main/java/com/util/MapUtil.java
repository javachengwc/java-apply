package com.util;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.*;

public class MapUtil {

    private static Logger logger = LoggerFactory.getLogger(MapUtil.class);

    private static Long zeroLong =0l;

    /**
     * 给List对象写入值
     * @param resultData   结果list
     * @param queryData    合并的list数据
     * @param clazz        对象类型
     * @return
     */
    public static <T> List<T> injectListValue(List<T> resultData,List<Map<String, Object>> queryData,Class clazz) throws Exception
    {
        return injectListValue(resultData,queryData,clazz,true);
    }

    /**
     * 给List对象写入值，如果莫有匹配的ele,canAdd为true就增加
     * @param resultData   结果list
     * @param queryData    合并的list数据
     * @param clazz        对象类型
     * @param canAdd       没找到匹配项，是否添加
     * @return
     */
    public static <T> List<T> injectListValue( List<T> resultData,List<Map<String, Object>> queryData,Class clazz ,boolean canAdd) throws Exception
    {

        if(queryData==null || queryData.size()<=0)
        {
            return resultData;
        }
        if(resultData==null)
        {
            resultData = new ArrayList<T>();
        }
        for(Map<String,Object> data:queryData)
        {
            T matchedObj=null;
            for(T bvo:resultData)
            {
                if(isSuit(bvo,data))
                {
                    matchedObj=bvo;
                    break;
                }
            }
            if(matchedObj==null && canAdd) {
                matchedObj = (T)clazz.newInstance();
                resultData.add(matchedObj);
            }
            if(matchedObj!=null) {
                injectValue(matchedObj, data);
            }
        }
        return resultData;
    }


    /**
     * 对象是否与map数据是匹配的，根据MapId字段的值判断
     * @return
     */
    public static boolean isSuit(Object obj,Map<String,Object> map,String ... priKeys)
    {
        if(obj==null || map==null || map.size()<=0 )
        {
            return false;
        }
        Map<String,Object> keyFieldValueMap=new HashMap<String,Object>();
        try {

            Class clazz = obj.getClass();
            while(clazz!=null && clazz!=Object.class)
            {
                Field[] fields = clazz.getDeclaredFields();
                for(Field field :fields)
                {
                    String fieldName= field.getName();
                    if(priKeys!=null && priKeys.length>0)
                    {
                       for(String key:priKeys)
                       {
                           if(key.equals(fieldName))
                           {
                               Object fieldValue = BeanUtils.getProperty(obj, fieldName);
                               keyFieldValueMap.put(key,fieldValue);
                           }
                       }

                    }
                }
                clazz = clazz.getSuperclass();
            }
            if(keyFieldValueMap!=null && keyFieldValueMap.size()>0)
            {

                for(String key:keyFieldValueMap.keySet())
                {
                    Object fieldVal=  keyFieldValueMap.get(key);
                    Object mapVal = map.get(key);
                    if(fieldVal==null && mapVal==null )
                    {
                        continue;
                    }
                    if(fieldVal==null || mapVal==null )
                    {
                        return false;
                    }
                    if(fieldVal.equals(mapVal) || fieldVal.toString().equals(mapVal.toString()))
                    {
                        continue;
                    }else
                    {
                        return false;
                    }
                }
                return true;
            }
            else
            {
                return false;
            }

        } catch (Exception e) {
            logger.error("MapDataUtil isSuit error,obj->\r\n"+obj.toString()+"\r\n,map->"+map.toString(), e);

            return false;
        }

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
                        BeanUtils.setProperty(obj,field.getName(),valueObj);
                    }
                }
                clazz = clazz.getSuperclass();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return obj;
    }

    /**
     * 多行数据映射成一个对象
     * @param resultData     封装数据保存列表
     * @param data           数据列表
     * @param mapping        映射关系  如：newUserNoCustomUv,count->maternal=0,visitors=new
     * @param clazz          封装数据的对象类型
     * @param priKey         表示数据分组的key
     * @return               映射注入后的List<T></>
     */
    public static <T> List<T> injectMapValue(List<T> resultData,List<Map<String,Object>> data,Map<String,Map<String,String>> mapping,Class clazz ,String priKey) throws Exception
    {

        Map<String,List<Map<String,Object>>> mapResult = splitData(data,priKey);
        if(mapResult==null || mapResult.size()<=0)
        {
            return resultData;
        }
        if(resultData==null)
        {
            resultData = new ArrayList<T>();
        }
        for(String key:mapResult.keySet())
        {
            HashMap<String,Object> tmpMap= new HashMap<String,Object>(1);
            tmpMap.put(priKey,key);
            T matchedObj =null;
            for(T bvo:resultData)
            {
                if(isSuit(bvo,tmpMap))
                {
                    matchedObj=bvo;
                    break;
                }
            }
            if(matchedObj==null) {
                matchedObj = (T)clazz.newInstance();
                resultData.add(matchedObj);
            }
            if(matchedObj!=null) {
                injectMapValue(matchedObj, mapResult.get(key),mapping);
            }

        }
        return resultData;
    }

    public static Map<String,List<Map<String,Object>>> splitData(List<Map<String,Object>> data,String priKey)
    {

        if(data==null || data.size()<=0)
        {
            return null;
        }
        Map<String,List<Map<String,Object>>> mapResult = new HashMap<String,List<Map<String,Object>>>();
        for(Map<String,Object> perMap:data)
        {
            Object keyObj = perMap.get(priKey);
            if(keyObj==null)
            {
                continue;
            }
            String key = keyObj.toString();
            List<Map<String,Object>> list=mapResult.get(key);
            if(list==null)
            {
                 list = new ArrayList<Map<String,Object>>();
                 mapResult.put(key,list);
            }
            list.add(perMap);
        }

        return mapResult;
    }

    public static <T> T injectMapValue(T obj,List<Map<String,Object>> data,Map<String,Map<String,String>> mapping) throws Exception
    {
        if(data==null || data.size()<=0)
        {
            return obj;
        }
        for(String mapKey:mapping.keySet())
        {
            String entityName = mapKey.split(",")[0];
            String dataKey = mapKey.split(",")[1];
            Map<String,String> mapValue = mapping.get(mapKey);
            if(mapValue==null) {
                if(BeanUtils.getProperty(obj,entityName)==null)
                {
                    BeanUtils.setProperty(obj,entityName,data.get(0).get(dataKey));
                }

            }else {
                for (Map<String, Object> perData : data) {
                    if (isSuit(mapValue, perData)) {
                        BeanUtils.setProperty(obj, entityName, perData.get(dataKey));
                        break;
                    }
                }
            }
        }
        return obj;
    }

    public static boolean isSuit(Map<String,String> mapValue,Map<String,Object> data)
    {

        if( (mapValue==null || mapValue.size()<=0)  || (data==null || data.size()<=0) )
        {
            return false;
        }
        for(String key:mapValue.keySet())
        {
            String value1 = mapValue.get(key);
            Object value2= data.get(key);
            if(value1==null && value2==null )
            {
                continue;
            }
            if(value1==null || value2==null)
            {
                return false;
            }
            if(!value1.equals(value2.toString()))
            {
                return false;
            }
        }
        return true;
    }



    public static <T> T injectNumberAddValue(T obj,Map<String,Object> map)
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
                        Object oldValue = BeanUtils.getProperty(obj,field.getName());
                        if(oldValue!=null)
                        {
                           if( NumberUtil.isNumeric(oldValue.toString()) && NumberUtil.isNumeric(valueObj.toString()))
                           {
                               Long result = Long.parseLong(oldValue.toString())+Long.parseLong(valueObj.toString());
                               valueObj =result;
                           }else
                            if( NumberUtil.isFloatNumeric(oldValue.toString()) && NumberUtil.isFloatNumeric(valueObj.toString()))
                            {
                                Double result = Double.parseDouble(oldValue.toString())+Double.parseDouble(valueObj.toString());
                                valueObj = result;
                            }
                        }
                        BeanUtils.setProperty(obj,field.getName(),valueObj);
                    }
                }
                clazz = clazz.getSuperclass();
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return obj;
    }

    /**
     * 把map数据合并到list
     * @param statisticList  目标list
     * @param list           mapList
     * @param pkMap          key->field  map中key与entity的field的映射  主键映射
     * @param colMap         key->field  合并的值映射
     * @param needAdd        没匹配上是否需要增加新记录到目标list
     * @param <T>
     */
    public static <T> void merginData(List<T> statisticList ,List<Map<String,Object>> list,Map<String,String> pkMap, Map<String,String> colMap,Class clazz,boolean needAdd)
    {
        if(list==null || (pkMap==null || pkMap.size()<=0) || (colMap==null || colMap.size()<=0))
        {
            return;
        }
        for(Map<String,Object> map :list)
        {

            boolean find =false;

            for(T stat:statisticList)
            {
                find= isSuit(stat,map,pkMap);

                if(find)
                {
                    Map<String,Object> mapValue =new HashMap<String,Object>();
                    for(String key:colMap.keySet())
                    {
                        mapValue.put(colMap.get(key), map.get(key));
                    }
                    MapUtil.injectValue(stat, mapValue);
                    break;
                }
            }
            if(!find && needAdd)
            {
                try {
                    T stat = (T)clazz.newInstance();

                    Map<String, Object> mapValue = new HashMap<String, Object>();
                    for (String key : colMap.keySet()) {
                        mapValue.put(colMap.get(key), map.get(key));
                    }
                    for (String key : pkMap.keySet()) {
                        mapValue.put(pkMap.get(key), map.get(key));
                    }
                    MapUtil.injectValue(stat, mapValue);

                    statisticList.add(stat);
                }catch(Exception e)
                {
                    logger.error("merginData newInstance error,", e);
                }

            }

        }
    }

    /**
     * 是否匹配
     */
    public static <T> boolean isSuit(T stat,Map<String,Object> map,Map<String,String> pkMap)
    {

        Map<String,Object> key=new HashMap<String,Object>();
        Map<String,Object> key1 = new HashMap<String,Object>();

        for(String perKey:pkMap.keySet())
        {
            Object obj = map.get(perKey);
            key.put(perKey,obj);
            Object obj1 =null;
            try{
                obj1= BeanUtils.getProperty(stat, pkMap.get(perKey));
            }catch(Exception e)
            {

            }
            key1.put(perKey,obj1);
        }


        if(mapEqual(key,key1))
        {
            return true;
        }

        return false;
    }

    public static boolean mapEqual(Map<String,Object> map,Map<String,Object> map1)
    {
        if(map==null || map1==null)
        {
            return false;
        }
        for(String key:map.keySet())
        {
            Object obj = map.get(key);
            Object obj1 =map1.get(key);
            if(obj==obj1)
            {
                continue;
            }
            String objStr = (obj==null)?null:obj.toString();
            String objStr1 = (obj1==null)?null:obj1.toString();

            if(objStr==null && objStr1==null)
            {
                continue;
            }
            if(objStr!=null && objStr!=null && objStr.equals(objStr1))
            {
                continue;
            }
            return false;
        }
        return true;
    }

    //从map中提取某key的值
    public static Object extractData(Map<String,Object> map ,String key,Class clazz,Integer defNum)
    {
        if(map==null)
        {
            return null;
        }
        String objStr = (map.get(key)==null)?"":map.get(key).toString();

        if(clazz==String.class)
        {
            return objStr;
        }
        if(clazz==int.class || clazz==Integer.class)
        {
            Integer rt= defNum;
            if(!StringUtils.isBlank(objStr))
            {
                rt=Integer.parseInt(objStr);
            }
            return numberSerialize(rt,clazz);
        }
        if(clazz==long.class || clazz==Long.class)
        {
            Long rt=(defNum==null)?null:defNum.longValue();
            if(!StringUtils.isBlank(objStr))
            {
                rt=Long.parseLong(objStr);
            }
            return numberSerialize(rt,clazz);
        }
        if(clazz==float.class || clazz==Float.class)
        {
            Float rt= (defNum==null)?null:defNum.floatValue();
            if(!StringUtils.isBlank(objStr))
            {
                rt=Float.parseFloat(objStr);
            }
            return numberSerialize(rt,clazz);
        }
        if(clazz==double.class || clazz==Double.class)
        {
            Double rt= (defNum==null)?null:defNum.doubleValue();
            if(!StringUtils.isBlank(objStr))
            {
                rt=Double.parseDouble(objStr);
            }
            return numberSerialize(rt,clazz);
        }

        if(clazz==byte.class || clazz==Byte.class )
        {
            Byte rt =(defNum==null)?null:defNum.byteValue();
            if(!StringUtils.isBlank(objStr))
            {
                rt=Byte.parseByte(objStr);
            }
            return numberSerialize(rt,clazz);
        }
        if(clazz==short.class || clazz==Short.class)
        {
            Short rt= (defNum==null)?null:defNum.shortValue();
            if(!StringUtils.isBlank(objStr))
            {
                rt=Short.parseShort(objStr);
            }
            return numberSerialize(rt,clazz);
        }
        if(clazz==char.class || clazz==Character.class)
        {
            Character rt= StringUtils.isBlank(objStr)?null:objStr.toCharArray()[0];
            if(clazz==char.class && rt==null)
            {
                rt =zeroLong.toString().toCharArray()[0];
            }
            return rt;
        }
        if(clazz==boolean.class || clazz==Boolean.class)
        {
            Boolean rt = StringUtils.isBlank(objStr)?null:Boolean.parseBoolean(objStr);
            if(clazz==boolean.class && rt==null)
            {
                rt =false;
            }
            return rt;
        }
        return objStr;
    }

    public static Object numberSerialize(Number number ,Class clazz)
    {
        if(number!=null)
        {
            return number;
        }
        if(clazz==int.class)
        {
            return zeroLong.intValue();
        }
        if(clazz==byte.class)
        {
            return zeroLong.byteValue();
        }
        if(clazz==short.class)
        {
            return zeroLong.shortValue();
        }
        if(clazz==long.class)
        {
            return zeroLong.longValue();
        }
        if(clazz==float.class)
        {
            return zeroLong.floatValue();
        }
        if(clazz==double.class)
        {
            return zeroLong.doubleValue();
        }
        return number;
    }


}
