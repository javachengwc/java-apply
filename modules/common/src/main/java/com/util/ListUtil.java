package com.util;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

/**
 * list转化工具类
 */
public class ListUtil {

    /**list转成sql in语句片段**/
    public static String list2SqlInStr(List<?> list)
    {

        String resultStr ="";
        if(list==null || list.size()<=0)
        {
            return resultStr;
        }
        StringBuffer resultBuf = new StringBuffer();
        for(Object obj:list)
        {
            if((obj instanceof Number) )
            {
                resultBuf.append(((Number)obj)).append(",");

            }else
            {
                resultBuf.append('\'').append(obj.toString()).append('\'').append(",");
            }

        }
        resultStr = resultBuf.toString();
        if(!StringUtils.isBlank(resultStr) && resultStr.endsWith(","))
        {
            resultStr=resultStr.substring(0,resultStr.length()-1);
        }
        return resultStr;
    }

    /**提取listMap中的某key-value组成map返回**/
    public static Map<String, Object> parseListMapToMap(List<Map<String, Object>> mapList, String key, String value){
        Map<String, Object> result = new HashMap<String, Object>();
        Object keyObj = null;
        Object valueObj = null;
        for (Map<String, Object> map : mapList){
            if (map != null){
                keyObj = map.get(key);
                if (keyObj != null && !"".equals(String.valueOf(keyObj)) ){
                    result.put(String.valueOf(keyObj), map.get(value));
                }
            }
        }
        return result;
    }

    /**list数据拼接成字符串**/
    public static <T> String listJointToStr(List<T> list,String split)
    {
        return listJointToStr(list,split,null,null);
    }

    /**list数据拼接成字符串**/
    public static <T>  String  listJointToStr(List<T> list,String split,String pre,String suf )
    {

        if(list==null || list.size()<=0)
        {
            return "";
        }
        StringBuffer buf = new StringBuffer();
        for(T t:list)
        {
            if(pre!=null)
            {
                buf.append(pre);
            }
            buf.append(t.toString());
            if(suf!=null)
            {
                buf.append(suf);
            }
            buf.append(split);
        }
        String rt = buf.toString();
        if(rt.endsWith(split))
        {
           rt= rt.substring(0,rt.lastIndexOf(split));
        }
        return rt;
    }

    //将list转换成map
    public static <K,V> Map<K,V> transMap(List<V> list,Picker<K,V> picker)
    {
        return transMap(list,picker,null);
    }

    public static <K,V> Map<K,V> transMap(List<V> list,Picker<K,V> picker,K def)
    {
        Map<K,V> map =new HashMap<K,V>();
        if(list==null || list.size()<=0)
        {
            return map;
        }
        for(V per:list)
        {
            K key = picker.pick(per);
            if(key==null)
            {
                key=def;
            }
            if(key!=null)
            {
                map.put(key,per);
            }
        }
        return  map;
    }

    //从对象中提取某值的接口
    public interface  Picker<K,V>
    {
        public K pick(V  value);
    }

    public static void main(String args [])
    {
        List<Integer> intList = new ArrayList<Integer>();
        intList.add(1);
        intList.add(2);
        intList.add(3);

        List<String> strList = new ArrayList<String>();
        strList.add("a");
        strList.add("b");
        strList.add("c");

        System.out.println("----------------"+list2SqlInStr(intList));
        System.out.println("----------------"+list2SqlInStr(strList));

        System.out.println("----------------"+listJointToStr(intList,","));
        System.out.println("----------------"+listJointToStr(strList,","));
        System.out.println("----------------"+listJointToStr(strList,",","'","'"));


    }
}
