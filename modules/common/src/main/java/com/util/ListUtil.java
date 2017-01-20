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

    public static <K> Map<K,Map<String,Object>> transMap(List<Map<String,Object>> list,final String keyName,final Class kClass)
    {
        Picker<K,Map<String,Object>> picker =new Picker<K, Map<String, Object>>() {
            @Override
            public K pick(Map<String, Object> value) {
                return (K)MapUtil.extractData(value,keyName,kClass);
            }
        };
        return transMap(list,picker);
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

        List<Map<String,Object>> listMap = new ArrayList<Map<String,Object>>();
        Map<String,Object> map1=new HashMap<String,Object>();
        map1.put("a",100);
        map1.put("b","bbb");
        listMap.add(map1);
        Map<String,Object> map2=new HashMap<String,Object>();
        map2.put("a",200);
        map2.put("b","bbb2");
        listMap.add(map2);
        Map<Integer,Map<String,Object>> tMap = transMap(listMap,"a",Integer.class);
        System.out.println(tMap.size());
        for(Map.Entry<Integer,Map<String,Object>> ent:tMap.entrySet())
        {
            System.out.println(ent.getKey()+":"+ent.getValue());
        }
    }
}
