package com.tool.util.lang;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileFilter;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;


/**
 * package工具类
 * 
 */
public class PackageUtil {
	
	private static Logger logger  = LoggerFactory.getLogger(PackageUtil.class);

	public final static List<String> EMPTY_LIST = new ArrayList<String>(0);

	/**
	 * 获取指定包路径下的所有类实例
	 */
	public static Set<Class<?>> getClassesInPackage(String packageName)
	{
		String[]  classNames = findClassesInPackage(packageName);
		Set<Class<?>> classes = new HashSet<Class<?>>();
		
		for(String className :classNames)
		{
			try { 
	            //添加到集合中去 
	            classes.add(Class.forName(className)); 
			 } catch (Exception  e)
			 { 
		        logger.error("加载类"+className+"失败，e:"+e.getMessage());
		     } 
		}
		return classes;
	}
	
	/**
	 * 查找指定package下的class
	 * 
	 * @param packageName
	 *            package名字，允许正则表达式
	 * @return 符合要求的classes，全路径名
	 * @throws java.io.IOException
	 */
	public static String[] findClassesInPackage(String packageName) {
		return findClassesInPackage(packageName, EMPTY_LIST, EMPTY_LIST);
	}

	/**
	 * 查找指定package下的class
	 * 
	 * @param packageName
	 *            package名字，允许正则表达式
	 * @param included
	 *            包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @param excluded
	 *            不包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @return 符合要求的classes，全路径名
	 * @throws java.io.IOException
	 */
	public static String[] findClassesInPackage(String packageName, List<String> included, List<String> excluded) {
		String packageOnly = packageName;
		boolean recursive = false;
		// 递归判断
		if (packageName.endsWith(".*")) {
			packageOnly = packageName.substring(0, packageName.lastIndexOf(".*"));
			recursive = true;
		}

		List<String> vResult = new ArrayList<String>();
		try {
			String packageDirName = packageOnly.replace('.', '/');
			Enumeration<URL> dirs = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
			while (dirs.hasMoreElements()) {
				URL url = dirs.nextElement();
				String protocol = url.getProtocol();
				// 如果是目录结构
				if ("file".equals(protocol)) {
					findClassesInDirPackage(packageOnly, included, excluded, URLDecoder.decode(url.getFile(), "UTF-8"),
							recursive, vResult);
				}
				// 如果是jar结构
				else if ("jar".equals(protocol)) {
					JarFile jar = ((JarURLConnection) url.openConnection()).getJarFile();
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						JarEntry entry = entries.nextElement();
						String name = entry.getName();
						if (name.charAt(0) == '/') {
							name = name.substring(1);
						}
						if (name.startsWith(packageDirName)) {
							int idx = name.lastIndexOf('/');
							if (idx != -1) {
								packageName = name.substring(0, idx).replace('/', '.');
							}
							if ((idx != -1) || recursive) {
								if (name.endsWith(".class") && !entry.isDirectory()) {
									String className = name.substring(packageName.length() + 1, name.length() - 6);
									includeOrExcludeClass(packageName, className, included, excluded, vResult);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace(System.out);
		}

		String[] result = vResult.toArray(new String[vResult.size()]);
		return result;
	}

	/**
	 * 通过遍历目录的方式查找符合要求的包下的class
	 * 
	 * @param packageName
	 *            包名，确定名
	 * @param included
	 *            包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @param excluded
	 *            不包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @param packagePath
	 *            包目录路径
	 * @param recursive
	 *            是否递归
	 * @param classes
	 *            结果集
	 */
	private static void findClassesInDirPackage(String packageName, List<String> included, List<String> excluded,
			String packagePath, final boolean recursive, List<String> classes) {
		File dir = new File(packagePath);
		if (!dir.exists() || !dir.isDirectory()) {
			return;
		}
		// 过滤目录和以class后缀的文件
		File[] dirfiles = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				return (recursive && file.isDirectory()) || (file.getName().endsWith(".class"));
			}
		});

		for (File file : dirfiles) {
			if (file.isDirectory()) {
				// 递归处理
				findClassesInDirPackage(packageName + "." + file.getName(), included, excluded, file.getAbsolutePath(),
						recursive, classes);
			} else {
				String className = file.getName().substring(0, file.getName().length() - 6);
				includeOrExcludeClass(packageName, className, included, excluded, classes);
			}
		}
	}

	/**
	 * include exclude过滤
	 * 
	 * @param packageName
	 *            包名，确定名
	 * @param className
	 *            短类名，不包含package前缀，确定名
	 * @param included
	 *            包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @param excluded
	 *            不包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @param classes
	 *            结果集
	 */
	private static void includeOrExcludeClass(String packageName, String className, List<String> included,
			List<String> excluded, List<String> classes) {
		if (isIncluded(className, included, excluded)) {
			classes.add(packageName + '.' + className);
		} else {
		}
	}

	/**
	 * 是否包含
	 * 
	 * @param name
	 *            短类名，不包含package前缀，确定名
	 * @param included
	 *            包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @param excluded
	 *            不包含的类名(短类名，不包含package前缀)，允许正则表达式
	 * @return include-true,else-false
	 */
	private static boolean isIncluded(String name, List<String> included, List<String> excluded) {
		boolean result = false;
		if (included.size() == 0 && excluded.size() == 0) {
			result = true;
		} else {
			boolean isIncluded = find(name, included);
			boolean isExcluded = find(name, excluded);
			if (isIncluded && !isExcluded) {
				result = true;
			} else if (isExcluded) {
				result = false;
			} else {
				result = included.size() == 0;
			}
		}
		return result;

	}

	private static boolean find(String name, List<String> list) {
		for (String regexpStr : list) {
			if (Pattern.matches(regexpStr, name)) {
				return true;
			}
		}
		return false;
	}

    public static String[] findClassesInJarFile(String filePath) {

        List<String> included=EMPTY_LIST;
        List<String> excluded=EMPTY_LIST;
        String packageOnly="";
        boolean recursive=false;
        String packageDirName = packageOnly.replace('.', '/');
        String packageName=packageOnly;
        List<String> vResult = new ArrayList<String>();
        try {

            JarFile jar =new JarFile(filePath);
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.charAt(0) == '/') {
                    name = name.substring(1);
                }
                if (name.startsWith(packageDirName)) {
                    int idx = name.lastIndexOf('/');
                    if (idx != -1) {
                        packageName = name.substring(0, idx).replace('/', '.');
                    }
                    if ((idx != -1) || recursive) {
                        if (name.endsWith(".class") && !entry.isDirectory()) {
                            String className = name.substring(packageName.length() + 1, name.length() - 6);
                            includeOrExcludeClass(packageName, className, included, excluded, vResult);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }

        String[] result = vResult.toArray(new String[vResult.size()]);
        return result;
    }

	public static void main(String[] args) throws Exception{
		
	}
}
