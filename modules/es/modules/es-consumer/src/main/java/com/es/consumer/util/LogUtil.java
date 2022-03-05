package com.es.consumer.util;

public class LogUtil {

    /**
     * 日志头部格式化数据
     */
    public static String[] formatLogHeader(String header){
        String[] strs = header.split(" ");
        //日期
        String day = "20"+strs[0].replace("/", "");
        //小时
        String hour = strs[1].substring(0, 2);
        //分钟
        String minute = strs[1].substring(3, 5);
        //url
        String url = "";
        if(strs.length==5){
            url = strs[4];
        }
        return new String[]{day,hour,url,minute};
    }

}
