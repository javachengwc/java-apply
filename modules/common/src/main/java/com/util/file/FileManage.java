package com.util.file;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * 文件读取类
 */
public final class FileManage implements Serializable
{
	private static final long serialVersionUID = -3842384283482299L;
	
	private static final Logger log = Logger.getLogger(FileManage.class);
	
	private static FileManage fileManage;
	
	public static final FileManage newInstance()
	{
		if(fileManage == null)
		{
			fileManage = new FileManage();
		}
		return fileManage;
	}
	
	
	public boolean isFileExist(String url) throws Exception{
		URL serverUrl = new URL(url);
		HttpURLConnection urlcon = (HttpURLConnection) serverUrl.openConnection();
	    String message = urlcon.getHeaderField(0);//文件存在‘HTTP/1.1 200 OK’ 文件不存在 ‘HTTP/1.1 404 Not Found’
	    if (!StringUtils.isBlank(message) && message.startsWith("HTTP/1.1 404")) {
	    	log.info("serverUrl aacConvert this downUrl is not found! downUrl:"+url);
			return false;
	    }
	    return true;
	}
	/**
	 * 获取文件内容
	 * 
	 * @param fileName
	 * @return
	 */
	public List<String> getContent(String fileName)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader isReader = null;
		
		List<String> list = null;
		try
		{
			fis = new FileInputStream(fileName);
			isr = new InputStreamReader(fis, "GBK");
			isReader = new BufferedReader(isr);
			// isReader = new BufferedReader(new UnicodeReader(fis,
			// Charset.defaultCharset().name()));
			
			String str = null;
			if(isReader.ready())
			{
				list = new ArrayList<String>();
				do
				{
					str = isReader.readLine();
					if(str == null)
					{
						break;
					}
					str = str.trim();
					if(str.length() > 0)
						list.add(str);
				}while(true);
			}
		}
		
		catch(FileNotFoundException e)
		{
			log.warn("getContent error:" + e.getMessage());
		}
		catch(IOException e)
		{
			log.warn("getContent error:" + e.getMessage());
		}
		catch(NullPointerException e)
		{
			log.warn("getContent NullPointerException:" + e.getMessage());
		}
		finally
		{
			try
			{
				if(null != fis){
					fis.close();
				}
				if(null != isr){
					isr.close();
				}
				if(null != isReader){
					isReader.close();
				}
			}
			catch(IOException e)
			{
			}
		}
		return list;
	}
	
	
	/**
	 * 获取文件内容
	 * 
	 * @param fileName
	 * @param charset 字符编码(为null，则使用UTF-8)
	 * @return
	 */
	public List<String> getContent(String fileName,String charset)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader isReader = null;
		
		List<String> list = null;
		try
		{
			fis = new FileInputStream(fileName);
			if(StringUtils.isBlank(charset))
			{
				charset = "UTF-8";
			}
			isr = new InputStreamReader(fis, charset);
			isReader = new BufferedReader(isr);
			String str = null;
			if(isReader.ready())
			{
				list = new ArrayList<String>();
				do
				{
					str = isReader.readLine();
					if(str == null)
					{
						break;
					}
					str = str.trim();
					if(str.length() > 0)
						list.add(str);
				}while(true);
			}
		}
		
		catch(FileNotFoundException e)
		{
			log.warn("getContent error:" + e.getMessage());
		}
		catch(IOException e)
		{
			log.warn("getContent error:" + e.getMessage());
		}
		catch(NullPointerException e)
		{
			log.warn("getContent NullPointerException:" + e.getMessage());
		}
		finally
		{
			try
			{
				if(null != fis){
					fis.close();
				}
				if(null != isr){
					isr.close();
				}
				if(null != isReader){
					isReader.close();
				}
			}
			catch(IOException e)
			{
				log.error("getContent(),error:",e);
			}
		}
		return list;
	}
	
	/**
	 * B方法追加文件：使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public void appendContent(String fileName, String content)
	{
		try
		{
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileOutputStream out = new FileOutputStream(fileName, true);
			OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
			writer.write(content+"\n");
			writer.close();
			out.close();
		}
		catch(IOException e)
		{
			log.error("appendContent(),error:",e);
		}
	}
	
	/**
	 * 使用FileWriter
	 * 
	 * @param fileName
	 * @param content
	 */
	public void setContent(String fileName, String content)
	{
		FileOutputStream out = null;
		try
		{                                                                             
			out = new FileOutputStream(fileName);
			OutputStreamWriter writer = new OutputStreamWriter(out, "utf-8");
			writer.write(content+"\n");
			writer.close();
			out.close();
		}
		catch(IOException e)
		{
			log.error("setContent(),error:",e);
		}
		finally
		{
			if(null !=out){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				out = null;
			}
		}
	}
	
	/**
	 * 创建一个空的文件
	 * 
	 * @param dir 文件目录
	 * @param fileName 文件名称
	 */
	public static boolean createFile(String dir,String fileName) 
	{
		String fullFilePath = dir + File.separator + fileName;
		fullFilePath = fullFilePath.replace("\\", "/");
		File excelFile = new File(fullFilePath);
		if(excelFile.exists())
		{
			excelFile.delete();
		}
		try {
			excelFile.createNewFile();
			return true;
		} catch (IOException e1) {
			log.error("createFile()删除文件失败,fullFilePath---->" + fullFilePath);
			return false;
		}
	}
	
	/**
	 * 删除指定的文件
	 * 
	 * @param dir 文件目录
	 * @param fileName 文件名称
	 * 
	 * @return 成功：true  失败：false;
	 */
	public static boolean deleteFile(String dir,String fileName) 
	{
		String fullFilePath = dir + File.separator + fileName;
		fullFilePath = fullFilePath.replace("\\", "/");
		File file = new File(fullFilePath);
		try {
			file.delete();
			return true;
		} catch (Exception e1) {
			log.error("deleteFile()删除文件失败,fullFilePath---->" + fullFilePath);
			return false;
		}
	}
	
	/**
	 * 重命名指定的文件
	 * 
	 * @param dir1 文件目录1
	 * @param fileName1 文件1名称
	 * @param dir2 文件目录2
	 * @param fileName2 文件2名称
	 * 
	 * @return 成功：true  失败：false;
	 */
	public static boolean renameFile(String dir1,String fileName1,String dir2,String fileName2) 
	{
		String fullFilePath1 = dir1 + File.separator + fileName1;
		fullFilePath1 = fullFilePath1.replace("\\", "/");
		File file1 = new File(fullFilePath1);
		
		String fullFilePath2 = dir2 + File.separator + fileName2;
		fullFilePath2 = fullFilePath2.replace("\\", "/");
		File file2 = new File(fullFilePath2);
		try
		{
			file1.renameTo(file2);
			return true;
		}
		catch(Exception ex)
		{
			log.error("renameFile()重命名文件失败,fullFilePath1---->" + fullFilePath1);
			log.error("renameFile()重命名文件失败,fullFilePath2---->" + fullFilePath2);
			return false;
		}
	}
	
	/**
	 * 读取txt文件内容
	 * @param filePath txt文件路径
	 * @return byte[] 文件内容字节数组
	 * @throws java.io.IOException
	 */
	private Byte[] readTxtFile(String filePath) throws IOException
	{
		InputStream input=new FileInputStream(filePath);
		byte[] b=new byte[1024];
		ArrayList<Byte> lsbytes = new ArrayList<Byte>();

		int n=0;
		while((n=input.read(b))!=-1){
			for(int i=0;i<n;i++){
				lsbytes.add(b[i]);
			}
		}

		return (Byte[])(lsbytes.toArray());
	}
	
	/**
	 * 从url取得文件名
	 * @param url 文件url 比如  http://m.yy.com/group9/M00/B3/EE/2hDjulAT_xEAAAAAADGKJiWpBJk680.apk
	 * @return
	 */
	public static String getFileNameFromUrl(String url)
	{
		String name = new Long(System.currentTimeMillis()).toString() + ".xml";
		int index = url.lastIndexOf("/");
		if (index > 0) {
			name = url.substring(index + 1);
			if (name.trim().length() > 0) {
				return name;
			}
		}
		return name;
	}
	
	/**
	 * 下载远程文件
	 * @param url 远程文件url,比如  http://m.yy.com/group9/M00/B3/EE/2hDjulAT_xEAAAAAADGKJiWpBJk680.apk
	 * @param dir 本地存放目录 ,比如 F:\\http_download\\
	 * @return 返回本地完整的路径名 ,比如 F:\http_download\2hDjulAT_xEAAAAAADGKJiWpBJk680.apk
	 */
	public static String downloadFromUrl(String url, String dir) 
	{                                                                                                 
		log.info("downloadFromUrl(),url:" + url);
		dir = dir.replace("\\", "/");
		if(!dir.endsWith("/")){
			dir = dir + "/";
		}
		log.info("downloadFromUrl(),url:" + url);
		String fileName = getFileNameFromUrl(url);
		log.info("downloadFromUrl(),fileName:" + fileName);
		try {
			URL httpurl = new URL(url);
			System.out.println("fileName--->" + fileName);
			File f = new File(dir + fileName);
			if(null != f && f.exists()){
				//System.out.println("fileName--->" + fileName + ",已存在,删除掉...");
				log.error("dir:"+dir+" ,fileName--->" + fileName + ",已存在,删除掉...");
				f.delete();
			}
			f.createNewFile();
			System.out.println("fileName--->" + fileName + ",创建成功!");
			FileUtils.copyURLToFile(httpurl, f);
		} catch (Exception e) {
			log.error("downloadFromUrl() error --->",e);
			//e.printStackTrace();
			return null;
		}
		if(dir.endsWith(File.separator)){
			return dir + fileName;
		}else{
			return dir + File.separator + fileName;
		}
	}
	
	public static void main(String[] args) throws InterruptedException
	{
//		String dir = "D:/workspace/yy-music/web/dev/trunk/src/WebServer_KTV/www/WEB-INF/sysconfig";
//		String oldFile = "checkVersion.properties";
//		boolean result = FileManage.deleteFile(dir, oldFile);
//		System.out.println("result:" + result);
		
		String url = "http://static.m.yy.com/group9/M00/B3/F1/2hDjulA3ny8AAAAAADjZfDkA9U.apk";
		String dir = "D:/";
		FileManage.downloadFromUrl(url, dir);
		
		Thread.currentThread().sleep(3000);
		String localFilePath = "D:/2hDjulA3ny8AAAAAADjZfDkA9UY613.apk";
		String destFilePath = "D:/weichang.apk";
		File downFile = new File(localFilePath);
		File destFile = new File(destFilePath);
		
		FileManage.deleteFile("D:", "weichang.apk");
		Thread.currentThread().sleep(3000);
		downFile.renameTo(destFile);
		Thread.currentThread().sleep(3000);
	}
}
