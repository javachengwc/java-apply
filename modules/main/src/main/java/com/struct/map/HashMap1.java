package com.struct.map;

import java.util.HashMap;
import java.util.Map;

public class HashMap1 {

    public static void main(String [] args) {

        Map<String,String> map = new HashMap<String,String>();
        map.put(null,null);//map的key-value都可为空
        map.put("a","a");

        System.out.println(map.size());
     }
}
