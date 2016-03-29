package com.manage.util;


import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;

/**
 * @author cwc
 * 
 */
public class ConfigUtils {
	private static Properties globalProperties = new Properties();

	static {
		try {
			InputStream in = ConfigUtils.class.getResourceAsStream("/generic.properties");
			globalProperties.load(in);
			in.close();
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static String getValue(String key) {
		return globalProperties.get(key).toString();
	}

	public static String getHtmlDir() {
		return getValue("html.dir");
	}
	public static String getHtmlTemplate()
	{
		return getValue("html.template");
	}
	public static String getWsTemplate()
	{
		return getValue("ws.template");
	}
	public static String getPdf2SwfExeDir()
	{
		return getValue("pdf2swf.dir");
	}
	public static Integer getOpenOfficePort()
	{
		if(StringUtils.isBlank(getValue("openoffice.port")))
			return null;
		return Integer.parseInt(getValue("openoffice.port"));
	}
	
	public static String getAttachPreUrl()
	{
		return getValue("attach_prefix_url");
	}
	public static Integer getCtNounFmtCount()
	{
		if(StringUtils.isBlank(getValue("content.noun.count")))
			return null;
		return Integer.parseInt(getValue("content.noun.count"));
		
	}
	public static String getCtNounFmtCls()
	{
		return getValue("content.noun.class");
	}
	public static String getCtNounFmtFuncs()
	{
		return getValue("content.noun.funcs");
	}
	public static boolean getDireccityCheckNeed()
	{
		if(StringUtils.isBlank(getValue("direccity.check.need")))
			return true;
		return Boolean.parseBoolean(getValue("direccity.check.need"));
	}
	public static boolean getAreaCheckNeed()
	{
		if(StringUtils.isBlank(getValue("area.check.need")))
			return true;
		return Boolean.parseBoolean(getValue("area.check.need"));
	}
	public static Integer getPhoneRoleId()
	{
		if(StringUtils.isBlank(getValue("phone.role.id")))
			return null;
		return Integer.parseInt(getValue("phone.role.id"));
	}
}
