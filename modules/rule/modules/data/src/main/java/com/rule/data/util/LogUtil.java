package com.rule.data.util;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

public class LogUtil {

    private static final Logger logger = Logger.getLogger(LogUtil.class);

    private static final ConcurrentLinkedQueue<LoggingEvent> queue = new ConcurrentLinkedQueue<LoggingEvent>();

    private static final String className = LogUtil.class.getName();

    static {

        new Thread(new Runnable() {
            @Override
            public void run() {

                Thread.currentThread().setName("log thread");
                LoggingEvent event;

                while (true) {

                    while ((event = queue.poll()) != null) {
                        logger.callAppenders(event);
                    }

                    try {
                        TimeUnit.SECONDS.sleep(1);
                    } catch (InterruptedException e) {
                        //
                    }
                }

            }
        }).start();
    }

    private static LoggingEvent getEvent(Level level, String msg, Throwable t) {

        LoggingEvent event = new LoggingEvent(className, logger, level, str2OneLine(msg), t);

        event.getLocationInformation();

        return event;
    }

    public static void info(String msg, Throwable t) {
        if (logger.isInfoEnabled()) {
            queue.offer(getEvent(Level.INFO, msg, t));
        }
    }

    public static void info(String msg) {
        if (logger.isInfoEnabled()) {
            queue.offer(getEvent(Level.INFO, msg, null));
        }
    }

    public static void debug(String msg, Throwable t) {
        if (logger.isDebugEnabled()) {
            queue.offer(getEvent(Level.DEBUG, msg, t));
        }
    }

    public static void debug(String msg) {
        if (logger.isDebugEnabled()) {
            queue.offer(getEvent(Level.DEBUG, msg, null));
        }
    }

    public static boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }


    public static void warn(String msg, Throwable t) {
        queue.offer(getEvent(Level.WARN, msg, t));
    }

    public static void warn(String msg) {
        queue.offer(getEvent(Level.WARN, msg, null));
    }

    public static void error(String msg, Throwable t) {
        queue.offer(getEvent(Level.ERROR, msg, t));
    }

    public static void error(String msg) {
        queue.offer(getEvent(Level.ERROR, msg, null));
    }

    public static void fatal(String msg, Throwable t) {
        queue.offer(getEvent(Level.FATAL, msg, t));
    }

    public static void fatal(String msg) {
        queue.offer(getEvent(Level.FATAL, msg, null));
    }

    public static void trace(String msg, Throwable t) {
        queue.offer(getEvent(Level.TRACE, msg, t));
    }

    public static void trace(String msg) {
        queue.offer(getEvent(Level.TRACE, msg, null));
    }

    //转换成1行
    public static String str2OneLine(String str) {
        if (str == null || str.length() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(str.length());

        for (char c : str.toCharArray()) {
            if (c == '\t' || c == '\n' || c == '\r') {
                sb.append(' ');
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}
