package com.solr.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;

/**
 * URL地址拼接工具类 负责url地址的参数动态加载
 */
public class UrlTool {

	/**
	 * 获取并且格式化URL
	 */
	public static String getAndformatUrl(HttpServletRequest req, boolean isD) {
		String url = req.getRequestURI();
		try {
			String qs = req.getQueryString();
			if (isD) {
				qs = deC(qs);
			}// 解码
			if (!url.endsWith(".html")) {
				if (url.endsWith("/")) {
					url = url + "0-0-0-0-0-0-0-0-0-0.html";
				} else {
					url = url + "/0-0-0-0-0-0-0-0-0-0.html";
				}
			}
			if (StringUtils.isNotBlank(qs)) {
				url = url + "?" + qs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	public static String utf8UrlEncode(String text) {
        StringBuffer result = new StringBuffer();
        for (int i = 0; i < text.length(); i++) {
	        char c = text.charAt(i);
	        if (c >= 0 && c <= 255) {
	        result.append(c);
	        }else {
		        byte[] b = new byte[0];
		        try {
		        b = Character.toString(c).getBytes("UTF-8");
		        }catch (Exception ex) {
		        }
			        for (int j = 0; j < b.length; j++) {
				        int k = b[j];
				        if (k < 0) k += 256;
				        result.append("%" + Integer.toHexString(k).toUpperCase());
			        }
	        }
        }
        return result.toString();
     }

	/**
	 * 获取并且格式化URL
	 */
	public static String getAndformatUrl(HttpServletRequest req, boolean isDecode, String formatUrl) {
		String url = req.getRequestURI();
		try {
			String qs = req.getQueryString();
			if (isDecode) {
				qs = deC(qs);
			}// 解码
			if (!url.contains(".html")) {
				url = url + formatUrl;
			}
			if (StringUtils.isNotBlank(qs)) {
				url = url + "?" + qs;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return url;
	}

	/**
	 * 获取并且格式化URL,考虑如果url地址不够长,则补充到足够长度
	 */
	public static String getAndformatUrl(HttpServletRequest req, int length, boolean isD) {
		String url = req.getRequestURI();
		StringBuffer sb = new StringBuffer();
		try {
			String qs = req.getQueryString();
			if (isD) {
				qs = deC(qs);
			}// 解码
			if (!url.endsWith(".html")) {
				sb.append(url);
				for (int i = 0; i < length - 1; i++) {
					sb.append("0-");
				}
				sb.append("0.html");
			} else {
				sb.append(url);
			}
			if (StringUtils.isNotBlank(qs)) {
				sb.append("?").append(qs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 解码
	 */
	public static String deC(String qs) {
		if (StringUtils.isNotBlank(qs)) {
			try {
				qs = URLDecoder.decode(qs, "UTF-8");
			} catch (Exception e) {
				qs = "\"\"";// 如果存在不能转码的数据,则直接赋值为"";
			}
		}
		return qs;
	}
}
