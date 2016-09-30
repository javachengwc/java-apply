package com.mountain.util;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 命名的线程工厂
 */
public class NamedThreadFactory implements ThreadFactory
{
    private AtomicInteger threadNum = new AtomicInteger(1);

    private static String defPrefix="mountain";

    private String prefix;

    private boolean daemo;

    private ThreadGroup group;

    public NamedThreadFactory()
    {
        this(defPrefix,false);
    }

    public NamedThreadFactory(String prefix)
    {
        this(prefix,false);
    }

    public NamedThreadFactory(String prefix,boolean daemo)
    {
        this.prefix= prefix;
        this.daemo = daemo;
        SecurityManager securityManager = System.getSecurityManager();
        group = ( securityManager == null ) ? Thread.currentThread().getThreadGroup() : securityManager.getThreadGroup();
    }

    public Thread newThread(Runnable runnable)
    {
        String name = prefix+"-thread-" + threadNum.getAndIncrement();
        Thread ret = new Thread(group,runnable,name,0);
        ret.setDaemon(daemo);
        return ret;
    }
}