package com.other.calendar;

import java.util.Calendar;

/**
 * Calendar.MONTH
 * 1,2...12-->0,1..11
 * Calendar.DAY_OF_WEEK
 * 星天 1,2,3,4,5,6-->1,2,3,4,5,6,7
 *
 */
public class CalendarBean {

    private int year;

    private int month;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String[] getCalendar()
    {

        String a[]=new String[42];

        Calendar date=Calendar.getInstance();

        //设定的年月第一天
        date.set(year,month-1,1);

        //星期几，0是星期天
        int week=date.get(Calendar.DAY_OF_WEEK)-1;

        //这个月有多少天
        int day=getDaysByMonth(month);

        //从这个月第一天设置数据，第一天对应的星期是week,对应的日期是1号
        for(int i=week,n=1;i<week+day;i++)
        {
            a[i]=String.valueOf(n) ;
            n++;
        }
        return a;
    }

    public Integer getDaysByMonth(int mth)
    {
        int day=0;

        //判断大月份
        if(month==1||month==3||month==5||month==7||month==8||month==10||month==12)
        {
            day=31;
        }
        //判断小月
        if(month==4||month==6||month==9||month==11)
        {
            day=30;
        }
        //判断平年与闰年
        if(month==2)
        {

            if(((year%4==0)&&(year%100!=0))||(year%400==0))
            {
                day=29;

            }
            else
            {
                day=28;
            }
        }
        return day;
    }

    public static void main(String args [])
    {
        Calendar ca = Calendar.getInstance();
        CalendarBean bean = new CalendarBean();
        bean.setYear(ca.get(Calendar.YEAR));
        int month=ca.get(Calendar.MONTH);
        System.out.println("----month:"+month);
        int dayweek = ca.get(Calendar.DAY_OF_WEEK);
        System.out.println("----dayweek:"+dayweek);

        bean.setMonth(month+1);

        String [] a = bean.getCalendar();
        for(String p:a)
        {
            System.out.println("---------p:"+p);
        }

    }
}
