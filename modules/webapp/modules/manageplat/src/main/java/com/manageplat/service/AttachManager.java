package com.manageplat.service;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.Calendar;
import java.util.Date;

public class AttachManager {
	
	private static Logger m_logger = Logger.getLogger(AttachManager.class);

    private static final String ROOT_DIR="/tmp/edm/";

    private static final String WIN_ROOT_DIR="E:/tmp/";

    private static AttachManager instance=new AttachManager();
	
	private AttachManager()
	{
	  
	}

	public static AttachManager getInstance()
	{
		return instance;
	}


    public String getAttachPath()
    {
        String dir = ROOT_DIR;
        String osInfo= System.getProperty("os.name").toLowerCase();
        if(osInfo.indexOf("windows")>=0)
        {
            dir = WIN_ROOT_DIR;
        }

        return dir;
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
		String preSuff= RandomStringUtils.randomAlphanumeric(4).toLowerCase();
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
	
	//返回绝对路径以保存到数据库
	public String saveFile(InputStream stream,String fileName,String defaultSuffix)
	{
	    File dir =getCurDateDir();
		String newFileName =getNewFileName(fileName,defaultSuffix);
		m_logger.info("AttachManager newFimeName is "+newFileName);
        File file = new File(dir,newFileName);
		try{
			FileOutputStream fos=new FileOutputStream(file);
			transStream(stream, fos);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		String savePath=file.getPath();
		m_logger.info("AttachManager save file path is "+savePath);
		return savePath;
	}
	
	//返回绝对路径以保存到数据库
	public String saveFile(File file,String fileName,String defaultSuffix)
	{
		File dir =getCurDateDir();
		String newFileName =getNewFileName(fileName,defaultSuffix);
		m_logger.info("AttachManager newFimeName is "+newFileName);
        FileInputStream fis=null;
        FileOutputStream fos=null;
        File target = new File(dir,newFileName);
		try{
			fis=new FileInputStream(file);
			fos=new FileOutputStream(target);
            transStream(fis, fos);
		}catch(Exception e)
		{
			e.printStackTrace();
		}finally {

            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }catch(Exception ee)
            {

            }
        }
        String savePath=target.getPath();
		m_logger.info("AttachManager save file path is "+savePath);
		return savePath;
	}

    public static void transStream(InputStream fis,OutputStream fos)
    {
        BufferedOutputStream bos=null;
        BufferedInputStream bis=null;
        try{
            bis=new BufferedInputStream(fis);
            bos=new BufferedOutputStream(fos);

            byte[] buf=new byte[4096];

            int len=-1;
            while((len=bis.read(buf))!=-1){
                bos.write(buf,0,len);
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        finally{
            try{
                if(null!=bis){
                    bis.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
            try{
                if(null!=bos){
                    bos.close();
                }
            }catch(IOException ex){
                ex.printStackTrace();
            }
        }

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
