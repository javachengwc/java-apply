package com.util.date;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SysDateTime
{
	private static Logger m_logger = Logger.getLogger(SysDateTime.class);
	
	public static String getNowString()
	{
		return Long.toString((new Date()).getTime() / 1000);
	}
	
	/**
	 * Get current time in second
	 * 
	 * @return
	 */
	public static int getNow()
	{
		return (int)((new Date()).getTime() / 1000);
	}
	/**
	 * yyyy-MM-dd HH:mm:ss
	 * 
	 * @return
	 */
	public static String getDatetime()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return(formatter.format(new Date()));
	}
	
	public static String getDatetime(Date datetime)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formatter.format(datetime);
	}
	
	public static String getDatetime(Long date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return(formatter.format(new Date(date)));
	}
	
	public static int getDatetime(String dateStr)
		throws Exception
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(dateStr);
		return (int)(date.getTime() / 1000);
	}
	
	public static String getDate()
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return(formatter.format(new Date()));
	}
	
	public static Date getDate(String sDate)
			throws Exception
	{
		return getDate(sDate,"yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date getDate(String sDate,String fmt)
		throws Exception
	{
		if(fmt == null)fmt = "yyyy-MM-dd";
		SimpleDateFormat formatter = new SimpleDateFormat(fmt);
		return formatter.parse(sDate);
	}
	
	public static String getDate(Date date)
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		return formatter.format(date);
	}
	
	/**
	 * 获得到传入时间日期 yyyy-MM-dd
	 * @param datetime
	 * @return
	 * @throws Exception
	 */
	public static String getDateOnly(String datetime)
			throws Exception
	{
		String[] ss = datetime.split("\\s");
		if(ss.length >= 1)
		{
			return ss[0];
		}
		return null;
	}
	
	public static String getDatetime_millisecond()
	{
		SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss.S");
		return(formatter.format(new Date()));
	}
	
	public static boolean checkString(String datetime)
		throws Exception
	{
		try
		{
			SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
			formatter.parse(datetime);
			return true;
		}
		catch(ParseException ex)
		{
			return false;
		}
	}
	
	// 如果格式不是很规范，如6月没有写成06等，转换成规范的格式
	public static String adjust(String s)
		throws Exception
	{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = formatter.parse(s);
		return formatter.format(new Date(date.getTime()));
	}
	
	@SuppressWarnings("unused")
	private static int getCalendarWeekday(int theday)
	{
		if(theday == 1)
			return Calendar.MONDAY;
		if(theday == 2)
			return Calendar.TUESDAY;
		if(theday == 3)
			return Calendar.WEDNESDAY;
		if(theday == 4)
			return Calendar.THURSDAY;
		if(theday == 5)
			return Calendar.FRIDAY;
		if(theday == 6)
			return Calendar.SATURDAY;
		if(theday == 7)
			return Calendar.SUNDAY;
		return -1;
	}
	
	public static String formatDate(Date date, String pattern, Locale loc)
	{
		if(pattern == null)
		{
			throw new IllegalArgumentException("parameter is invalid.");
		}
		String newDate = "";
		if(loc == null)
		{
			loc = Locale.getDefault();
		}
		if(date != null)
		{
			SimpleDateFormat sdf = new SimpleDateFormat(pattern, loc);
			newDate = sdf.format(date);
		}
		return newDate;
	}
	
	public static String formatDate(Date date, String pattern)
	{
		return formatDate(date, pattern, null);
	}
	/**
	 * 加月份
	 * @param date
	 * @param month
	 * @return
	 */
	public static String addMonthDate(Date date,int month){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.MONTH, month);
		return formatDate(c.getTime(), "yyyy-MM-dd");
	}
	
	/**
	 * 加日
	 * @param date
	 * @param day
	 * @return
	 */
	public static String addDayDate(Date date,int day){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, day);
		return formatDate(c.getTime(), "yyyy-MM-dd");
	}
	
	
	/**
	 * 加年
	 * @param date
	 * @param year
	 * @return
	 */
	public static String addYearDate(Date date,int year){
		Calendar c=Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.YEAR, year);
		return formatDate(c.getTime(), "yyyy-MM-dd");
	}
	
	
	/**
	 * 获取date时间天数以上的毫秒数
	 * 
	 * @param date
	 * @return
	 */
	public static long getDayTime(Date date)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		ca.set(Calendar.HOUR_OF_DAY, 0); // 小时取0
		ca.set(Calendar.MINUTE, 0); // 分取0
		ca.set(Calendar.SECOND, 0); // 秒取0
		ca.set(Calendar.MILLISECOND, 0); // 毫秒取0
		return ca.getTime().getTime();
	}
	
	/**
	 * 判断时间是不是整点
	 * @param date
	 * @return
	 */
	public static boolean isIntegerTime(Date date)
	{
		Calendar ca = Calendar.getInstance();
		ca.setTime(date);
		return ca.get(Calendar.MINUTE)==0 && ca.get(Calendar.SECOND)==0;
	}
	
	
	/**
	 * 两个日期相差的天数
	 */
	public static long getDayDifference(Date date1, Date date2)
	{
		return Math.abs(getDateTimeDifference(date1, date2, (1000 * 60 * 60 * 24)));
	}
	
	/**
	 * 两个日期相差
	 */
	public static long getDateTimeDifference(Date date1, Date date2, long unit)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date1);
		long timethis = calendar.getTimeInMillis();
		calendar.setTime(date2);
		long timeend = calendar.getTimeInMillis();
		long difference = (timeend - timethis) / unit;
		return difference;
	}
	
	/**
	 * 两个日期相差的小时数
	 */
	public static long getHourDifference(Date date1, Date date2)
	{
		return Math.abs(getDateTimeDifference(date1, date2, (1000 * 60 * 60)));
	}
	
	/**
	 * 两个日期相差的分钟数
	 */
	public static long getMinuteDifference(Date date1, Date date2)
	{
		return Math.abs(getDateTimeDifference(date1, date2, (1000 * 60)));
	}
	
	/**
	 * 两个日期相差的秒数
	 */
	public static long getSecondDifference(Date date1, Date date2)
	{
		return Math.abs(getDateTimeDifference(date1, date2, (1000)));
	}

	/**
	 * 微博用到的动态日期格式
	 */
	public static String formatDateForBlog(Date date1)
	{
		Date now = new Date();
		long second = getSecondDifference(now, date1);
		if(second < 60){
			return second+"秒前";
		}
		long minute = getMinuteDifference(now, date1);
		if(minute < 60){
			return minute+"分钟前";
		}
		Date todayEnd = CalendarUtil.getLastDateTimeOfDay(now);
		Date todayBegin = CalendarUtil.getFirstDateTimeOfDay(now);
		if(date1.after(todayBegin) && date1.before(todayEnd)){
			return "今天 "+ SysDateTime.formatDate(date1, "HH:mm");
		}
		Date[] years = CalendarUtil.getYearStartAndEndDayTime(now);
		if(date1.after(years[0]) && date1.before(years[1])){
			return SysDateTime.formatDate(date1, "M月d日 ")+ SysDateTime.formatDate(date1, "HH:mm");
		}
		return SysDateTime.formatDate(date1, "yyyy-MM-dd HH:mm");
	}
	
	/**
	 * 判断传进来的一日期于今天是否相隔在一个指定时间之内
	 * @param time 时间
	 * @param day 多少天
	 * @return
	 * @throws java.text.ParseException
	 */
	public static boolean getDaysBetween(String time,int day) throws ParseException
	{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date date = sdf.parse(time);
			Date today = new Date();
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Calendar calendar2 = Calendar.getInstance();
			calendar2.setTime(today);
			int year2 = calendar2.get(Calendar.YEAR);
			if(calendar.get(Calendar.YEAR) == year2)
			{
				int days = calendar2.get(Calendar.DAY_OF_YEAR)
					- calendar.get(Calendar.DAY_OF_YEAR);
				if(days < day)
					return true;
			}
		return false;
	}
	
	/**
	 * 把string型的日期转成long
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public static long getTime(String time) throws Exception{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse(time);
		long ret = date.getTime();
		return ret;
	}
	
	
	/**
	 * 得到一当前日期的周一和周日的时期
	 * @param time
	 * @return
	 * @throws Exception
	 */
	public static HashMap<String,String> fisrtLastDayByWeek(long time) throws Exception{
		HashMap<String, String> prop = new HashMap<String,String>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd",Locale.CHINA);
		Calendar cl = Calendar.getInstance(Locale.CHINA);
		cl.setFirstDayOfWeek(Calendar.MONDAY);
		cl.setTimeInMillis(time);
		cl.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
		String monday = sf.format(cl.getTime());
		prop.put("monday", monday);
		cl.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
		String sunday = sf.format(cl.getTime());
		prop.put("sunday", sunday);
		return prop;
	}
	
	/**
	 * 把日期变成	时分秒	[手机倒计时用 ]
	 * @param date
	 * @return
	 */
	public static String formatLongToTimeStr(Long date) {        
		int hour = 0;
		int minute = 0;
		int second = 0;
		second = date.intValue() / 1000;
		if (second > 60) {
			minute = second / 60;
			second = second % 60;
		}
		if (minute > 60) {
			hour = minute / 60;
			minute = minute % 60;
		}
		String strtime = "";
		
		if(hour<10){
			strtime += "0"+hour + "时"; 
		}else{
			strtime += hour + "时";
		}
		
		if(minute<10){
			strtime += "0"+minute + "分"; 
		}else{
			strtime += minute + "分";
		}
		
		if(second<10){
			strtime += "0"+second + "秒"; 
		}else{
			strtime += second + "秒";
		}
		
		
		return strtime; 

		}

	public static void main(String[] args) throws Exception{
		System.out.println(formatLongToTimeStr(6000L));
	}
	/**
	 * 把日期变成	天时分秒	[手机倒计时用 ]
	 * @param mss
	 * @return
	 */
	public static String formatDuring(long mss) {
		String days = mss / (1000 * 60 * 60 * 24) + "";
		if (days.length() == 1) {
			days = "0" + days;
		}
		String hours = (mss % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60) + "";
		if (hours.length() == 1) {
			hours = "0" + hours;
		}
		String minutes = (mss % (1000 * 60 * 60)) / (1000 * 60) + "";
		if (minutes.length() == 1) {
			minutes = "0" + minutes;
		}
		String seconds = (mss % (1000 * 60)) / 1000 + "";
		if (seconds.length() == 1) {
			seconds = "0" + seconds;
		}
		return days + "天" + hours + "时" + minutes + "分" + seconds + "秒";
	}
	
}
