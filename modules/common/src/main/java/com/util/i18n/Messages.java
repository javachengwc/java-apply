package com.util.i18n;

import java.io.File;
import java.util.LinkedList;
import java.util.ResourceBundle;
/**
 * 国际化资源文件
 * ResourceBundle类是读取资源属性文件（properties），然后根据.properties文件的名称信息（本地化信息）,
 * 匹配当前系统的国别语言信息（也可以程序指定），然后获取相应的properties文件的内容。
 *
 * properties资源文件必须是ISO-8859-1编码，因此，对于非西方语系的处理，都必须先将之转换为Java Unicode Escape格式。转换方法是native2ascii。
 * native2ascii -encodeing 源资源编码  源资源文件 转码后资源文件
 * ep:native2ascii -encoding UTF8 il8n.zh.properties il8n.zh_CN.properties
 *
 * 比如在中文操作系统下应用有下面3个国际化资源文件
 * i18n.properties
 * il8n.zh_CN.properties
 * il8n_en_US.properties
 *
 * Locale locale1 = new Locale("zh", "CN");
 * ResourceBundle resb1 = ResourceBundle.getBundle("i18n", locale1);
 * --匹配il8n.zh_CN.properties配置文件
 *
 * ResourceBundle resb2 = ResourceBundle.getBundle("i18n", Locale.getDefault()); 或
 * ResourceBundle resb2 = ResourceBundle.getBundle("i18n",);
 * --匹配il8n.zh_CN.properties配置文件,当il8n.zh_CN.properties不存在时候，会使用默认的i18n.properties
 *
 * Locale locale3 = new Locale("en", "US");
 * ResourceBundle resb3 = ResourceBundle.getBundle("i18n", locale3);
 * --匹配il8n_en_US.properties配置文件
 */
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
