package com.util.date;

import com.util.base.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

    public static int getNowInt()
    {
        return (int)((new Date()).getTime() / 1000);
    }

    public static void main(String args[])
    {
        System.out.println(dateLongTransReadAble("200",0));
        System.out.println(dateLongTransReadAble("12800",1));
        System.out.println(dateLongTransReadAble("haha",0));
        System.out.println("------------------");
    }

}
