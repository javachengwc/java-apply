package com.flower;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.flower.exception.ActionInvocationException;
import org.apache.log4j.Logger;

import com.flower.annotation.Read;
import com.flower.annotation.Scope;
import com.flower.converter.ConverterFactory;
import com.flower.multipart.IMultiPartRequest;
import com.flower.multipart.MultipartRequest;
import com.flower.populator.PopulatorFactory;
import com.flower.util.GenericsUtils;
import com.util.web.HttpRenderUtil;


/**
 * Action基础类，所有Action必须继承自此类。BaseAction封装了参数填充、转发、方法调用等功能，并向子类提供render等渲染方法。
 */
public class BaseAction {

	private static final long serialVersionUID = 1312678678678678L;

	/**
	 * 日志对象. 在子类中不必单独定义日志对象，直接使用这里生成的即可
	 */
	protected final Logger logger = Logger.getLogger(getClass());

	public void processRequest(Method method, String methodName) throws Exception {
		logger.debug("will invoke method : "
				+ methodName);
		if (method == null) {
			throw new NoSuchMethodException("Can NOT find specified method: "
					+ methodName);
		}
		Object result = this.exec(method);
		if(result != null && result instanceof Return){
			Return ret = (Return)result;
			ret.process();
		}
	}

	/**
	 * 供内部使用的调用所指定方法的方法
	 * 
	 * @param method
	 *            所指定的需要被调用的方法
	 * @return 调用方法后所返回的结果
	 * @throws Exception
	 */
	private Object exec(Method method) throws Exception {

		// 获得所调用方法的参数类型和所使用的Annotation数组
		Class<?>[] type = method.getParameterTypes();
		Annotation[][] annotationArray = method.getParameterAnnotations();

		// 用于存放调用参数的对象数组
		Object[] paramTarget = new Object[type.length];

		// 构造调用所需要的参数数组
		for (int i = 0; i < type.length; i++) {
			Class<?> typeClasz = (Class<?>) type[i];
			Annotation[] annotation = annotationArray[i];

			if (annotation == null || annotation.length == 0) {
				throw new Exception(
						"Must specify a @Read annotation for every parameter in method: "
								+ method.getName());
			}
			Read read = (Read) annotation[0];

			if (typeClasz == Serializable.class)
				typeClasz = GenericsUtils.getGenericClass(getClass(), 1);
			else if (typeClasz == Serializable[].class)
				typeClasz = Array.newInstance(
						GenericsUtils.getGenericClass(getClass(), 1), 0)
						.getClass();

			// 生成当前的调用参数
			try{
				paramTarget[i] = getParameter(typeClasz, read, method, i);
			}catch(Throwable e){
				throw new ActionInvocationException(new IllegalArgumentException("参数不合法:" + read.key(), e));
			}
		}
		Object result = null;
		try {
			// 调用，并得到调用结果
			result = method.invoke(this, paramTarget);
		} catch(InvocationTargetException e){
			throw new ActionInvocationException("Process has bean aborted while processing method " + method.getName(), e.getCause());
		} catch(IllegalArgumentException e){
			throw new ActionInvocationException("Process has bean aborted while processing method " + method.getName(), e);
		} catch (RuntimeException e) {
			logger.debug("Process has bean aborted while processing method "
					+ method.getName() + " chain", e);
		}
		return result;
	}

	/**
	 * 获得转换后的方法参数的对象
	 */
	private Object getParameter(Class<?> type, Read read, Method method, int index) {

		// 获得属性key的值
		String key = read.key();

		// 如果有key值则代表该Annotation所描述的参数不是一个Bean
		if (key != null && key.length() > 0) {
			Object value = null;
			switch (read.scope()) {
				case REQUEST:
					value = DataFilter.getAttribute(key, Scope.REQUEST);
					break;
				case SESSION:
					value = DataFilter.getAttribute(key, Scope.SESSION);
					break;
				case APPLICATION:
					value = DataFilter.getAttribute(key, Scope.APPLICATION);
					break;
				default:
					if(type == File[].class){// value是
						if(getRequest() instanceof MultipartRequest){
							return ((MultipartRequest)getRequest()).getFile(key);
						}else{
							logger.warn("FAIL to get file , reason : REQUEST object is not instance of MultipartRequest");
							return null;
						}
					}else {
						// value是字符串类型
						String defaultValue = read.defaultValue();
						String[] params = (String[]) getRequest().getParameterMap()
								.get(key);
						if (params == null) {
							params = useDefaultValue(defaultValue) ? new String[1]
									: new String[0];
						}
						if (params.length < 1) {
							if (type.isArray()) {
								value = params;
							} else if (useDefaultValue(defaultValue)) {
								value = defaultValue;
							}
						} else if (params.length == 1) {
							value = type.isArray() ? refillParameters(params,
									defaultValue) : refillParameters(params,
									defaultValue)[0];
						} else {
							if (type.isArray()) {
								value = refillParameters(params, defaultValue);
							} else {
								if (useDefaultValue(defaultValue)) {
									value = defaultValue;
								} else {
									value = (params[0] == null || params[0].length() == 0) ? null
											: params[0];
								}
							}
						}
					}
		
					return ConverterFactory.getDefaultConverter().convertValue(value,
							type);
			}
			return value;
			// 否则代表本Annotation所描述的参数是一个Bean
		} else {
			Object value;
			switch (read.scope()) {
				case REQUEST:
					value = DataFilter.getAttribute(key, Scope.REQUEST);
					break;
				case SESSION:
					value = DataFilter.getAttribute(key, Scope.SESSION);
					break;
				case APPLICATION:
					value = DataFilter.getAttribute(key, Scope.APPLICATION);
					break;
				default:{
					if(type.isAssignableFrom(Map.class)){
						@SuppressWarnings("unchecked")
						List<Class> list = GenericsUtils.getMethodGenericParameterTypes(method, index);
						if(list.size() == 2 && list.get(0) == String.class && list.get(1) == String.class){
							Map<String, String> map = new LinkedHashMap<String, String>();
							@SuppressWarnings("unchecked")
							Map<String, String[]> paramMap = getRequest().getParameterMap();
							Set<String> keys = paramMap.keySet();
							for(String keyy : keys){
								map.put(keyy, paramMap.get(key)[0]);
							}
							value = map;
						}else{
							if(list.size() == 2){
								logger.warn("Map Type Parameter Must both be String : Occuring Point : " + method.toGenericString());
								value = null;
							}else{
								value = getRequest().getParameterMap();
							}
						}
					}else{
						try {
							value = type.newInstance();
						} catch (InstantiationException e) {
							logger
									.debug(
											"InstantiationException happened when initializing bussiness object",
											e);
							return null;
						} catch (IllegalAccessException e) {
							logger
									.debug(
											"IllegalAccessException happened when initializing bussiness object",
											e);
							return null;
						}
			
						PopulatorFactory.getDefaultPopulator().populate(getRequest(), value,
								null, null);
					}
				}
			}
			return value;
		}
	}

	/**
	 * 将从Request Map中的获得的String数组重新注值，对空值的地方赋予默认值
	 * 
	 * @param params
	 *            Request Map中的获得的String数组
	 * @param defaultValue
	 *            默认值
	 * @return 重新注值后的String数组
	 */
	private String[] refillParameters(String[] params, String defaultValue) {
		if (params == null)
			return null;

		for (int i = 0; i < params.length; i++) {
			if (params[i] == null || params[i].length() == 0) {
				params[i] = useDefaultValue(defaultValue) ? defaultValue : null;
			}
		}
		return params;
	}

	/**
	 * 判断是否使用默认值
	 * 
	 * @param defaultValue
	 *            默认值
	 * @return 是否使用默认值
	 */
	private boolean useDefaultValue(String defaultValue) {
		return defaultValue != null
				&& !defaultValue
						.equals("CORE_ANNOTATION_READ_DEFAULTVALUE_DEFAULT");
	}


	private Map<String, Method> methodMap;

	public boolean methodMapReady;

	public synchronized void initMethodMap(){
		methodMap = new HashMap<String, Method>();
		for (Class<?> clazz = this.getClass(); clazz != BaseAction.class; clazz = clazz.getSuperclass()) {
			Method[] tempMethods = clazz.getDeclaredMethods();
			for (Method tempMethod : tempMethods) {
				if (Modifier.PUBLIC == tempMethod.getModifiers()) {
					methodMap.put(tempMethod.getName(), tempMethod);
				}
			}
		}
		methodMapReady = true;
	}
	/**
	 * 以名称作为匹配条件搜索指定方法
	 */
	protected Method getActionMethod(String name) {
		if(!methodMapReady){
			return null;
		}
		return methodMap.get(name);
	}
	
	/**
	 * response输出
	 * 
	 * @param text
	 * @param contentType
	 * @throws IOException
	 */
	protected void render(String text, String contentType)
		throws IOException
	{
		HttpRenderUtil.render(text, contentType, getResponse());
	}

	/**
	 * 直接输出纯字符串
	 * 
	 * @param text
	 *            字符串内容
	 */
	protected void renderText(String text) {
		HttpRenderUtil.renderText(text, getResponse());
	}

	/**
	 * 直接输出纯HTML
	 * 
	 * @param html
	 *            HTML内容
	 */
	protected void renderHtml(String html) {
		HttpRenderUtil.renderHtml(html, getResponse());
	}

	/**
	 * 直接输出纯XML
	 * 
	 * @param xml
	 *            XML内容
	 */
	protected void renderXML(String xml) {
		HttpRenderUtil.renderXML(xml, getResponse());
	}
	
	protected void renderJSON(String json){
		HttpRenderUtil.renderJSON(json, getResponse());
	}
	
	/**
	 * 直接输出二进制数组. 通常用于直接输出图片或者附件等
	 * 
	 * @param mimetype 要输出的内容的MimeType
	 * @param content 要输出的内容
	 * @throws IOException
	 */
	protected void renderBinary(String mimetype, byte[] content)
		throws IOException
	{
		HttpRenderUtil.renderBinary(mimetype, content, getResponse());
	}
	
	/**
	 * 直接输出二进制数组. 通常用于直接输出图片或者附件等
	 * 
	 * @param mimetype 要输出的内容的MimeType
	 * @param source 包含要输出的内容的输入流
	 * @throws IOException
	 */
	protected void renderBinary(String mimetype, InputStream source)
		throws IOException
	{
		HttpRenderUtil.renderBinary(mimetype, source, getResponse());
	}

	protected HttpServletRequest getRequest() {
		return DataFilter.getRequest();
	}

	protected HttpServletResponse getResponse() {
		return DataFilter.getResponse();
	}

	public void setAttribute(String key, Object obj){
		setAttribute(key, obj, Scope.REQUEST);
	}
	public void setAttribute(String key, Object obj, Scope scope) {
		switch (scope) {
			case REQUEST:
				getRequest().setAttribute(key, obj);
				break;
			case SESSION:
				 getRequest().getSession().setAttribute(key, obj);
				 break;
			case APPLICATION:
				getRequest().getSession().getServletContext().setAttribute(key, obj);
				break;
			default:
				getRequest().setAttribute(key, obj);
				break;
		}
	}
	
	public Object getAttrbute(String key, Scope scope){
		Object attr = null;
		switch (scope) {
			case REQUEST:
				attr = getRequest().getAttribute(key);
				break;
			case SESSION:
				attr = getRequest().getSession().getAttribute(key);
				break;
			case APPLICATION:
				attr = getRequest().getSession().getServletContext().getAttribute(key);
				break;
			default:
				break;
		}
		return attr;
	}
	/**
	 * 获取IMultipartRequest对象
	 * @return 假如请求对象本身不是MultipartRequest，则返回null。
	 */
	protected IMultiPartRequest getMultipartRequest(){
		HttpServletRequest request = getRequest();
		if(request instanceof IMultiPartRequest){
			return (IMultiPartRequest)request;
		}else{
			return null;
		}
	}
}
