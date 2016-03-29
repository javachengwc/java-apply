package com.solr.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class DataFilter {
	
	// 过滤特殊字符
	public static String stringFilter(String str) throws PatternSyntaxException {   
		// 只允许字母和数字		
	    // String regEx = "[^a-zA-Z0-9]";                   
	    // 清除掉所有特殊字符
		String regEx="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？\"]";
		Pattern p = Pattern.compile(regEx);   
		Matcher m = p.matcher(str);   
		return m.replaceAll("").trim();   
	}   
	
	//过滤 UTF-8字符
	public static String stripNonCharCodepoints(String input) {
		StringBuilder retval = new StringBuilder();
		char ch;
		for (int i = 0; i < input.length(); i++) {
			ch = input.charAt(i);
			// Strip all non-characters
			// http://unicode.org/cldr/utility/list-unicodeset.jsp?a=[:Noncharacter_Code_Point=True:]
			// and non-printable control characters except tabulator, new line
			// and carriage return
			if (ch % 0x10000 != 0xffff && // 0xffff - 0x10ffff range step
											// 0x10000
					ch % 0x10000 != 0xfffe && // 0xfffe - 0x10fffe range
					(ch <= 0xfdd0 || ch >= 0xfdef) && // 0xfdd0 - 0xfdef
					(ch > 0x1F || ch == 0x9 || ch == 0xa || ch == 0xd)) {

				retval.append(ch);
			}
		}
		return retval.toString();
	}
}
