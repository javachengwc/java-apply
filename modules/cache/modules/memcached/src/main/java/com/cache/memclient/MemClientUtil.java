package com.cache.memclient;

import com.whalin.MemCached.MemCachedClient;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MemClientUtil {

    public static List<String> getAllKeys(MemCachedClient client) {
        List<String> list = new ArrayList<String>();

        Map<String, Map<String, String>> items = client.statsItems();
        for (Iterator<String> itemIt = items.keySet().iterator(); itemIt.hasNext();) {
            String itemKey = itemIt.next();

//            System.out.println("----------------------------");
//            System.out.println("items:" + itemKey);//items:127.0.0.1:11211
//            System.out.println("============================");

            Map<String, String> maps = items.get(itemKey);
            for (Iterator<String> mapsIt = maps.keySet().iterator(); mapsIt.hasNext();) {
                String mapsKey = mapsIt.next();
                String mapsValue = maps.get(mapsKey);

//                System.out.println("----------------------------");
//                System.out.println("maps:" + mapsKey);//items:15:number
//                System.out.println("maps:" + mapsValue);//50
//                System.out.println("============================");

                if (mapsKey.endsWith("number")) {//memcached key 类型  item_str:integer:number_str
                    String[] arr = mapsKey.split(":");
                    int slabNumber = Integer.valueOf(arr[1].trim());
                    int limit = Integer.valueOf(mapsValue.trim());

                    Map<String, Map<String, String>> dumpMaps = mcc.statsCacheDump(slabNumber, limit);
                    for (Iterator<String> dumpIt = dumpMaps.keySet().iterator(); dumpIt.hasNext();) {
                        String dumpKey = dumpIt.next();

//                        System.out.println("----------------------------");
//                        System.out.println("dumpMaps:" + dumpKey);//127.0.0.1:11211
//                        System.out.println("============================");

                        Map<String, String> allMap = dumpMaps.get(dumpKey);
                        for (Iterator<String> allIt = allMap.keySet().iterator(); allIt.hasNext();) {
                            String allKey = allIt.next();
                            list.add(allKey.trim());
                        }
                    }
                }
            }
        }
        return list;
    }
}
