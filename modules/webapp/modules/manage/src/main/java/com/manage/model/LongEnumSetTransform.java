package com.manage.model;

import java.util.EnumSet;

/**
 * long和EnumSet互相转换的工具类
 */
public class LongEnumSetTransform {

	/**
	 * 长整数转化为EnumSet 转化规则：
	 * <li>如果enumType是IOneBitEnum，转化为IOneBitEnum的EnumSet</li>
	 * <li>如果enumType是Enum，转化为空的EnumSet</li>
	 * <li>如果enumType不是Enum，抛出异常</li>
	 * 
	 * @param <T>
	 * @param enumType
	 *            枚举类型
	 * @param value
	 *            待转换的长整数值
	 * @return EnumSet
	 */
	public static <T extends Enum<T>> EnumSet<T> long2EnumSet(Class<T> enumType, long value) {
		if (enumType.isEnum()) {
			if (IOneBitEnum.class.isAssignableFrom(enumType)) {
				return long2OneBitEnumSet(enumType, value);
			}
			return EnumSet.noneOf(enumType);
		} else {
			throw new IllegalArgumentException(enumType + " not enum type");
		}
	}

	/**
	 * EnumSet转化为长整数转化规则：
	 * <li>如果EnumSet的元素是IOneBitEnum，返回所有IOneBitEnum的getValue的累加值</li>
	 * <li>否则返回0</li>
	 * 
	 * @param set
	 * @return long
	 */
	public static Long enumSet2Long(EnumSet<?> set) {
		if (set == null || set.size() == 0) {
			return 0l;
		}
		for (Enum<?> enum1 : set) {
			Class<?> enumType = enum1.getClass();
			if (IOneBitEnum.class.isAssignableFrom(enumType)) {
				return oneBitEnumSet2Long(set);
			}
		}
		return 0l;
	}

	/**
	 * IOneBitEnum的EnumSet转化为long
	 * 
	 * @param set
	 * @return
	 */
	private static Long oneBitEnumSet2Long(EnumSet<?> set) {
		long value = 0;
		for (Enum<?> enum1 : set) {
			IOneBitEnum ob = (IOneBitEnum) enum1;
			value += 1 << ob.getPosition();
		}
		return value;
	}

	/**
	 * 长整数转化为IOneBitEnum的EnumSet
	 * 
	 * @param <T>
	 * @param enumType
	 * @param value
	 * @return
	 */
	private static <T extends Enum<T>> EnumSet<T> long2OneBitEnumSet(Class<T> enumType, long value) {
		EnumSet<T> set = EnumSet.noneOf(enumType);
		T[] es = enumType.getEnumConstants();
		for (T e : es) {
			IOneBitEnum ob = (IOneBitEnum) e;
			if ((1 << ob.getPosition() & value) > 0) {
				set.add(e);
			}
		}
		return set;
	}

}
