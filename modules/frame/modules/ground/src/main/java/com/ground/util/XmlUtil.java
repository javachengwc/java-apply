package com.ground.util;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;

/**
 * 使用dom4j处理xml操作
 */
public class XmlUtil {

	public final static String DEFAULT_ENCODE = "UTF-8";

	/**
	 * 把document写入文件
	 * @param file－如果文件不存在，创建之，包括它的上层目录，如果存在，将会被覆盖
	 * @param doc－Document对象
	 */
	public static void writeToFile(final File file, final Document doc) {
		XMLWriter writer = null;
		FileOutputStream fout = null;
		try {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			if (!file.exists()) {
				file.createNewFile();
			}
			fout = new FileOutputStream(file);
			writer = new XMLWriter(fout, OutputFormat.createCompactFormat());
			writer.write(doc);
			writer.close();
			fout.close();
		} catch (final Exception e) {
		} finally {
			try {
				if (fout != null) {
					fout.close();
				}
				if (writer != null) {
					writer.close();
				}
			} catch (final IOException e) {
				e.printStackTrace(System.out);
			}
		}
	}

	/**
	 * 读入Xml内容
	 * 
	 * @param file－xml文件
	 * @return－Document对象，发生任何异常都返回null
	 */
	public static Document parse(final File file) {
		if (!file.exists()) {
			return null;
		}
		try {
			return parse(new FileInputStream(file));
		} catch (final Exception e) {

		}
		return null;
	}

	/**
	 * 读入Xml内容
	 * 
	 * @param input－xml内容
	 * @return－Document对象，发生任何异常都返回null
	 */
	public static Document parse(final InputStream input) {
		if (input == null) {
			return null;
		}
		final SAXReader reader = new SAXReader();
		reader.setEncoding(DEFAULT_ENCODE);
		try {
			return reader.read(input);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * 读入Xml内容
	 * 
	 * @param reader－xml内容
	 * @return－Document对象，发生任何异常都返回null
	 */
	public static Document parse(final Reader reader) {
		if (reader == null) {
			return null;
		}
		final SAXReader saxreader = new SAXReader();
		saxreader.setEncoding(DEFAULT_ENCODE);
		try {
			return saxreader.read(reader);
		} catch (final Exception e) {
			return null;
		}
	}

	/**
	 * 读入Xml内容
	 * 
	 * @param content－xml内容
	 * @return－Document对象，发生任何异常都返回null
	 */
	public static Document parse(final String content) {
		if (content == null) {
			return null;
		}
		return parse(new StringReader(content));
	}
	
	public static String getNodeValue(Node node, String xpath) {
		Element element = (Element)node.selectSingleNode(xpath);
		return element.getStringValue();
	}
	
	public static Element getNodeAsElement(Node node, String xpath) {
		return (Element)node.selectSingleNode(xpath);
	}
}
