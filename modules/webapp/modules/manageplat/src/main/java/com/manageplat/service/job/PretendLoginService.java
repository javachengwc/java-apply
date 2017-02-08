package com.manageplat.service.job;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.manageplat.model.job.JobHttpUrl;
import com.manageplat.util.HttpClientCookieUtil;
import com.manageplat.util.SysConfig;
import com.util.enh.RandomUtil;
import com.util.http.UrlUtil;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 伪装登陆
 */
@Service
public class PretendLoginService {
	
	private static Logger logger =  Logger.getLogger(PretendLoginService.class);
	
    private static String mainSites [] ={};
	
	private static String workSites [] ={};

	/**
	 * 0---后端  1---前端 -1---其他
	 */
	public int getUrlFrom(JobHttpUrl url)
	{
		if(Arrays.asList(mainSites).contains(url.getDomain()))
		{
			return 1;
		}
		if(Arrays.asList(workSites).contains(url.getDomain()))
		{
			return 0;
		}
		return -1;
	}
	
	public static void main(String[] args) {
	    PretendLoginService sercie=new PretendLoginService();
	    JobHttpUrl url=new JobHttpUrl("http://www.baidu.com",null);
	    sercie.login(url);
	    
    }
	
	/**
	 * 登陆 可能会返回登陆状态cookie
	 */
	public Map<String,String> login(JobHttpUrl url)
	{
		Map<String,String> result=new HashMap<String,String>();
		//后端任务
		if(getUrlFrom(url)==0)
		{
			String userName = url.getParam("user_name");
			if(StringUtils.isBlank(userName))
			{
				userName ="";
			}
			String token ="需要的token";//待具体获取
			String password="需要的password";//待具体获取

			String loginUrl = SysConfig.getValue(url.getDomain() + ".login");
			String connectStr="?";
			if(loginUrl.indexOf("?")>0)
			{
				connectStr="&";
			}
			loginUrl +=connectStr+"user_name="+userName+"&password="+password;
			
            Map<String,String> loginData =doHttpInvoke(loginUrl,null,true,true);
			
			logger.error("PretendLoginService login "+loginUrl+",return \r\n"+loginData.get("error")+","+loginData.get("msg")+",\r\n cookie:"+loginData.get("Cookie"));
			
			return loginData;
			
		}else
		{
			//原先是先访问站点，获取初始化cookie,但cookie为空，可能cookie是在页面js生成的
			//随机生成一个40的字符
			String cookieString =null;
			if(url.getDomain().indexOf("cc.com")>=0)
			{
				Cookie ck = new Cookie();
                //具体域名具体处理
				ck.setDomain(".cc.com");
				ck.setName("random");
				ck.setPath("/");
				ck.setValue(RandomUtil.getRandomString(40));
				cookieString = HttpClientCookieUtil.cookieTransStr(new Cookie[]{ck});
			}
			String loginUrl = SysConfig.getValue(url.getDomain()+".login");
			
			if(StringUtils.isBlank(loginUrl))
			{
				return makeResponse("1","未获取到站点:"+url.getDomain()+"的登录地址",result);
			}
			String username = url.getParam("username");
			String password = url.getParam("password");
			String connectStr="?";
			
			if(loginUrl.indexOf("?")>0)
			{
				connectStr="&";
			}
			loginUrl +=connectStr+"username2="+username+"&password2="+password;
			
			Map<String,String> loginData =doHttpInvoke(loginUrl,cookieString,true,true);
			
			logger.error("PretendLoginService login "+loginUrl+",return \r\n"+loginData.get("error")+","+loginData.get("msg")+",\r\n cookie:"+loginData.get("Cookie"));
			
			return loginData;
		}
	}
	
	private Map<String,String> makeResponse(String code, String message, Map<String, String> result) {
        result.put("error", code);
        result.put("msg", message);
        return result;
    }

	/**
	 * http调用
	 * @param url
	 * @param addCookieStr
	 * @param needRtCookie 需要返回cookie
	 * @param jsonValidate 结果需要转成json验证
	 * @return
	 */
	public Map<String,String> doHttpInvoke(String url,String addCookieStr,boolean needRtCookie,boolean jsonValidate)
	{
		Map<String,String> result=new HashMap<String,String>();
		
		HttpClient client = new HttpClient();
		
		client.getHttpConnectionManager().getParams().setConnectionTimeout(10000);
		client.getHttpConnectionManager().getParams().setSoTimeout(10000);

		int returnStatus=0;
		String	returnValue=null;
		String cookie=null;
		HttpMethod request=null;
		String returnCkStr=null;
		int time=0;
		int tryCount=3;
		boolean needDo=true;
		while(time<tryCount && needDo)
		{
			try {
				time++;
				request = new GetMethod(url);
				if(!StringUtils.isBlank(addCookieStr))
				{
				    request.addRequestHeader("Cookie", addCookieStr);
				}
				
				returnStatus = client.executeMethod(request);
				returnValue=request.getResponseBodyAsString();
				if(needRtCookie)
				{
	//			     Cookie[] cookies = client.getState().getCookies();  
	//			     for ( int i = 0; i < cookies.length; i++) {  
	//			    	 System.out.println(cookies[i].toString());  
	//		         }
					Header cookieHead =request.getResponseHeader("Set-Cookie");
					
					if(cookieHead!=null)
					{
						cookie = cookieHead.getValue();
					}
				}
				needDo=false;
			} catch (Exception e) {
				
				logger.error("PretendLoginService doHttpInvoke htttclient url=" + url, e);
				if(time>=tryCount)
				{
					result.put("error", "1");
					result.put("msg", "PretendLoginService doHttpInvoke htttclient exception,"+e.getClass().getName()+","+e.getMessage());
					return result;
				}
				
			} finally {
				
				if(request!=null)
				{
					request.releaseConnection();
				}
	        }
		}
		
		boolean success=false;
		
		if(jsonValidate)
		{
			if(HttpStatus.SC_OK == returnStatus && !StringUtils.isBlank(returnValue))
			{
				try{
					JSONObject json = JSON.parseObject(returnValue);
					if(json.get("error")!=null && "0".equals(json.get("error").toString()))
					{
						success=true;
						Object msgObj = json.get("msg");//可能只在后台登陆返回的时候才有值
						if(msgObj!=null)
						{
						    returnCkStr = msgObj.toString();
						}
					}
				}catch(Exception e)
				{
					logger.error("PretendLoginService doHttpInvoke json parseObject error, value=" + returnValue, e);
				}
			}
		}else
		{
			if (HttpStatus.SC_OK == returnStatus)
			{
				success=true;
			}
		}
		
		if(!StringUtils.isBlank(returnValue))
        {
            int length =( (returnValue.length()>150)?150:returnValue.length());
            returnValue=returnValue.substring(0,length);
        }
		logger.error("PretendLoginService doHttpInvoke returnStatus="+ returnStatus+",returnValue="+ returnValue);
		
		String msg ="returnStatus="+ returnStatus+",returnValue="+ returnValue;
		result.put("error", "1");
		result.put("msg", msg);
		
		if (success) {
			result.put("error", "0");
			
			if(needRtCookie)
			{
				if(!StringUtils.isBlank(addCookieStr))
				{
				   if(StringUtils.isBlank(cookie))
				   {
					   cookie = addCookieStr;
				   }else
				   {
				      cookie += ", "+addCookieStr;
				   }
				}
				if(jsonValidate && !StringUtils.isBlank(returnCkStr))
				{
					String dm = UrlUtil.getDomain(url);
					if(workSites[0].equalsIgnoreCase(dm) || workSites[1].equalsIgnoreCase(dm))
					{
						//返回的实体中带了cookie，依返回实体中的cookie为准
						cookie =returnCkStr;
					}
				}
				
				result.put("Cookie", cookie);
			}
		}
		return result;
	}
	
	public Map<String,String> doHttpInvoke(String url,String addCookieStr,boolean needRtCookie)
	{
		return  doHttpInvoke(url,addCookieStr,needRtCookie,false);
	}

}
