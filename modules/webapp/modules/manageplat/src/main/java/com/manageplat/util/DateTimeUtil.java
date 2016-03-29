package com.manageplat.util;

import org.joda.time.*;
import org.joda.time.format.DateTimeFormatter;

/**
 * org.joda.time.DateTime工具类
 */
public class DateTimeUtil {

    public static DateTime getDateTime(String dateName, String reg) {

        DateTimeFormatter dateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(reg);
        return dateTimeFormatter.parseDateTime(dateName);
    }


    /**
     * 时间差(天) 不管大在前还是小在前，返回都是正数
     * @param date1
     * @param date2
     * @return
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
     * @param date1
     * @param date2
     * @return
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

    public static void main(String args [])
    {
        DateTime date1 = DateTime.now();
        date1= getDateTime(date1.toString("yyyy-MM-dd"),"yyyy-MM-dd");

        DateTime date2 = DateTime.now().plusDays(30);
        date2= getDateTime(date2.toString("yyyy-MM-dd"),"yyyy-MM-dd");

        System.out.println("--------date1:"+date1.toString("yyyy-MM-dd HH:mm:ss"));
        System.out.println("--------date2:"+date2.toString("yyyy-MM-dd HH:mm:ss"));

        System.out.println(dateDiffDay(date1,date2));
        System.out.println(dateDiffDay(date2, date2));

        System.out.println(Days.daysBetween(date1, date2).getDays());
        System.out.println(Hours.hoursBetween(date1, date2).getHours());
        System.out.println(Minutes.minutesBetween(date1, date2).getMinutes());
        System.out.println(Seconds.secondsBetween(date1, date2).getSeconds());

        System.out.println("----------------");
        Float f =2.23f;

        System.out.println(f.toString()+'%');

        DateTime dd =getDateTime(new DateTime().toString("yyyy-MM-dd"),"yyyy-MM-dd");
        System.out.println("dd:"+dd.toString("yyyy-MM-dd HH:mm:ss"));

        DateTime ee =dd.plusMinutes(1430);
        System.out.println("ee:"+ee.toString("yyyy-MM-dd HH:mm:ss"));

        DateTime ff =dd.plusHours(24);
        System.out.println("ff:"+ff.toString("yyyy-MM-dd HH:mm:ss"));

        System.out.println(new DateTime().secondOfDay().withMinimumValue().toString("yyyy-MM-dd HH:mm:ss"));

        System.out.println(new DateTime().secondOfDay().withMaximumValue().toString("yyyy-MM-dd HH:mm:ss"));

    }
}
