package com.tool.util.base;

/**
 * 线程工具类
 */
public class ThreadUtil {

    public static void sleep(long mill)
    {
        try
        {
            Thread.sleep(mill);

        }catch(Exception e)
        {
            e.printStackTrace(System.out);
        }
    }
}
