package com.es.consumer.base.config;

/**
 * 开关设置类
 */
public class SettingConfig {

    //处理消息开关
    public static boolean handleMessage=true;

    //写入索引等待结果时间，毫秒， <=0--表示一直阻塞等待结果， >0--表示等待此时长后中止等待。
    public static Long resultWaitTime =0L;

    //bulk批量处理数量
    public static int bulkCount=100;
}
