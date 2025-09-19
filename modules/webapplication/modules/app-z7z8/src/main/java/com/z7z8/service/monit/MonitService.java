package com.z7z8.service.monit;

import java.io.File;

import com.util.CharsetUtil;
import com.util.file.FileUtil;
import com.z7z8.dao.monit.MonitDao;
import com.z7z8.model.monit.ImageInfo;
import com.z7z8.model.monit.ProcessInfo;
import com.z7z8.model.monit.SettingInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MonitService {
	
	private static Logger m_logger = LoggerFactory.getLogger(MonitService.class);
	/**默认的一个版本号**/
	private static String DEFALUT_VERSION ="1.0.0.0";
	
	@Autowired
	public MonitDao monitDao;
	
	public MonitService()
	{
	   System.out.println("MonitService create");
	}
	
	public SettingInfo getSetting()
	{
		return SettingConfig.getSettingInfo();
	}
	
	/**
	 * 获取最新版本
	 */
	public String getLastedVersionNo()
	{
		
		String rt =DEFALUT_VERSION;//默认的值
		String lastedVersionNoFile = SettingConfig.getLastedVersionNoFile();
		File f = new File(lastedVersionNoFile);
		if(f.exists())
		{

			try
			{
				rt = FileUtil.readFile(lastedVersionNoFile, CharsetUtil.UTF8);
			}catch(Exception e)
			{
				m_logger.error("MonitService getLastedVersion 拿不到文件里面的值,异常为e:",e);
			}
		}
		if(rt==null)
		{
			rt=DEFALUT_VERSION;
		}
		return rt;
	}
	/**
	 * 获取版本文件
	 * @return
	 */
	public File getVersionPathFile()
	{
		String path = SettingConfig.getVersionPath();
		File dir = new File(path);
		if(dir.exists())
		{
			if(dir.isDirectory())
			{
				File [] files =dir.listFiles();
				if(files!=null && files.length>0)
				{
					for(File f:files)
					{
						return f;
					}
				}
			}else
			{
				m_logger.error("MonitService getVersionPathFile path="+path+",is not a dir");
				
			}
		}else
		{
			m_logger.error("MonitService getVersionPathFile path="+path+",is not exist");
			
		}
		return null;
	}
	
	public void recordProcess(ProcessInfo info) throws Exception
	{
		monitDao.recordProcessInfo(info);
	}
	
	public void recordImage(ImageInfo info) throws Exception
	{
		monitDao.insertImage(info);
	}
	
}
