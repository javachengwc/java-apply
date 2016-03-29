package com.util;

import java.util.LinkedList;
import java.util.List;

/**
 * 运行时工具类
 */
public class RunTimeUtil {

    /**
     * 获取调用栈信息,从调用者开始的栈信息
     */
    public static List<String> getInvokeStackInfo()
    {
        StackTraceElement[]  traces =Thread.currentThread().getStackTrace();
        List<String> list = new LinkedList<String>();
        //1--getStackTrace
        //2--getInvokeStackkInfo
        if(traces.length<=2)
        {
            return list;
        }
        for(int i=0;i<traces.length;i++)
        {
            if(i<=1)
            {
                continue;
            }
            StackTraceElement element = traces[i];
            list.add(element.getClassName()+","+element.getMethodName()+","+element.getLineNumber());
        }
        return list;
    }

    /**
     * 获取自己的方法名
     * @return
     */
    public static String getSelfMethodName()
    {

        StackTraceElement[]  traces =Thread.currentThread().getStackTrace();
        //1--getStackTrace
        //2--getSelfMethod
        //3--自己的方法
        if(traces.length<3)
        {
            return "";
        }
        else
        {
            return traces[2].getMethodName();
        }
    }

    /**获取调用自己的方法的名称*/
    public static String getInvokeMeMethodName()
    {

        StackTraceElement[]  traces =Thread.currentThread().getStackTrace();
        //1--getStackTrace
        //2--getSelfMethod
        //3--自己的方法
        //调用自己的方法名
        if(traces.length<4)
        {
            return "";
        }
        else
        {
            return traces[3].getMethodName();
        }
    }

    /**
     * 获取自己的类名
     * @return
     */
    public static String getSelfClassName()
    {
        StackTraceElement[]  traces =Thread.currentThread().getStackTrace();
        //1--Thread
        //2--RunTimeUtil
        //3--自己的类
        if(traces.length<3)
        {
            return "";
        }
        else {
            return traces[2].getClassName();
        }

    }

    /**
     * 获取调用自己的类名
     * @return
     */
    public static String getInvokeMeClassName()
    {
        StackTraceElement[]  traces =Thread.currentThread().getStackTrace();
        //1--Thread
        //2--RunTimeUtil
        //3--自己的类
        //4--调用自己的类
        if(traces.length<4)
        {
            return "";
        }
        else {
            return traces[3].getClassName();
        }

    }

    /**
     * 增加JVM停止时要做处理事件
     */
    public static void addShutdownHook( Runnable runnable ) {
        Runtime.getRuntime().addShutdownHook( new Thread( runnable ) );
    }

    public static void main(String args[])
    {

        //打印调用栈
        List<String> list = getInvokeStackInfo();
        for(String s:list)
        {
            System.out.println(s);
        }

        System.out.println("----selfMethodName:"+getSelfMethodName());
        System.out.println("----selfClassName:"+getSelfClassName());

        System.out.println("----getInvokeMeMethodName:"+getInvokeMeMethodName());
        System.out.println("----getInvokeMeClassName:"+getInvokeMeClassName());

    }
}
