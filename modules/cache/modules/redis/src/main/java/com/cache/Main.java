package com.cache;

import com.cache.redis.demo.MapService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String args [])
    {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext(
                new String[]{"classpath:spring/ApplicationContext-context.xml",
                        "classpath:spring/ApplicationContext-redis.xml"
                });
        applicationContext.start();

        MapService mapService = applicationContext.getBean(MapService.class);
        try {

            Map<String,String> data =new HashMap<String,String>();
            data.put("1","100");
            data.put("2","0");

            String cacheKey="tmp";

            mapService.setData(cacheKey,data);

            Map<String,String> cacheData =mapService.getData(cacheKey);

            if(cacheData!=null)
            {
                System.out.println("cacheData is:");
                for(String key:cacheData.keySet())
                {
                    System.out.println(key+" "+cacheData.get(key));
                }

            }else
            {
                System.out.print("cacheData is null");
            }


        } catch (Exception e) {

            e.printStackTrace(System.out);

        } finally {
            System.exit(0);
        }
    }
}
