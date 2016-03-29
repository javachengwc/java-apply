package com.httpclient.util;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpClientCookieUtil {

	/**
	 * 特殊字符值未转意,慎用
	 */
	public static String cookieTransStr(Cookie[] cookies) {
		if (cookies == null || cookies.length <= 0) {
			return "";
		}
		StringBuffer buf = new StringBuffer();
		for (Cookie ck : cookies) {
			buf.append(ck.getName()).append("=").append(ck.getValue())
					.append("; ");
			buf.append("domain").append("=").append(ck.getDomain())
					.append("; ");
			if (ck.getExpiryDate() != null) {
				buf.append("expires").append("=")
						.append(toGMT(ck.getExpiryDate())).append("; ");
			}
			buf.append("path").append("=").append(ck.getPath()).append(", ");
		}
		String rt = buf.toString();
		rt = rt.substring(0, rt.length() - 2);
		return rt;
	}

	/**
	 * cookie字符串转换成cookie列表
	 * 
	 * @param cookie
	 * @param defaultPath
	 * @param defaultDomain
	 * @return
	 */
	public static List<Cookie> strTransCookie(String cookie, String defaultPath,
			String defaultDomain) {
		List<Cookie> cookies = new ArrayList<Cookie>();
		if (StringUtils.isBlank(cookie)) {
			return cookies;
		}
		String perCookies[] = cookie.split(",");

		for (String per : perCookies) {
			String[] tempCookies = per.split(";");
			String tempCookie = null;
			int equallength = 0;// =的位置

			String key = null;
			String value = null;
			String domain = null;
			String path = null;
			String expires = null;
			for (int i = 0; i < tempCookies.length; i++) {
				String cookieKey = null;
				String cookieValue = null;
				if (StringUtils.isBlank(tempCookies[i])) {
					continue;
				}
				tempCookie = tempCookies[i];

				equallength = tempCookie.indexOf("=");

				if (equallength >= 0) // 有可能cookie
										// 无=，就直接一个cookiename；比如:a=3;ck;abc=;
				{

					cookieKey = tempCookie.substring(0, equallength).trim();
					// cookie=

					if (equallength == tempCookie.length() - 1) // 这种是等号后面无值，如：abc=;
					{
						cookieValue = "";
					} else {
						cookieValue = tempCookie.substring(equallength + 1)
								.trim();
					}
				} else {
					cookieKey = tempCookie.trim();
					cookieValue = "";
				}
				if ("Domain".equalsIgnoreCase(cookieKey)) {
					domain = ((StringUtils.isBlank(cookieValue)) ? defaultDomain
							: cookieValue);

				} else if ("Path".equalsIgnoreCase(cookieKey)) {
					path = ((StringUtils.isBlank(cookieValue)) ? defaultPath
							: cookieValue);

				} else if ("expires".equalsIgnoreCase(cookieKey)) {
					expires = cookieValue;
				} else {
					key = cookieKey;
					value = cookieValue;
				}
			}
			Cookie ck = new Cookie();

			ck.setName(key);
			ck.setValue(value);
			ck.setPath((StringUtils.isBlank(path) ? "/" : path));
			ck.setDomain(domain);
			if (!StringUtils.isBlank(expires)) {
				Date d = GMTStrToDate(expires);
				if (d != null) {
					ck.setExpiryDate(d);
				}
			}
			cookies.add(ck);
		}
		return cookies;
	}

	/**
	 * 转化为标准时间字符串
	 * 
	 * @param date
	 * @return
	 */
	public static final String toGMT(Date date) {
		Locale aLocale = Locale.US;
		DateFormat fmt = new SimpleDateFormat("EEE,d MMM yyyy hh:mm:ss z",
				new DateFormatSymbols(aLocale));
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		return fmt.format(date);
	}

	/**
	 * 标准时间字串转换为时间
	 * 
	 * @return
	 */
	public static final Date GMTStrToDate(String str) {
		Locale aLocale = Locale.US;
		DateFormat fmt = new SimpleDateFormat("EEE,d MMM yyyy hh:mm:ss z",
				new DateFormatSymbols(aLocale));
		fmt.setTimeZone(TimeZone.getTimeZone("GMT"));
		try {
			Date d = fmt.parse(str);
			return d;
		} catch (Exception e) {
			return null;
		}
	}

	public static void main(String args[]) {
		String tt = "Wed, 20 Aug 2014 08:04:09 GMT";
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(format.format(GMTStrToDate(tt)));
		String cookieStr = "a=1694556006%40qq.com; Domain=.cc.com; Path=/, "
				+ "b=lichengyan; Domain=.cc.com; Path=/, "
				+ "c=212265; Domain=.cc.com; Path=/, "
				+ "d=1; Domain=.cc.com; Path=/, "
				+ "e=e; Domain=.cc.com; Path=/";
		List<Cookie> list =strTransCookie(cookieStr,null,null);
		 System.out.println(list.size());
        System.out.println(cookieTransStr(list.toArray(new Cookie[list.size()])));
        
	}
	
	public static String getCookieValue(String key,HttpServletRequest request)
    {
    	String value="";
    	javax.servlet.http.Cookie [] cks =request.getCookies();
		if(cks!=null && cks.length>0)
		{
			for(javax.servlet.http.Cookie ck:cks)
			{
				if(key.equals(ck.getName()))
				{
					value = ck.getValue();
					break;
				}
			}
		}
		return value;
    }
	
	public static void addCookie(javax.servlet.http.Cookie c,String path,String domian,HttpServletResponse response)
    {
		c.setPath("/");
		c.setDomain(domian);
		response.addCookie(c);
    }
}
