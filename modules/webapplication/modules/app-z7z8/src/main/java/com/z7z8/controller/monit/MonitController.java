package com.z7z8.controller.monit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.z7z8.model.monit.ImageInfo;
import com.z7z8.model.monit.ProcessInfo;
import com.z7z8.model.monit.SettingInfo;
import com.z7z8.service.monit.AttachManager;
import com.z7z8.service.monit.MonitService;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("monit")
public class MonitController {
	
    private static Logger m_logger = LoggerFactory.getLogger(MonitController.class);

	@Autowired
	private MonitService monitService;

	@ResponseBody
	@RequestMapping("getSetting")
	public JSONObject getSetting() 
	{
		SettingInfo info =monitService.getSetting();
		String version = monitService.getLastedVersionNo();
		
		JSONObject json = new JSONObject();
		json.put("uploadInterval", info.getUploadInterval());
		json.put("exitCode", info.getExitCode());
		json.put("version", version);
		
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		
//		request.getContextPath(); /mll
//		request.getRequestURI(); /mll/monit/getSetting.do
//		request.getRequestURL(); http://localhost:8081/mll/monit/getSetting.do
//		request.getServletPath(); /monit/getSetting.do
		
		String requestUrl =request.getRequestURL().toString();
		String versionDownloadPath = requestUrl.substring(0,requestUrl.lastIndexOf("/")+1)+"downloadVerson.do";
		json.put("versionPath",versionDownloadPath);
		return json;
	}
	
	@RequestMapping("downloadVerson")  
	public ResponseEntity<byte[]> downloadVerson() throws IOException { 
		
		System.out.println("MonitController downloadVerson start");
	    HttpHeaders headers = new HttpHeaders();  
	    headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    String fileName ="version";
	    headers.setContentDispositionFormData("attachment",fileName);
	    File versionFile = monitService.getVersionPathFile();
	    
	    if(versionFile!=null)
	    {
	    	fileName = versionFile.getName();
	    	headers.setContentDispositionFormData("attachment",fileName);
	        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(versionFile), headers, HttpStatus.CREATED); 
	    }else
	    {	    	
	    	return new ResponseEntity<byte[]>(new byte[0] , headers, HttpStatus.CREATED); 
	    }
	} 
	
	@ResponseBody
	@RequestMapping("recordProcess")
	public Object recordProcess(String info)
	{
		m_logger.info("MonitController recordProcess start");
		//校验
		if(StringUtils.isBlank(info))
		{
			m_logger.info("MonitController recordProcess param valid fail, info="+info);
			JSONObject json =new JSONObject();
			json.put("error", 1);
			json.put("msg", "参数验证失败");
			json.put("now", System.currentTimeMillis());
			return json;
		}
		ProcessInfo processInfo =null;
		try
		{
		   processInfo =JSON.parseObject(info, ProcessInfo.class);
		}catch(Exception e)
		{
			m_logger.error("MonitController recordProcess param json to object error,info="+info,e);
			JSONObject json =new JSONObject();
			json.put("error", 1);
			json.put("msg", "json数据转换失败");
			json.put("now", System.currentTimeMillis());
			return json;
		}
		try
		{
			monitService.recordProcess(processInfo);
			JSONObject json =new JSONObject();
			json.put("error", 0);  //标示成功
			json.put("now", System.currentTimeMillis());
			return json;
		}catch(Exception e)
		{
			JSONObject json =new JSONObject();
			json.put("error", 1);
			json.put("msg", "程序应用异常");
			json.put("now", System.currentTimeMillis());
			return json;
		}
	}
	
	@ResponseBody
	@RequestMapping("recordImage")
	public Object recordImage(@RequestParam(value = "file", required = false) MultipartFile file,String userName,String machineName,String time,String fileName  )
	{
		m_logger.info("MonitController recordImage start");
		System.out.println("MonitController recordImage start userName="+userName+",fileName="+fileName+",time="+time+",machineName="+machineName);
		//校验
		if(StringUtils.isBlank(userName) )
		{
			m_logger.info("MonitController recordImage param valid fail, userName="+userName+",fileName="+fileName+
					",time="+time+",machineName="+machineName);
			
			JSONObject json =new JSONObject();
			json.put("error", 1);
			json.put("msg", "参数验证失败");
			json.put("now", System.currentTimeMillis());
			return json;
		}
		String path=null;
		//文件处理
		try
		{
			// 获得文件名：   
	        //String filename = file.getOriginalFilename();   
	        // 获得输入流：
			if(file==null)
			{
				System.out.println("MonitController recordImage file is null");
				m_logger.error("MonitController recordImage  file is null");
				JSONObject json =new JSONObject();
				json.put("error", 1);
				json.put("msg", "文件为空");
				json.put("now", System.currentTimeMillis());
				return json;
			}else
			{   
				String defaultSuffix=".jpg";
		        InputStream input = file.getInputStream();
		        path = AttachManager.getInstance().saveFile(input,fileName,defaultSuffix);
			}
		}catch(Exception e)
		{
			m_logger.error("MonitController recordImage deal file error",e);
			JSONObject json =new JSONObject();
			json.put("error", 1);
			json.put("msg", "文件处理失败");
			json.put("now", System.currentTimeMillis());
			return json;
		}
		try
		{
			ImageInfo image = new ImageInfo();
			image.setFileName(fileName);
			image.setUserName(userName);
			image.setMachineName(machineName);
			Date date = new Date();
			if(!StringUtils.isBlank(time))
			{
				DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		        date =format.parse(time);
			}
			image.setTime(date);
			System.out.println("path:"+path);
			image.setPath(path);
			monitService.recordImage(image);
			JSONObject json =new JSONObject();
			json.put("error", 0);  //标示成功
			json.put("now", System.currentTimeMillis());
			return json;
		}catch(Exception e)
		{
			JSONObject json =new JSONObject();
			json.put("error", 1);
			json.put("msg", "程序应用异常");
			json.put("now", System.currentTimeMillis());
			return json;
		}
	}
	
	public static void main(String args []) throws Exception
	{
		String json ="{\"UserName\":\"xiaowanyong\",\"MachineName\":\"MLL-0701\",\"Time\":\"2014-05-17T15:16:43.9354077+08:00\",\"Items\":[{\"Name\":\"explorer\",\"Title\":\"\",\"FileName\":\"\"}]}";
		ProcessInfo info = JSON.parseObject(json, ProcessInfo.class);
		//DateTime.ParseExact("2013-11-17T11:59:22+08:00","yyyy-MM-ddTHH:mm:ss+08:00",null);
		Date d = info.getTime();
		DateFormat ft = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		//System.out.println(format.format(d));
		System.out.println(info);
		DateFormat format2= DateFormat.getDateInstance();
		System.out.println("-----------");
		System.out.println(ft.format(format.parse("2014-05-17T15:16:43.9354077+08:00")));
		//Calendar c =Calendar.getInstance();
		System.out.println(File.separator);
		String sdhj="http://localhost:8081/monit/getSetting.do";
		
		String ap = sdhj.substring(0,sdhj.lastIndexOf("/")+1)+"downloadVerson.do";
		System.out.println(ap);
		
	}

}
