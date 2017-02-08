package com.util.lang;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 逻辑、功能相关描述:用来加载类，classPath下的资源文件，属性文件等。
 * getExtendResource(StringrelativePath)方法，可以使用".." +
 * File.separator符号来加载classpath外部的资源。
 * 
 */
public class ClassLoaderUtil {

	private static Log log = LogFactory.getLog(ClassLoaderUtil.class);
	// Thread.currentThread().getContextClassLoader().getResource("")

	/**
	 * Function:加载Java类。 使用全限定类名
	 */
	public static Class<?> loadClass(String className) {
		try {
			return getClassLoader().loadClass(className);
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("class not found '" + className + "'", e);
		}
	}

	/**
	 * Function:得到类加载器
	 * @param
	 */
	public static ClassLoader getClassLoader() {
		return getClassLoader(null);
	}
	
	/**
	 * 获取类加载器
	 * @param cls
	 * @return
	 */
	public static ClassLoader getClassLoader(Class<?> cls) {
		ClassLoader cl = null;
		if(cls != null){
			cl = cls.getClassLoader();
		}
		if (cl == null) {
			cl = Thread.currentThread().getContextClassLoader();
        }
		if (cl == null) {
			cl = ClassLoaderUtil.class.getClassLoader();
		}
		if (cl == null) {
            throw new IllegalStateException("Cannot determine classloader");
        }
		log.debug("ClassLoader = "+cl);
		return cl;
	}

	/**
	 * Function:提供相对于classpath的资源路径，返回文件的输入流
	 * @param relativePath 文件输入流
	 * @return InputStream 
	 */
	public static InputStream getStream(String relativePath)
			throws MalformedURLException, IOException {
		if (!relativePath.contains(".." + File.separator)) {
			return getClassLoader().getResourceAsStream(relativePath);
		} else {
			return ClassLoaderUtil.getStreamByExtendResource(relativePath);
		}
	}

	/**
	 * Function:打开到此 URL 的连接并返回一个用于从该连接读入的 InputStream。
	 * @param url
	 * @return InputStream
	 */
	public static InputStream getStream(URL url) throws IOException {
		if (url != null) {
			return url.openStream();
		} else {
			return null;
		}
	}

	/**
	 * Function:
	 * @param relativePath
	 *            必须传递资源的相对路径。是相对于classpath的路径。
	 *            如果需要查找classpath外部的资源，需要使用../来查找
	 */
	public static InputStream getStreamByExtendResource(String relativePath)
			throws MalformedURLException, IOException {
		return ClassLoaderUtil.getStream(ClassLoaderUtil
				.getExtendResource(relativePath));
	}

	/**
	 * Function:提供相对于classpath的资源路径，返回属性对象，它是一个散列表
	 * @param resource
	 */
	public static Properties getProperties(String resource) {
		Properties properties = new Properties();
		try {
			properties.load(getStream(resource));
		} catch (IOException e) {
			throw new RuntimeException("couldn't load properties file '"
					+ resource + "'", e);
		}
		return properties;
	}

	/**
	 * Function:得到本Class所在的ClassLoader的Classpat的绝对路径。
	 * @param
	 */
	public static String getAbsolutePathOfClassLoaderClassPath() {
		String path = ClassLoaderUtil.getClassLoader().getResource("").toString();
		log.info(path);
		return path;
	}

	/**
	 * Function:取得路径
	 * 
	 * @param relativePath
	 *            必须传递资源的相对路径。是相对于classpath的路径。如果需要查找classpath外部的资源，需要使用../来查找
	 * @return 资源的绝对URL 
	 */
	public static URL getExtendResource(String relativePath)
			throws MalformedURLException {
		// ClassLoaderUtil.log.info("传入的相对路径："+relativePath) ;
		// System.out.println(File.separator);
		// ClassLoaderUtil.log.info(Integer.valueOf(relativePath.indexOf("../")))
		// ;
		// 判断是否有上级目录
		if (!relativePath.contains(".." + File.separator)) {
			return ClassLoaderUtil.getResource(relativePath);
		}

		// 获取E:/webx_workspace/PMS/WebRoot/WEB-INF/classes/目录
		String classPathAbsolutePath = ClassLoaderUtil
				.getAbsolutePathOfClassLoaderClassPath();

		if (relativePath.substring(0, 1).equals(File.separator)) {
			relativePath = relativePath.substring(1);
		}

		String wildcardString = relativePath.substring(0, relativePath
				.lastIndexOf(".." + File.separator) + 3);

		relativePath = relativePath.substring(relativePath.lastIndexOf(".."
				+ File.separator) + 3);
		// 验证相对路径中存在多少个../,
		int containSum = ClassLoaderUtil.containSum(wildcardString, ".."
				+ File.separator);
		
		classPathAbsolutePath = ClassLoaderUtil.cutLastString(
				classPathAbsolutePath, File.separator, containSum);
		
		String resourceAbsolutePath = classPathAbsolutePath + relativePath;
		
		URL resourceAbsoluteURL = new URL(resourceAbsolutePath);
		
		return resourceAbsoluteURL;
	}

	/**
	 * Function:验证相对路径中存在多少个../, 比如../表示上级目录，../../表示上上级目录
	 * @param source (路径),dest（符号）
	 */
	private static int containSum(String source, String dest) {
		int containSum = 0;
		int destLength = dest.length();
		while (source.contains(dest)) {
			containSum = containSum + 1;
			source = source.substring(destLength);
		}
		return containSum;
	}

	/**
	 * Function:获取被截取后的上级目录
	 * @param source 路径
	 * @param dest 符号
	 * @param num 几级目录
	 */
	private static String cutLastString(String source, String dest, int num) {
		// String cutSource=null;
		for (int i = 0; i < num; i++) {
			source = source.replace("/", dest);
			source = source.substring(0, source.lastIndexOf(dest, source
					.length() - 2) + 1);
		}
		return source;
	}

	/**
	 * Function:
	 * @param resource
	 */
	public static URL getResource(String resource) {
		ClassLoaderUtil.log.info("传入的相对于classpath的路径：" + resource);
		return ClassLoaderUtil.getClassLoader().getResource(resource);
	}

	/**
	 * Function:获取文件所在路径
	 * @param relativePath 传入的相对路径
	 */
	public static String getPath(String relativePath) {
		String temppath;
		try {
			temppath = "" + ClassLoaderUtil.getExtendResource(relativePath);
			int pathindex = temppath.indexOf("/");
			String path = null;
			if (System.getProperty("os.name").toUpperCase().equals("LINUX")) {
				path = temppath.substring(pathindex);
			} else {
				path = temppath.substring(pathindex + 1);
			}
			return path;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}

    /**
     * 查询类在运行期对应的JAR包
     */
    public static String where(final Class cls) {
        if (cls == null)throw new IllegalArgumentException("null input: cls");
        URL result = null;
        final String clsAsResource = cls.getName().replace('.', '/').concat(".class");
        final ProtectionDomain pd = cls.getProtectionDomain();
        if (pd != null) {
            final CodeSource cs = pd.getCodeSource();
            if (cs != null) result = cs.getLocation();
            if (result != null) {
                if ("file".equals(result.getProtocol())) {
                    try {
                        if (result.toExternalForm().endsWith(".jar") ||
                                result.toExternalForm().endsWith(".zip"))
                            result = new URL("jar:".concat(result.toExternalForm())
                                    .concat("!/").concat(clsAsResource));
                        else if (new File(result.getFile()).isDirectory())
                            result = new URL(result, clsAsResource);
                    }
                    catch (MalformedURLException ignore) {}
                }
            }
        }
        if (result == null) {
            final ClassLoader clsLoader = cls.getClassLoader();
            result = clsLoader != null ?
                    clsLoader.getResource(clsAsResource) :
                    ClassLoader.getSystemResource(clsAsResource);
        }
        return result.toString();
    }

    public static void main(String[] args) throws MalformedURLException {
		// ClassLoaderUtil.getExtendResource("../spring/dao.xml");
		// ClassLoaderUtil.getExtendResource("../../../src/log4j.properties");
		// ClassLoaderUtil.getExtendResource(".."+File.separator+"log4j.properties");

        System.out.println(ClassLoaderUtil.getAbsolutePathOfClassLoaderClassPath());
		System.out.println(ClassLoaderUtil.getPath(".." + File.separator + ".."
				+ File.separator + "upload" + File.separator + "fees"
				+ File.separator + "temp"));
		System.out.println(ClassLoaderUtil.getPath(".." + File.separator + ".." + File.separator+ "report.properties").toString());
		System.out.println(ClassLoaderUtil.getPath("report.properties").toString());
		File file = new File(ClassLoaderUtil.getPath("report.properties").toString());
		System.out.println(file.exists());
	}

}
