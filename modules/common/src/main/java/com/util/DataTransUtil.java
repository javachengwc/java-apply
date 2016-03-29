package com.util;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 数据转换工具类
 */
public class DataTransUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransUtil.class);

    /**
     * bean转成map
     * @param obj
     * @return
     */
    public static Map<String,Object> bean2Map(Object obj)
    {
        if(obj==null)
        {
            return null;
        }
        Map<String,Object> map =new HashMap<String,Object>();
        try {

            Class clazz = obj.getClass();
            while(clazz!=null && clazz!=Object.class)
            {
                Field[] fields = clazz.getDeclaredFields();
                for(Field field :fields)
                {
                    String key = field.getName();
                    Object value = BeanUtils.getProperty(obj, key);

                    map.put(key,value);
                }
                clazz = clazz.getSuperclass();
            }

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return map;
    }

    /**
     * 列表bean转成列表map
     * @param list
     * @param <T>
     * @return
     */
    public static <T> List<Map> listBean2ListMap(List<T> list)
    {
        if(list==null || list.size()<=0)
        {
            return null;
        }
        List<Map> resultList = new LinkedList<Map>();
        for(T obj:list)
        {
            Map<String,Object> map = bean2Map(obj);
            if(map!=null)
            {
                resultList.add(map);
            }
        }
        return resultList;
    }

    public static byte[] int2byte(int n)
    {
        byte[] b = new byte[4];
        b[0] = (byte)(n >> 24);
        b[1] = (byte)(n >> 16);
        b[2] = (byte)(n >> 8);
        b[3] = (byte)n;
        return b;
    }
    public static int byte2int(byte[] b, int offset) {
        return b[(offset + 3)] & 0xFF | (b[(offset + 2)] & 0xFF) << 8 | (b[(offset + 1)] & 0xFF) << 16 | (b[offset] & 0xFF) << 24;
    }
    public static int byte2int(byte[] b) {
        return byte2int(b, 0);
    }

    public static String bytes2HexString(byte[] b) {
        String result = "";
        String tem = "";
        for (int i = 0; i < b.length; i++) {
            tem = Integer.toHexString(b[i] & 0xFF);
            if (tem.length() < 2) {
                tem = "0" + tem;
            }
            result = result + tem;
        }
        return result;
    }

    public static byte[] hexString2Bytes(String str)
    {
        if ((str == null) || (str.length() <= 0)) {
            return null;
        }
        int iLeng = str.length();
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        DataOutputStream out2 = new DataOutputStream(out1);
        try {
            for (int i = 0; i < iLeng; i += 2)
                try {
                    out2.writeByte(Integer.parseInt(str.substring(i, i + 2), 16));
                }
                catch (Exception localException1) {
                }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return out1.toByteArray();
    }

    public static String str2HexStr(String para, String encoding)
    {
        if ((para == null) || (para.length() <= 0))
            return null;
        String r = "";
        try {
            byte[] v = para.getBytes(encoding);
            for (int i = 0; i < v.length; i++)
                r = r + Integer.toHexString(v[i] & 0xFF);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }

    /**
     * map-list 转成 list-list
     * 各个key-list中bean与其他key-list中bean相互组合形成多组组合
     */
    public static <T>  List<List<T>> mapList2ListList(Map<String,List<T>> mapList)
    {
        List<List<T>> result = new LinkedList<List<T>>();
        for(String key:mapList.keySet())
        {
            List<T> list = mapList.get(key);
            result = nextCondition(result,list);
        }
        return result;
    }

    public static <T>  List<List<T>> nextCondition(List<List<T>> list,List<T> next)
    {
        if(next==null || next.size()<=0)
        {
            return list;
        }
        int size = next.size();

        List<List<T>> result = new LinkedList<List<T>>();
        if(list==null || list.size()<=0)
        {
            for(T combin:next) {
                List<T> perList = new LinkedList<T>();
                perList.add(combin);
                result.add(perList);
            }
            return result;
        }
        for (int i = 0; i < size; i++) {
            T combin = next.get(i);
            List<List<T>> targetList = simpleCopy(list);
            for(List<T> perList:targetList)
            {
                perList.add(combin);
            }
            result.addAll(targetList);
        }
        list.clear();
        return result;
    }

    public static <T> List<List<T>> simpleCopy(List<List<T>> list)
    {
        List<List<T>> targetList = new LinkedList<List<T>>();
        for(List<T> perList:list)
        {
            targetList.add(perList);
        }
        return targetList;
    }

}
