/**
 * 
 */
package com.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * AWK代理服务
 */
public class AwkAgent {
	/**
	 * 日志查询的awk组装
	 * 
	 * @param splitString
	 *            列分隔符
	 * @param conditions
	 *            条件，为Map，key为列号，从1开始，value为查询正则表达式，意为第几列符合正则表达式条件
	 * @param files
	 *            文件全路径数组，可包含正则表达式
	 * @return 字符串列表，没个元素为一行完整的日志
	 */
	public static List<String> logAwk(String splitString, Map<Integer, String> conditions, String[] files) {
		List<String> lines = new ArrayList<String>();
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("awk -F \"" + splitString + "\"");
			boolean first = true;
			builder.append(" '");
			for (Entry<Integer, String> entry : conditions.entrySet()) {
				if (first) {
					builder.append("$" + entry.getKey() + "~/" + entry.getValue() + "/");
					first = false;
				} else {
					builder.append(" && $" + entry.getKey() + "~/" + entry.getValue() + "/");
				}

			}
			builder.append("'");
			for (String file : files) {
				builder.append(" " + file);
			}
			System.out.println("builder===  "+builder);
			Runtime rt = Runtime.getRuntime();
			String[] cmd = new String[] { "/bin/bash", "-c", builder.toString() };
			Process pross = rt.exec(cmd);
			BufferedReader reader = new BufferedReader(new InputStreamReader(pross.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				lines.add(line);
			}
			pross.waitFor();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
		return lines;
	}
}
