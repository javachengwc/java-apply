package com.xml.simple;

import org.simpleframework.xml.core.Persister;

public class XmlUtil {

	public static <T> T parseFromXml(T entry, String file) throws Exception {
		return new Persister().read(entry, XmlUtil.class.getResourceAsStream(file));
	}
	
}
