package com.shopstat.util;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * DateTime工具类
 */
public class DateTimeUtil {

    public static final String FMT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static final String FMT_YMD = "yyyy-MM-dd";

    public static Date getDate(String timeStr, String reg) {
        SimpleDateFormat format = new SimpleDateFormat(reg);
        Date date = null;
        try {
            date = format.parse(timeStr);
        } catch (Exception e)
        {
        }
        return date;
    }

    public static Date getDate(String timeStr) {
        return getDate(timeStr, FMT_YMD);
    }

    public static DateTime getDateTime(String dateName) {
        return getDateTime(dateName, FMT_YMD_HMS);
    }

    public static DateTime getDateTime(String dateName, String reg) {
        DateTimeFormatter dateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(reg);
        return dateTimeFormatter.parseDateTime(dateName);
    }

    public static Boolean checkDate(String date) {
        try {
            if (StringUtils.isNotBlank(date)) {
                new SimpleDateFormat(FMT_YMD).parse(date);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static Boolean checkDateTime(String date) {
        try {
            if (StringUtils.isNotBlank(date)) {
                new SimpleDateFormat(FMT_YMD_HMS).parse(date);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 时间差(天) 不管大在前还是小在前，返回都是正数
     */
    public static int dateDiffDay(DateTime date1,DateTime date2)
    {
        int days= Days.daysBetween(date1, date2).getDays();
        if(days<0)
        {
            days=-days;
        }
        return days;
    }

    /**
     * 时间差(分) 不管大在前还是小在前，返回都是正数
     */
    public static int dateDiffMinute(DateTime date1,DateTime date2)
    {
        int minute= Minutes.minutesBetween(date1, date2).getMinutes();
        if(minute<0)
        {
            minute=-minute;
        }
        return minute;
    }
}