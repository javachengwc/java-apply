package com.manage.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.xwork.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.manage.util.WebappCommons;
import com.manage.web.CurrentRequestContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
/**   
 * 初始化currentRequestContext属性
 *    
 */
public class RequestContextInterceptor extends AbstractInterceptor{
  
	  private static final long serialVersionUID = -1288561454445706890L;
	
	  @Autowired
	  private CurrentRequestContext currentRequestContext;
	  
	  public RequestContextInterceptor()
	  {
		 // System.out.println("requestContextInterceptor inst craete--------");
	  }
	    
	  public CurrentRequestContext getCurrentRequestContext() {
		return currentRequestContext;
	 }
	
	  public void setCurrentRequestContext(CurrentRequestContext currentRequestContext) {
		this.currentRequestContext = currentRequestContext;
	 }
	
	  @Override	
	  public String intercept(ActionInvocation actionInvocation) throws Exception
	  {   
		  //System.out.println("---before currentRequestContext:"+currentRequestContext.toString());
		  
		  Object action =actionInvocation.getAction();
		  //System.out.println("---访问action:"+action.getClass().getName());
		  
		  HttpServletRequest request=(HttpServletRequest)actionInvocation.getInvocationContext().get(ServletActionContext.HTTP_REQUEST);
		  
//		  System.out.println("---访问请求相关参数：");
//		  System.out.println("---request.getAuthType():"+request.getAuthType());
//		  System.out.println("---request.getCharacterEncoding():"+request.getCharacterEncoding());
//		  System.out.println("---request.getContentType():"+request.getContentType());
//		  System.out.println("---request.getContextPath():"+request.getContextPath());
//		  System.out.println("---request.getQueryString():"+request.getQueryString());
//		  System.out.println("---request.getServletPath():"+request.getServletPath());
		  //////
		  currentRequestContext.setAllowRequest(true);
		  
		  String uri=request.getRequestURI();
		  //System.out.println("---访问uri:"+uri);
		  String url=request.getRequestURL().toString();
		  //System.out.println("---访问url:"+url);
		  
		  String serverPath=url.substring(0,url.length()-uri.length());
		  serverPath=serverPath+request.getContextPath()+WebappCommons.URL_SEPARATOR;
		  
		  currentRequestContext.setServerPath(serverPath);
			
	      String relativePath=url.substring(serverPath.length()-1);
			
	      currentRequestContext.setRelativePath(relativePath);
	      currentRequestContext.setRemoteAddr(request.getRemoteAddr());
			
	      String refererURL=request.getHeader("Referer");
	      //System.out.println("---request.referURL:"+refererURL);
	      
	      String ua=request.getHeader(WebappCommons.USER_AGENT);
	      //System.out.println("---request.user_agent:"+ua);
	      
		  if(StringUtils.isNotBlank(refererURL))
		  {
			  String refererRelativePath=refererURL.substring(serverPath.length()-1);
			  currentRequestContext.setRefererURL(refererURL);
			  currentRequestContext.setRefererRelativePath(refererRelativePath);
		  }
		 
		  //System.out.println("---after currentRequestContext:"+currentRequestContext.toString());
		  
		  return actionInvocation.invoke();
	  }
	
	  public static final Method getActionMethod(Class<? extends Object> actionClass, String methodName) throws NoSuchMethodException {
	    Method method;
	    try {
	      method = actionClass.getMethod(methodName, new Class[0]);
	    }
	    catch (NoSuchMethodException e)
	    {
		     try{
		      String altMethodName = "do" + methodName.substring(0, 1).toUpperCase() + methodName.substring(1);
		      method = actionClass.getMethod(altMethodName, new Class[0]);
		     }catch(NoSuchMethodException ee)
		     {
		    	 return null;
		     }
	    }
	    return method;
	  }

}
