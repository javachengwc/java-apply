package com.cache.memclient;

import com.whalin.MemCached.MemCachedClient;
import com.whalin.MemCached.SockIOPool;

import java.util.*;

public class MemClientUtil {

    private static MemCachedClient client = new MemCachedClient();

    static {
        //服务器列表和其权重
        String[] servers = { "127.0.0.1:11211" };
        Integer[] weights = { 5 };

        //获取socke连接池的实例对象
        SockIOPool pool = SockIOPool.getInstance();
        pool.setServers(servers);
        pool.setWeights(weights);
        //初始连接数、最小和最大连接数以及最大处理时间
        pool.setInitConn(5);
        pool.setMinConn(5);
        pool.setMaxConn(50);
        pool.setMaxIdle(30 * 60 * 1000);  //30分钟

        //设置主线程的睡眠时间
        pool.setMaintSleep(30);

        //设置TCP的参数，连接超时等
        pool.setSocketTO(3000);
        pool.setSocketConnectTO(0);
        pool.initialize();
    }

    //获取
    public static Object get(String key) {
        return client.get(key);
    }

    //删除
    public static boolean delete(String key) {
        return client.delete(key);
    }

    //添加
    public static boolean add(String key, Object value) {
        return client.add(key, value);
    }

    public static boolean add(String key, Object value, Date expiry) {
        return client.add(key, value, expiry);
    }

    //替换
    public static boolean replace(String key, Object value) {
        return client.replace(key, value);
    }

    public static boolean replace(String key, Object value, Date expiry) {
        return client.replace(key, value, expiry);
    }

    //获取全部节点
    public static List<String> getAllKeys() {
        List<String> list = new ArrayList<String>();

        Map<String, Map<String, String>> items = client.statsItems();
        printMMap(items);
        for (String itemKey:items.keySet()) {
            Map<String, String> maps = items.get(itemKey);
            for (String mapsKey:maps.keySet()) {
                String mapsValue = maps.get(mapsKey);
                if (mapsKey.endsWith("number")) {
                   //memcached key类型: item_str:integer:number_str 比如:items:1:number
                    String[] arr = mapsKey.split(":");
                    int slabNumber = Integer.valueOf(arr[1].trim());
                    int limit = Integer.valueOf(mapsValue.trim());

                    Map<String, Map<String, String>> dumpMaps = client.statsCacheDump(slabNumber, limit);
                    printMMap(dumpMaps);
                    for (String dumpKey:dumpMaps.keySet()) {
                        Map<String, String> allMap = dumpMaps.get(dumpKey);
                        list.addAll(allMap.keySet());
                    }
                }
            }
        }
        return list;
    }

    private static void printMMap(Map<String, Map<String, String>> mmap)
    {
        if(mmap==null || mmap.size()<=0)
        {
            System.out.println("mmap is null or size <=0");
        }
        for (String mapsKey:mmap.keySet()) {
            System.out.println("mapskey="+mapsKey);
            Map<String, String> maps= mmap.get(mapsKey);
            System.out.println("mapskey value:");
            if(maps==null || maps.size()<=0)
            {
                System.out.println("maps is null or size <=0");
            }else
            {
                for (String key:maps.keySet())
                {
                    System.out.print(key+"-->"+maps.get(key));
                }
            }
        }
    }
}
