package com.z7z8.service.monit;

import com.util.base.StreamUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class AttachManager {
	
	private static Logger m_logger = LoggerFactory.getLogger(AttachManager.class);
	
	private static AttachManager instance=new AttachManager();
	
	private AttachManager()
	{
	  
	}
	
	public static AttachManager getInstance()
	{
		return instance;
	}
	
	
	public String getAttachPath() {
		
		return SettingConfig.getAttachDir();
	}
	
	public byte[] transStreamToByte(InputStream in) throws Exception{
		
		byte[] file_buff;
		ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
		try {
			byte[] b = new byte[ 4096 ];
			int n;
			while((n = in.read(b)) != -1)
			{
				out.write(b, 0, n);
			}
			file_buff = out.toByteArray();
		}finally{
			out.close();
			in.close();
		}
		return file_buff;
	}
	
	public File getCurDateDir()
	{
		Calendar calendar=Calendar.getInstance();
		int year=calendar.get(Calendar.YEAR);
		int month=calendar.get(Calendar.MONTH)+1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int dirs [] ={year,month,day,hour};
		
		String path=getAttachPath();
		
		File dir=new File(path);
		if(!dir.exists()){
			dir.mkdir();
		}
		for(int nextDirNumber:dirs)
		{
			File nextDir=new File(dir,new Integer(nextDirNumber).toString());
			if(!nextDir.exists()){
				nextDir.mkdir();
			}
			dir = nextDir;
		}
		return dir;
	}
	
	public String getNewFileName(String fileName,String defaultSuffix)
	{
		String newFileName=null;
		String preSuff=RandomStringUtils.randomAlphanumeric(4).toLowerCase();
		long now=new Date().getTime();
		if(StringUtils.isBlank(fileName))
		{
			newFileName=preSuff+now+defaultSuffix;
		}else
		{
			int index=fileName.lastIndexOf('.');
			if(index!=-1){
				newFileName=preSuff+now+fileName.substring(index);
			}else{
				newFileName=preSuff+now+defaultSuffix;
			}
		}
		return newFileName;
	}
	
	//返回相对路径以保存到数据库
	public String saveFile(InputStream stream,String fileName,String defaultSuffix)
	{
	    File dir =getCurDateDir();
		String newFileName =getNewFileName(fileName,defaultSuffix);
		m_logger.info("AttachManager newFimeName is "+newFileName);
		try{
			FileOutputStream fos=new FileOutputStream(new File(dir,newFileName));
			StreamUtil.transStream(stream, fos);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		m_logger.info("AttachManager dir.getPath()= "+dir.getPath());
		m_logger.info("AttachManager getAttachPath()="+getAttachPath());
		String savePath=dir.getPath().replace(getAttachPath(),"")+File.separator+newFileName;
		m_logger.info("AttachManager save file path is "+savePath);
		return savePath;
	}
	
	//返回相对路径以保存到数据库
	public String saveFile(File file,String fileName,String defaultSuffix)
	{
		File dir =getCurDateDir();
		String newFileName =getNewFileName(fileName,defaultSuffix);
		m_logger.info("AttachManager newFimeName is "+newFileName);
		try{
			FileInputStream fis=new FileInputStream(file);
			FileOutputStream fos=new FileOutputStream(new File(dir,newFileName));
            StreamUtil.transStream(fis, fos);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		String savePath=dir.getPath().replace(getAttachPath(),"")+File.separator+newFileName;
		m_logger.info("AttachManager save file path is "+savePath);
		return savePath;
	}
	
	public void delFiles(String... paths)
	{
		
		if(paths!=null && paths.length>0)
		{
			for(String path:paths)
			{
			   File file=new File(path);
			   if(file.exists())
				   file.delete();
			}
		}
	}
	
}
