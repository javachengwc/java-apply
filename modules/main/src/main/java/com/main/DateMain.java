package com.main;

import com.util.date.DateUtil;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateMain {

	public static void main(String args [])
	{
	    Date defDate =new Date(0);
	    System.out.println(DateUtil.formatDate(defDate,DateUtil.FMT_YMD_HMS));

		DateTime now = new DateTime();
		System.out.println(now.toString("yyyy/MM/dd"));
		System.out.println(now.getYear()+"/"+now.getMonthOfYear()+"/"+now.getDayOfMonth());
		
		System.out.println(now.getMillis()+","+System.currentTimeMillis());
		
		DateTime beginO = new DateTime(now.getYear(),now.getMonthOfYear(),now.getDayOfMonth(),0,0,0);
        DateTime end0 = new DateTime(2015,5,31,0,0,0);
        int days = Days.daysBetween(beginO, end0).getDays();
        System.out.println(days);

        DateTime a = new DateTime();
        System.out.println(a.toString("yyyy-MM-dd HH:mm:ss"));
        DateTime b = addHour(a,2);
        System.out.println(b.toString("yyyy-MM-dd HH:mm:ss"));

        DateTime c =getDateTime("2015-06-23","yyyy-MM-dd");
        System.out.println(c.toString("yyyy-MM-dd HH:mm:ss"));

	}

    public static DateTime addHour(DateTime dateTime,int hour)
    {
        dateTime =dateTime.plusHours(hour);
        return dateTime;
    }

    public static DateTime getDateTime(String dateName, String reg) {
        DateTimeFormatter dateTimeFormatter = org.joda.time.format.DateTimeFormat.forPattern(reg);
        return dateTimeFormatter.parseDateTime(dateName);
    }
}
