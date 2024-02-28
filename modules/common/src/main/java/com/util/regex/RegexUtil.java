package com.util.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public final class RegexUtil {

	public static final Pattern CELL_PHONE_PATTERN = Pattern.compile(CommonRegex.CELL_PHONE);

	public static final Pattern TELE_PHONE_PATTERN = Pattern.compile(CommonRegex.TELE_PHONE);
	
	public static final Pattern BANK_ACCOUNT_PATTERN = Pattern.compile(CommonRegex.BANK_ACCOUNT);
	
	public static final Pattern QQ_PATTERN = Pattern.compile(CommonRegex.QQ);
	
	public static final Pattern IP_PATTERN = Pattern.compile(CommonRegex.IP);

    public static final Pattern PATTERN_OF_CONTAINS_IP = Pattern.compile(CommonRegex.REG_EXP_OF_CONTAINS_IP );

    //懒惰匹配 懒惰匹配，也就是匹配尽可能少的字符
    //懒惰匹配模式，只要在它后面加上一个问号?,这样.*?就意味着匹配任意数量的重复，但是在能使整个匹配成功的前提下使用最少的重复
    //懒惰匹配例子--匹配最短的，以a开始，以b结束的字符串
    public static final Pattern LAZY_PATIERN= Pattern.compile("a.*?b");
	
	//手机号判断
	public static boolean isCellPhone(String input) {
		if (StringUtils.isNotBlank(input)) {
			if (CELL_PHONE_PATTERN.matcher(input).matches()) {
				return true;
			}
		}
		return false;
	}
	
	//验证身份证号
    public static boolean checkIdCard(String idCard)
    {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex,idCard);
    }

    //银行卡号判断
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

    // 验证中文
    public static boolean checkChinese(String chinese) {
        String regex = "^[\u4E00-\u9FA5]+$";
        return Pattern.matches(regex,chinese);
    }

    //验证空白字符，包括：空格、\t、\n、\r、\f、\x0B
    public static boolean checkBlankSpace(String blankSpace) {
        String regex = "\\s+";
        return Pattern.matches(regex,blankSpace);
    }

    //分隔符分割
	public static String[] splitString(String string){
		if (StringUtils.isEmpty(string)){
			return null;
		}
		Pattern pattern = Pattern.compile(CommonRegex.CUTOFF);
		Matcher matcher = pattern.matcher(string.trim());
		if (matcher.find()){
			return string.split(CommonRegex.CUTOFF);
		}else {
			return new String[]{string};
		}
	}

}
