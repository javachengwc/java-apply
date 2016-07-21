package com.other.guava;

import com.google.common.collect.*;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MultimapMain {

    public static void main(String args [])
    {
        HashMultimap<Integer,Integer> map = HashMultimap.create();
        map.put(2, 3);
        map.put(1, 2);
        map.put(1, 3);
        map.put(2, 6);
        map.put(2, 3);
        map.put(11, 2);
        map.put(12, 3);
        map.put(21, 6);
        map.put(6, 7);
        map.put(2, 3);

        //同key,同value的map归一了
        //2-3的map有3个，放HashMultimap后只有一个2-3存在
        int count = map.size();
        System.out.println("------map count="+count);

        Iterator iter = map.entries().iterator();
        while(iter.hasNext())
        {
            Map.Entry<Integer, Integer> entry = (Map.Entry<Integer, Integer>)iter.next();
            System.out.println(String.format("%d:%d", entry.getKey(),entry.getValue()));
        }
        System.out.println("------------------------");

        Set<Integer> keys = map.keySet();
        for(int key:keys)
        {
            String result = String.format("%d:", key);
            Set<Integer> values = map.get(key);
            for(int value:values)
            {
                result= result+" "+value;
            }
            System.out.println(result);
        }
    }
}
