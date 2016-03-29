package com.other.classloader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ApplicationClasses {

	private Map<String, ApplicationClass> classes = new HashMap<String, ApplicationClass>();

	public ApplicationClass getAppClass(String name) {
		if (!classes.containsKey(name))
			classes.put(name, new ApplicationClass(name));
		return classes.get(name);
	}

	public List<ApplicationClass> getAssignableClasses(Class<?> clazz) {
		List<ApplicationClass> results = new ArrayList<ApplicationClass>();
		for (ApplicationClass appClass : classes.values()) {
			try {
				App.classloader.loadClass(appClass.name);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
			if (clazz.isAssignableFrom(appClass.javaClass) && !clazz.getName().equals(appClass.javaClass.getName()))
				results.add(appClass);
		}
		return results;
	}

	public List<ApplicationClass> all() {
		return new ArrayList<ApplicationClass>(classes.values());
	}

	public boolean containsClass(String name) {
		return classes.containsKey(name);
	}

	public void clear() {
		classes.clear();
	}

	public class ApplicationClass {

		public String name;

		public byte[] javaByteCode;

		public Class<?> javaClass;

		public Long timestamp = 0L;

		boolean compiled;

		public ApplicationClass(String name) {
			this.name = name;
			this.refresh();
		}

		public void refresh() {
			this.javaByteCode = null;
			this.compiled = false;
			this.timestamp = 0L;
		}

		public boolean isDefinable() {
			return compiled && javaClass != null;
		}

		public void uncompile() {
			this.javaClass = null;
		}

	}
}
