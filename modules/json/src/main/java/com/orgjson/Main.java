package com.orgjson;

import java.util.*;

import com.model.SiteAccessInfo;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * org.json包的json相关操作
 * 优点：轻量级,在处理json字符串相对简便
 * 缺点：json转成java对象比较费事,都需要自己实现,lib包不成熟,建议不用
 */
public class Main {


    public static void main(String[] args) throws Exception{

        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("name", "Alexia");
        map1.put("sex", "female");
        map1.put("age", "23");

        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("name", "Edward");
        map2.put("sex", "male");
        map2.put("age", "24");

        List<Map> list = new ArrayList<Map>();
        list.add(map1);
        list.add(map2);

        List<SiteAccessInfo> list2 = generalTestData();

        //list<map>-->json
        JSONArray ja = new JSONArray(list);
        //list<bean>-->json
        JSONArray ja2 = new JSONArray(list2);

        JSONArray ja3 = new JSONArray();
        ja3.put(map1);

        System.out.println("JSONArray对象数据格式：");
        System.out.println(ja.toString());
        System.out.println(ja2.toString());
        System.out.println(ja3.toString());

        SiteAccessInfo info = generalSiglData();

        JSONObject jo1 = new JSONObject(info);

        System.out.print("entity对象的Json数据格式：");
        System.out.println(jo1.toString());

        //json-->bean 没提供内置的接口功能 需要自己实现json-bean的转换
        SiteAccessInfo info2 = JsonHelper.json2Bean(jo1,SiteAccessInfo.class);
        System.out.print("json转成entity数据:");
        System.out.println(info2);

        //json-->listbean
        List<SiteAccessInfo> rtList = JsonHelper.json2ListBean(ja2,SiteAccessInfo.class);
        if(rtList!=null)
        {
            for(SiteAccessInfo st:rtList)
            {
                System.out.println("---rt list per:"+st);
            }
        }
        List<Map<String,Object>> rtList2 = JsonHelper.json2ListMap(ja.toString());
        if(rtList2!=null)
        {
            for(Map<String,Object> m:rtList2)
            {
                System.out.print("----rt listmap per:");
                for(String key:m.keySet())
                {
                    System.out.print(key+":"+m.get(key)+",");
                }
                System.out.println();
            }
        }

    }

    public static SiteAccessInfo generalSiglData()
    {
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.xxx.com");
        info.setName("xxx");
        info.setUv(3000 * 10000l);
        info.setOut(3000 * 1000l);
        info.setPcExp(3000 * 9000l);
        //info.setWxExp(3000 * 2000l);
        info.setAddTime(new Date());
        return info;
    }


    public static List<SiteAccessInfo> generalTestData()
    {
        List<SiteAccessInfo> list = new ArrayList<SiteAccessInfo>();
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.baidu.com");
        info.setName("百度");
        info.setUv(10000 * 10000l);
        info.setOut(10000 * 1000l);
        info.setPcExp(10000 * 9000l);
        info.setWxExp(10000 * 2000l);
        info.setAddTime(new Date());
        list.add(info);

        SiteAccessInfo info2 = new SiteAccessInfo();
        info2.setSite("http://www.yy.com");
        info2.setName("YY");
        info2.setUv(1000*5000l);
        info2.setOut(1000*3000l);
        info2.setPcExp(1000*700l);
        info2.setWxExp(1000*2000l);
        list.add(info2);

        return list;
    }

}