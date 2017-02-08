package com.util.i18n;

import com.util.lang.PackageUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 生成message properties内容
 */
public class PickUpMessage {

	private static Map<String, List<String>> resources = new LinkedHashMap<String, List<String>>();

	public static void main(String[] args) throws Exception {
		String include = args[0];
		String filePath = args[1];
		String[] clazzes = PackageUtil.findClassesInPackage(include);
		for (String cla : clazzes) {
			try {
				Class<?> clazz = Class.forName(cla);
				if (Nameable.class.isAssignableFrom(clazz) && clazz.isEnum()) {
					Enum<?>[] enums = (Enum[]) clazz.getEnumConstants();
					for (Enum<?> enum1 : enums) {
						Class<?> enumClass = enum1.getClass();
						Field nameField = enumClass.getDeclaredField("name");
						nameField.setAccessible(true);
						String key = enumClass.getName() + "." + enum1.name();
						String value = nameField.get(enum1).toString();
						String message = key + " = " + value;
						// System.out.println(message);
						group(key, message);
					}
				}
				Message message = clazz.getAnnotation(Message.class);
				if (message != null) {
					String[] values = message.values();
					if (values != null) {
						for (int i = 0; i < message.values().length; i++) {
							String key = clazz.getName() + "." + i;
							String value = values[i];
							String msg = key + " = " + value;
							// System.out.println(msg);
							group(key, msg);
						}
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		out(filePath);
	}

	private static void out(String filePath) {
		try {
			File file = new File(filePath);
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			OutputStream writer = new FileOutputStream(file);
			System.out.println(file);
			List<String> keys = new ArrayList<String>(resources.keySet());
			Collections.sort(keys);
			int total = 0;
			for (String key : keys) {
				List<String> messages = resources.get(key);
				Collections.sort(messages);
				writer.write(("# group '" + key + "' size:" + messages.size() + "\n").getBytes("utf-8"));
				total += messages.size();
			}
			writer.write(("# total:" + total + "\n\n").getBytes("utf-8"));
			for (String key : keys) {
				List<String> messages = resources.get(key);
				writer.write(("# '" + key + "' begin" + "\n").getBytes("utf-8"));
				for (String message : messages) {
					writer.write((message + "\n").getBytes("utf-8"));
				}
				writer.write(("# '" + key + "' end\n\n").getBytes("utf-8"));
			}
			writer.flush();
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void group(String key, String message) {
		put(formKey(key), message);
	}

	private static String formKey(String key) {
		String[] packages = key.split("\\.");
		int prefixIndex = packages.length - 3;
		StringBuilder builder = new StringBuilder(64);
		for (int i = 0; i < prefixIndex && i < packages.length; i++) {
			builder.append(packages[i]);
			builder.append(".");
		}
		return builder.toString().substring(0, builder.length() - 1);
	}

	private static void put(String key, String message) {
		List<String> messages = resources.get(key);
		if (messages == null) {
			messages = new ArrayList<String>();
			resources.put(key, messages);
		}
		messages.add(message);
	}

}
