package com.util.lang;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URL;
import java.util.*;
import net.sf.cglib.beans.BeanGenerator;

public class ClassUtil {

    private static Map<String, Class<?>> result;

	public static List<Class<?>> getClassByDirectory(String packageName) {
		URL url = ClassUtil.class.getResource("/");
		File file = new File(url.getPath() + packageName.replace(".", "/"));
		List<Class<?>> result = new ArrayList<Class<?>>();

		if (file.isDirectory()) {
			String pName = packageName + ".";
			for (File f : file.listFiles()) {
				String fileName = f.getName();
				try {
					Class<?> clazz = Class.forName(pName
							+ fileName.substring(0, fileName.indexOf(".")));
					result.add(clazz);
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}

	public static Map<String, Class<?>> getAllClassByDirectory(String packageName) {
		result = new HashMap<String, Class<?>>();
		URL url = ClassUtil.class.getResource("/");
		File file = new File(url.getPath() + packageName.replace(".", "/"));
		addAllClass(file.getAbsolutePath(), packageName + ".");
		return result;
	}

	private static void addAllClass(String filePath, String packageName) {

		File file = new File(filePath);
		File[] fileList = file.listFiles();

		String dirPath = null;

		if (fileList != null) {
			String pName = packageName;
			for (int i = 0; i < fileList.length; i++) {

				if (fileList[i].isFile()) {
					String fileName = fileList[i].getName();
					if(!fileName.endsWith(".class")){ // 目录下不一定全部都是.class文件
						continue;
					}
					Class<?> clazz = null;
					try {
						clazz = Class.forName(pName
								+ fileName.substring(0, fileName.indexOf(".")));
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						return;
					}
					result.put(clazz.getSimpleName(), clazz);
				}

				if (fileList[i].isDirectory()) {

					dirPath = fileList[i].getPath();
					addAllClass(dirPath, packageName + fileList[i].getName()
							+ ".");
				}
			}
		}
	}
	
	public static List<Method> getMethodsByName(Class<?> clazz, String methodName){
		Method[] methods = clazz.getDeclaredMethods();
		List<Method> results = new ArrayList<Method>();
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				results.add(method);
			}
		}
		return results;
	}
	
	public static String getMethodSignature(Method method, boolean includeException){
		String signature = method.toString();
		if(!includeException && method.getExceptionTypes().length > 0){
			signature = signature.substring(0, signature.lastIndexOf("throws")).trim();
		}
		return signature;
	}

    public static List<Field> getAllField(Class clz)
    {
        List<Field> list = new ArrayList<Field>();
        Class clazz = clz;
        while(clazz!=null && clazz!=Object.class)
        {
            Field[] fields = clazz.getDeclaredFields();
            list.addAll(Arrays.asList(fields));
            clazz = clazz.getSuperclass();
        }
        return list;
    }

    public static Class createClass(Parameter[] parameters, Class supperClass) {
        BeanGenerator generator = new BeanGenerator();
        generator.setSuperclass(supperClass);
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            String paramName = parameter.getName();
            generator.addProperty(paramName, parameter.getType());
        }
        return (Class)generator.createClass();
    }
}
