package com.rule.data.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigUtil {

    private static final HashMap<String, String> config;

    //是否启用会话缓存
    private static boolean tlsEnabled = true;

    private static int config_cache_second = 1;

    private static int calTimeout = 3000;

    //最大数据大小
    private static int file_threshold = 20971520;
    //数据库最大行数
    private static int db_row_max = 200000;
    //线程数
    private static int thread_count = 100;

    private static int waiting_threshold = 1000;

    //调用日志开关
    private static boolean call_log = false;
    //debug开关
    private static boolean debug_flag = false;

    static {

        config = new HashMap<String, String>();

        Properties properties = new Properties();
        try {
            properties.load(ConfigUtil.class.getClassLoader().getResourceAsStream("config.properties"));

            for (Map.Entry entry : properties.entrySet()) {
                config.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
            }

            try {
                config_cache_second = Integer.parseInt(config.get("config_cache_second"));
            } catch (Throwable t) {
                //
            }

            try {
                tlsEnabled = Integer.parseInt(config.get("tls_enabled")) == 1;
            } catch (Throwable t) {
                //
            }

            try {
                calTimeout = Integer.valueOf(config.get("cal_timeout"));
            } catch (Throwable t) {
                //
            }

            try {
                file_threshold = Integer.valueOf(config.get("file_threshold"));
            } catch (Throwable t) {
                //
            }

            try {
                db_row_max = Integer.valueOf(config.get("db_row_max"));
            } catch (Throwable t) {
                //
            }

            try {
                thread_count = Integer.valueOf(config.get("thread_count"));
            } catch (Throwable t) {
                //
            }

            try {
                waiting_threshold = Integer.valueOf(config.get("waiting_threshold"));
            } catch (Throwable t) {
                //
            }

            try{
                call_log = Integer.valueOf(config.get("call_log")) == 1; //开启调用记录
            } catch (Throwable t){
                //
            }
            try{
                debug_flag = Integer.valueOf(config.get("debug_flag")) == 1; //开启调用记录
            } catch (Throwable t){
                //
            }

        } catch (Throwable e) {
            LogUtil.error("load config error, " + e.getMessage());
        }

    }

    public static int getConfigCacheSecond() {
        return config_cache_second;
    }

    public static boolean getTlsEnabled() {
        return tlsEnabled;
    }

    public static int getCalTimeout() {
        return calTimeout;
    }

    public static int getFileThreshold() {
        return file_threshold;
    }

    public static int getDbRowMax() {
        return db_row_max;
    }

    public static int getThreadCount() {
        return thread_count;
    }

    public static int getWaitingThreshold() {
        return waiting_threshold;
    }

    public static boolean getCallLog(){
        return call_log;
    }

    public static boolean getDebugFlag(){
        return debug_flag;
    }
}
