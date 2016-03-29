package com.other.classloader;

import java.net.MalformedURLException;
import java.net.URL;

public class App {

	public static String EXT_APP_JAR_PATH = "file://com/app/ext/ext.jar";
	public static AppClassLoader classloader;
	public static ApplicationClasses classes = new ApplicationClasses();

	public void init() {
		try {
			classloader = new AppClassLoader(new URL(EXT_APP_JAR_PATH));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	public void reload() {
		try {
			classloader = new AppClassLoader(new URL(EXT_APP_JAR_PATH));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
		Thread.currentThread().setContextClassLoader(classloader);
	}
}
