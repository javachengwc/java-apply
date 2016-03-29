package com.other.classloader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * 这个自定义的classloader从某个jar包中加载class
 */
public class AppClassLoader extends URLClassLoader {

	public AppClassLoader(URL jar) {
		super(new URL[] { jar }, AppClassLoader.class.getClassLoader());
	}

	@Override
	protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
		Class<?> c = findLoadedClass(name);
		if (c != null) {
			return c;
		}

		Class<?> appClass = loadApplicationClass(name);
		if (appClass != null) {
			if (resolve) {
                resolveClass(appClass);
            }
			return appClass;
		}

		return super.loadClass(name);
	}

	protected Class<?> loadApplicationClass(String name) {
		long start = System.currentTimeMillis();
		ApplicationClasses.ApplicationClass appClass = App.classes.getAppClass(name);
		if (appClass != null) {
			if (appClass.isDefinable()) {
				return appClass.javaClass;
			} else {

			}
		}
		return null;
	}
}
