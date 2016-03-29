package com.manage.model;

/**
 * 标志位的枚举所需实现的接口，一个枚举对应一个位，最多64位
 * 
 */
public interface IOneBitEnum {

	/**
	 * 该标志位是第几位，取值：0－63
	 */
	int getPosition();
	
	String getName();
}
