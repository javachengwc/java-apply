package com.util.regex;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class RegexUtil {

	public static final Pattern CELL_PHONE_PATTERN = Pattern.compile(CommonRegex.CELL_PHONE);
	public static final Pattern TELE_PHONE_PATTERN = Pattern.compile(CommonRegex.TELE_PHONE);
	
	public static final Pattern ID_CARD_NO_PATTERN = Pattern.compile(CommonRegex.ID_CARD_NO);
	
	public static final Pattern BANK_ACCOUNT_PATTERN = Pattern.compile(CommonRegex.BANK_ACCOUNT);
	
	public static final Pattern QQ_PATTERN = Pattern.compile(CommonRegex.QQ);
	
	public static final Pattern IP_PATTERN = Pattern.compile(CommonRegex.IP);

    public static final Pattern PATTERN_OF_CONTAINS_IP = Pattern.compile(CommonRegex.REG_EXP_OF_CONTAINS_IP );

    //懒惰匹配 懒惰匹配，也就是匹配尽可能少的字符
    //懒惰匹配模式，只要在它后面加上一个问号?,这样.*?就意味着匹配任意数量的重复，但是在能使整个匹配成功的前提下使用最少的重复
    //懒惰匹配例子--匹配最短的，以a开始，以b结束的字符串
    public static final Pattern LAZY_PATIERN= Pattern.compile("a.*?b");
	
	/**
	 * 手机号判断
	 * @param input
	 * @return
	 */
	public static boolean isCellPhone(String input) {
		if (StringUtils.isNotBlank(input)) {
			if (CELL_PHONE_PATTERN.matcher(input).matches()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 座机号码判断
	 * <pre>
	 * 88886666, 8888666
	 * 028-88886666
	 * 028-88886666-1234
	 * </pre>
	 * @param input
	 * @return
	 */
	public static boolean isTelePhone(String input) {
		if (StringUtils.isNotBlank(input)) {
			if (TELE_PHONE_PATTERN.matcher(input).matches()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 电话号码判断, 包括手机号 和 座机号
	 * @param input
	 * @return
	 */
	public static boolean isPhoneNum(String input) {
		if (StringUtils.isNotBlank(input)) {
			return isCellPhone(input) || isTelePhone(input);
		}
		return false;
	}
	
	/**
	 * 身份证号码判断
	 * @param input
	 * @return
	 */
	public static boolean isIDCardNo(String input) {
		if (StringUtils.isNotBlank(input)) {
			if (ID_CARD_NO_PATTERN.matcher(input).matches()) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 银行卡号判断
	 * @param input
	 * @return
	 */
	public static boolean isBankAccount(String input) {
		if (StringUtils.isNotBlank(input)) {
			if (BANK_ACCOUNT_PATTERN.matcher(input).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public static boolean isQQ(String input) {
		if (StringUtils.isNotBlank(input)) {
			if (QQ_PATTERN.matcher(input).matches()) {
				return true;
			}
		}
		return false;
	}
	
	public static String fetchIpFromText(String input) {
		return IP_PATTERN.matcher(input).group();
	}
}
