package com.tool.util.date;

import com.tool.util.base.NumberUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间工具类
 */
public class DateUtil {

    public static final String FMT_YMD_HMS_MS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static final String FMT_YMD_HMS = "yyyy-MM-dd HH:mm:ss";

    public static final String FMT_YMD = "yyyy-MM-dd";

    public static final String FMT_DOT_YMD = "yyyy.MM.dd";

    public static final String FMT_YMD2 = "yyyy-MM";

    public static final String FMT_YYMMDD="yyyyMMdd";

    //一天的毫秒数
    public static long DAY_MILIS = 86400000L;

    public static Date getDate(String timeStr, String fmt) {
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        Date date = null;
        try {
            date = format.parse(timeStr);
        } catch (ParseException e) {
        }
        return date;
    }

    public static String formatDate(Date date,String fmt)
    {
        SimpleDateFormat format = new SimpleDateFormat(fmt);
        return format.format(date);
    }

    public static Boolean checkDate(String date,String fmt) {
        try {
            if (StringUtils.isNotBlank(date))
            {
                new SimpleDateFormat(fmt).parse(date);
                return true;
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    public static long getDayDiff(Date date1, Date date2) {
        long timea = date1.getTime();
        long timeend = date2.getTime();
        long diff = (timeend - timea) / DAY_MILIS;
        long day = Math.abs(diff);
        return day;
    }

    /**将秒或分转成可读的数据 显示**/
    public static String dateLongTransReadAble(String period,int flag)
    {
        if(StringUtils.isBlank(period) || !NumberUtil.isNumeric(period))
        {
            return "";
        }

        int hour=0;
        int minite=0;
        int sed=0;

        Long value = Long.parseLong(period);

        int md = new Long(value%60).intValue();
        int hs = new Long(value/60).intValue();

        if(flag==1)
        {
            //表示参数 是秒数
            sed=md;
            minite = hs%60;
            hs = hs/60;
        }else
        {
            minite=md;
        }
        hour = hs;

        StringBuffer buf = new StringBuffer("");
        if(hour>0)
        {
            buf.append(hour).append("小时");
        }
        if(minite>0)
        {
            buf.append(minite).append("分");
        }
        if(sed>0)
        {
            buf.append(sed).append("秒");
        }
        return buf.toString();
    }

    public static Date addDates(Date date, int days)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        return c.getTime();
    }

    public static int getNowInt()
    {
        return (int)((new Date()).getTime() / 1000);
    }

    public static String formatDate2(Date date, String pattern) {
        String str = DateFormatUtils.format(date, pattern);
        return str;
    }

    public static void main(String args[])
    {
        Date date = new Date();
        System.out.println(formatDate(date,FMT_YMD_HMS));
        System.out.println(formatDate2(date,FMT_YMD_HMS));

        System.out.println(dateLongTransReadAble("200",0));
        System.out.println(dateLongTransReadAble("12800",1));
        System.out.println(dateLongTransReadAble("haha",0));
        System.out.println("------------------");
    }

}
