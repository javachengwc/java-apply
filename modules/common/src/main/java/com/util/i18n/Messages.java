package com.util.i18n;

import java.io.File;
import java.util.LinkedList;
import java.util.ResourceBundle;

public class Messages {
	
	private static ResourceBundle[] resources;
	
	static {
		try {
			File dir = new File(Messages.class.getResource("/i18n").toURI());
			if (dir.isDirectory()) {
				File[] files = dir.listFiles();
				LinkedList<ResourceBundle> rbs = new LinkedList<ResourceBundle>();
				for (File file : files) {
					try {
						String name = file.getName();
						int index = name.lastIndexOf(".");
						if (index > 0) {
							name = name.substring(0, index);
						}
						ResourceBundle rb = ResourceBundle.getBundle("i18n." + name);
						if (rb != null) {
							if ("ihome".equals(name)) {
								rbs.addFirst(rb);
							} else {
								rbs.add(rb);
							}
						}
					} catch (Exception e) {
						e.printStackTrace(System.out);
					}
				}
				if (rbs.size() > 0) {
					resources = rbs.toArray(new ResourceBundle[rbs.size()]);
				} else {
					resources=new ResourceBundle[0];
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}

	private Messages() {
	}

	public static String getString(String key) {
		for (ResourceBundle resource : resources) {
			try {
			String value = resource.getString(key);
			if (value != null) {
					return value;
				}
			} catch (Exception e) {
				continue;
			}
		}
		return null;
	}

	public static String getString(Class<?> clazz, int index) {
		return getString(clazz.getName() + "." + index);
	}
	
	public static void reset() {
		ResourceBundle.clearCache();
	}
}
