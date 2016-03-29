package com.util;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 判断对象是否为空
 */
public class BlankUtil {

	/**
	 * 判断字符串是否为空
	 * @return 空:true 否则：false
	 */
	public static boolean isBlank(final String str) {
		return (str == null) || (str.trim().length() <= 0);
	}

	/**
	 * 判断字符是否为空
	 */
	public static boolean isBlank(final Character cha){
		return (cha==null) || cha.equals(' ');
	}

	/**
	 * 判断对象是否为空
	 */
	public static boolean isBlank(final Object obj) {
		return (obj==null);
	}

	/**
	 * 判断数组是否为空
	 */
	public static boolean isBlank(final Object[] objs) {
		return (objs == null) || (objs.length <= 0);
	}

	/**
	 * 判断Collectionj是否为空
	 */
	public static boolean isBlank(final Collection<Object> obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * 判断Set是否为空
	 */
	public static boolean isBlank(final Set<Object> obj) {
		return (obj == null) || (obj.size() <= 0);
	}

	/**
	 * 判断Serializable是否为空
	 */
	public static boolean isBlank(final Serializable obj) {
		return obj == null;
	}

	/**
	 * 判断Map是否为空
	 */
	public static boolean isBlank(final Map<Object, Object> obj) {
		return (obj == null) || (obj.size() <= 0);
	}

 	/**
	 * 过滤特殊字符设置到SQL语句中的字符串(过滤的字符类似 ',")
	 * @param str 待处理字符串
	 */
	public final static String toDbFilter(String str)
	{
		return BlankUtil.trimString(str).replaceAll("\\\'","''");
	}
	
	/**
	 * 去掉字符串左右两边的空格,返回一个对象的字符串
	 */
	public static String trimString(Object obj)
	{
		return (obj == null) ? "" : String.valueOf(obj).trim();
	}
	
	/**
	 * 是否有为空的String
	 */
	public static boolean isAnyBlank(String ...args)
	{
		for(String p:args)
		{
			if(isBlank(p))
			{
				return true;
			}	
		}
		return false;
	}
}
