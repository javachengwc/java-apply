package com.util.date;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CalendarUtil
{
	/**
	 * 取得当前日期是多少周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setMinimalDaysInFirstWeek(7);
		c.setTime(date);
		
		return c.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * 得到某一年周的总数
	 * 
	 * @param year
	 * @return
	 */
	public static int getMaxWeekNumOfYear(int year)
	{
		Calendar c = new GregorianCalendar();
		c.set(year, Calendar.DECEMBER, 31, 23, 59, 59);
		
		return getWeekOfYear(c.getTime());
	}
	
	/**
	 * 得到某年某周的第一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getFirstDayOfWeek(int year, int week)
	{
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		
		Calendar cal = (GregorianCalendar)c.clone();
		cal.add(Calendar.DATE, week * 7);
		
		return getFirstDayOfWeek(cal.getTime());
	}
	
	/**
	 * 得到某年某周的最后一天
	 * 
	 * @param year
	 * @param week
	 * @return
	 */
	public static Date getLastDayOfWeek(int year, int week)
	{
		Calendar c = new GregorianCalendar();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, Calendar.JANUARY);
		c.set(Calendar.DATE, 1);
		
		Calendar cal = (GregorianCalendar)c.clone();
		cal.add(Calendar.DATE, week * 7);
		
		return getLastDayOfWeek(cal.getTime());
	}
	
	/**
	 * 取得当前日期所在周的第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfWeek(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return calculateBeginDayOfWeek(c);
	}
	
	/**
	 * 取得当前日期所在周的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfWeek(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return calculateEndDayOfWeek(c);
	}
	
	/**
	 * 取得当前日期所在月的第一天
	 * <p>
	 * 功能描述：
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayOfMonth(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return calculateBeginDayOfMonth(c);
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期所在月的最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayOfMonth(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return calculateEndDayOfMonth(c);
		
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期开时时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateTimeOfDay(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return calculateBeginTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateTimeOfDay(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		return calculateEndTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期所在周的第一天的开时时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayTimeOfWeek(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		calculateBeginDayOfWeek(c);
		return calculateBeginTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期所在周的最后一天的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayTimeOfWeek(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		calculateEndDayOfWeek(c);
		return calculateEndTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期所在月的第一天的开时时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDayTimeOfMonth(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		calculateBeginDayOfMonth(c);
		return calculateBeginTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：取得当前日期所在月的最后一天的结束时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDayTimeOfMonth(Date date)
	{
		Calendar c = new GregorianCalendar();
		c.setTime(date);
		calculateEndDayOfMonth(c);
		return calculateEndTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：本季度的起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getQuarterStartAndEndDayTime(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		int month = c.get(Calendar.MONTH);
		int monthStart = 0;
		Date quarterStart = null;
		Date quarterEnd = null;
		if(month >= 0 && month <= 2)
		{
			monthStart = 0;
		}
		else if(month >= 3 && month <= 5)
		{
			monthStart = 3;
		}
		else if(month >= 6 && month <= 8)
		{
			monthStart = 6;
		}
		else
		{
			monthStart = 9;
		}
		c.set(Calendar.MONTH, monthStart);
		c.set(Calendar.DATE, 1);
		quarterStart = calculateBeginTime(c);
		c.add(Calendar.MONTH, 3);
		c.add(Calendar.DATE, -1);
		quarterEnd = calculateEndTime(c);
		return new Date[]
		{
			quarterStart, quarterEnd
		};
	}
	
	/**
	 * <p>
	 * 功能描述：本年度起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getYearStartAndEndDayTime(Date date)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return new Date[]
		{
			calculateBeginDayOfYear(c), calculateEndDayOfYear(c)
		};
	}
	
	/**
	 * <p>
	 * 功能描述：计算改变年份后的年度起始时间
	 * 
	 * @param date
	 * @param years
	 * @return
	 */
	public static Date[] getYearInceptByChangeYears(Date date, int years)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.YEAR, years);
		return new Date[]
		{
			calculateBeginDayOfYear(c), calculateEndDayOfYear(c)
		};
	}
	
	// //////////////////////////////////////日期计算/////////////////////////////
	
	/**
	 * <p>
	 * 功能描述：计算日期的开始时间
	 * 
	 * @param c
	 * @return
	 */
	public static Date calculateBeginTime(Calendar c)
	{
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}
	
	public static Date getBeginTimeOfAddDays(Date date, int days)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return calculateBeginTime(c);
	}
	
	public static Date getEndTimeOfAddDays(Date date, int days)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return calculateEndTime(c);
	}
	
	public static Date getBeginTimeOfAddMonths(Date date, int months)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return calculateBeginTime(c);
	}
	
	public static Date getEndTimeOfAddMonths(Date date, int months)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, months);
		return calculateEndTime(c);
	}
	
	public static Date getBeginTimeOfAddYears(Date date, int years)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return calculateBeginTime(c);
	}
	
	public static Date getEndTimeOfAddYears(Date date, int years)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, years);
		return calculateEndTime(c);
	}
	
	/**
	 * <p>
	 * 功能描述：计算日期的结束时间
	 * 
	 * @param c
	 * @return
	 */
	public static Date calculateEndTime(Calendar c)
	{
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 999);
		return c.getTime();
	}
	
	/**
	 * <p>
	 * 功能描述：计算日期所在周的开始时间
	 * 
	 * @param c
	 * @return
	 */
	public static Date calculateBeginDayOfWeek(Calendar c)
	{
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
		return c.getTime();
	}
	
	/**
	 * <p>
	 * 功能描述：计算日期所在周的结束时间
	 * 
	 * @param c
	 * @return
	 */
	public static Date calculateEndDayOfWeek(Calendar c)
	{
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		return c.getTime();
	}
	
	/**
	 * <p>
	 * 功能描述：计算日期所在月的开始时间
	 * 
	 * @param c
	 * @return
	 */
	public static Date calculateBeginDayOfMonth(Calendar c)
	{
		c.set(Calendar.DAY_OF_MONTH, 1);; // Month first day
		return c.getTime();
	}
	
	/**
	 * <p>
	 * 功能描述：计算日期所在月的结束时间
	 * 
	 * @param c
	 * @return
	 */
	public static Date calculateEndDayOfMonth(Calendar c)
	{
		c.add(Calendar.MONTH, 1);
		c.set(Calendar.DAY_OF_MONTH, 0);; // Month last day
		return c.getTime();
	}
	
	public static Date calculateBeginDayOfYear(Calendar c)
	{
		c.set(Calendar.MONTH, 0);
		c.set(Calendar.DATE, 1);
		return calculateBeginTime(c);
	}
	
	public static Date calculateEndDayOfYear(Calendar c)
	{
		c.add(Calendar.YEAR, 1);
		c.add(Calendar.DATE, -1);
		return calculateEndTime(c);
	}
	
	public static Date addMinutes(Date date, int minutes)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MINUTE, minutes);
		return c.getTime();
	}
	
	public static Date addHours(Date date, int hours)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.HOUR, hours);
		return c.getTime();
	}
	
	public static Date addDates(Date date, int days)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, days);
		return c.getTime();
	}
	
	/**
	 * 计算两个日期之间的月差
	 * @throws Exception 
	 */
	public static int dispersionMonth(Object objDate1, Object objDate2) throws Exception{
		Date date1 = null;
		Date date2 = null;
		
		if(objDate1 instanceof String){
			date1 = DateFormat.getDateInstance().parse(objDate1.toString());
		}else if(objDate1 instanceof Date){
			date1 = (Date)objDate1;
		}else{
			throw new Exception("参数类型不正确!");
		}
		
		if(objDate2 instanceof String){
			date2 = DateFormat.getDateInstance().parse(objDate2.toString());
		}else if(objDate1 instanceof Date){
			date2 = (Date)objDate2;
		}else{
			throw new Exception("参数类型不正确!");
		}
		 
		return dispersionMonth(date1,date2);
	}
	
	/**
	 * 计算两个日期之间的月差
	 */
	public static int dispersionMonth(Date date1, Date date2)
	{
		int iMonth = 0;
		int flag = 0;
		Calendar objCalendarDate1 = Calendar.getInstance();
		objCalendarDate1.setTime(date1);
		Calendar objCalendarDate2 = Calendar.getInstance();
		objCalendarDate2.setTime(date2);
		
		if(objCalendarDate2.equals(objCalendarDate1))
			return 0;
		if(objCalendarDate1.after(objCalendarDate2))
		{
			Calendar temp = objCalendarDate1;
			objCalendarDate1 = objCalendarDate2;
			objCalendarDate2 = temp;
		}
		if(objCalendarDate2.get(Calendar.DAY_OF_MONTH) < objCalendarDate1
			.get(Calendar.DAY_OF_MONTH))
			flag = 1;
		
		if(objCalendarDate2.get(Calendar.YEAR) > objCalendarDate1
			.get(Calendar.YEAR))
			iMonth = ((objCalendarDate2.get(Calendar.YEAR) - objCalendarDate1
				.get(Calendar.YEAR))
				* 12 + objCalendarDate2.get(Calendar.MONTH) - flag)
				- objCalendarDate1.get(Calendar.MONTH);
		else
			iMonth = objCalendarDate2.get(Calendar.MONTH)
				- objCalendarDate1.get(Calendar.MONTH) - flag;
		return iMonth;
	}
	
	
	/**
	 * 验证时间格式是否符合要求 
	 * @param time 待验证的字符串
	 * @param checkFormat 目标格式 (比如 yyyy/MM/dd HH:mm:ss, yyyy-MM-dd HH:mm:ss)
	 * @return true:符合  false:不符合
	 */
	public static boolean checkTimeFormat(String time,String checkFormat)
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss");  
	    @SuppressWarnings("unused")
		Date d = null;  
        if(time != null && !time.equals(""))  
        {  
            if(time.split("/").length > 1)  
            {  
                dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");  
            }  
            if (time.split("-").length > 1)  
            {  
            	dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
            }  
            try  
            {  
                d = dateFormat.parse(time);  
                return true;
            }  
            catch(Exception e)  
            {  
//                System.out.println("格式错误");  
                return false;  
            }  
        }
        else  
        {  
            return false;  
        }  
	}
	
	
	/**
	 * 将指定日期转化为秒数
	 * @param expireDate 日期 比如 2012-07-04 
	 * @return 毫秒数,比如 1341331200000
	 */
    public static long getMilliSecondsFromDate(String expireDate)
    {
        if(expireDate==null||expireDate.trim().equals("") || "0000-00-00".equals(expireDate))
        {
        	return 0;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date=null;
        try{
        	date = sdf.parse(expireDate);
            return date.getTime();
        }
        catch(ParseException e)
        {
            return 0;
        }
    }

    //获得某一日期的前一天
    public static Date getPreviousDate(Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        int day=calendar.get(Calendar.DATE);
        calendar.set(Calendar.DATE,day-1);
        return calendar.getTime();
    }

    public static void main(String[] args)
    {
    	long tt = CalendarUtil.getMilliSecondsFromDate("2012-07-04");
    	System.out.println(tt);
    	tt = CalendarUtil.getMilliSecondsFromDate("2012-07-08");
    	System.out.println(tt);
    }
}
