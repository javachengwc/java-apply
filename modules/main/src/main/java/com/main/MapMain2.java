package com.main;

import java.util.*;

public class MapMain2 {

    public static void main(String args [])
    {
        Map<Integer,Integer> aMap = new HashMap<Integer,Integer>();
        Integer aa =aMap.get(null);
        System.out.println("-------------aa is:"+aa);
        chechKeyNull();
    }

    //hashMap的key可为null,treeMap的key不能为null
    public static  void chechKeyNull() {
        Map<String,String> map = new HashMap<String,String>();
        //Map<String,String> map = new TreeMap<String, String>(); 抛异常NullPointerException
        map.put("a","a");
        map.put(null,"b");
        String key=null;
        System.out.println(map.size());
        System.out.println(map.keySet().contains(key));
    }
}
