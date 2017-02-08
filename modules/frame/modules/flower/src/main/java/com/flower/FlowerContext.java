package com.flower;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.flower.config.FlowerConfig;
import com.flower.exception.DuplicateActionException;
import com.flower.exception.DuplicateInterceptorIdException;
import com.flower.exception.InterceptorNoExistException;
import com.util.lang.PackageUtil;
import com.util.inject.Injector;
import org.apache.log4j.Logger;

import com.flower.annotation.Interceptors;
import com.flower.annotation.Namespace;
import com.flower.config.InterceptorConfig;
import com.flower.interceptor.Interceptor;
import com.flower.interceptor.InterceptorWrapper;
import com.flower.invocation.ActionConfig;
import com.flower.invocation.ActionInvocation;
import com.flower.invocation.ActionProxy;
import com.util.ClassUtil;
import com.flower.util.MatchUtil;

public class FlowerContext {
	protected final Logger logger = Logger.getLogger(getClass());
	
	private Map<String, ActionWrapper> actions = new HashMap<String, ActionWrapper>();

	private Map<String, InterceptorWrapper> interceptors = new LinkedHashMap<String, InterceptorWrapper>();
	private Map<String, ActionConfig> action_config = new HashMap<String, ActionConfig>();

	public FlowerContext(String configFilePath){
		FlowerConfig config = FlowerConfig.parse(configFilePath);
		
		initInterceptorMap(config.getInterceptorConfigs(), interceptors);
		
		initActionMap(config, actions, ".action");
	}
	public FlowerContext(File configFile){
		FlowerConfig config = FlowerConfig.parse(configFile);
		
		initInterceptorMap(config.getInterceptorConfigs(), interceptors);
		
		initActionMap(config, actions, ".action");
	}
	
	public ActionWrapper getActionWrapper(String path){
		return actions.get(path);
	}
	private void initInterceptorMap(List<InterceptorConfig> iconfigs, Map<String, InterceptorWrapper> interceptorMap){
		logger.info("Interceptors begin initiating");
		for(InterceptorConfig iconfig : iconfigs){
			if(interceptorMap.containsKey(iconfig.id)){
				throw new DuplicateInterceptorIdException(iconfig.id); // 抛出异常，并使context启动失败（必须修复）
			}
			Class<?> clazz = null;
			try {
				clazz = Class.forName(iconfig.className);
			} catch (ClassNotFoundException e) {
				logger.error("Error when initiating interceptor : " + iconfig.className, e);
			}
			if(clazz != null){
				try {
					Interceptor interceptor = (Interceptor)clazz.newInstance();
					Injector.doInject(interceptor);
					interceptorMap.put(iconfig.id, new InterceptorWrapper(interceptor, iconfig.pattern, iconfig.excludes));
					logger.info("inititiating interceptor for class : " + clazz.getSimpleName());
				} catch (InstantiationException e) {
					logger.error("Error when initiating interceptor : " + iconfig.className, e);
				} catch (IllegalAccessException e) {
					logger.error("Error when initiating interceptor : " + iconfig.className, e);
				} catch (ClassNotFoundException e) {
					logger.error("Error when initiating interceptor : " + iconfig.className, e);
				} catch (Exception e) {
					logger.error("Error when initiating interceptor : " + iconfig.className, e);
				}
			}
		}
		logger.info("Interceptors inititalized successfully");
	}
	

	public ActionProxy getActionProxy(ActionWrapper actionWrapper) throws InterceptorNoExistException {
		
		// make config object according to action object and method name
		ActionConfig config = getActionConfig(actionWrapper);
		
		// make proxy object and inject action object , method name, and invocation object
		ActionProxy proxy = new ActionProxy();
		// make invocation object and inject config object
		ActionInvocation invocation = new ActionInvocation();
		invocation.init(config, proxy);
		
		proxy.setActionObject(actionWrapper.actionObject);
		proxy.setMethod(actionWrapper.method);
		proxy.setMethodName(actionWrapper.method.getName());
		proxy.setInvocation(invocation);
		return proxy;
	}
	
	private void initActionMap(FlowerConfig config, Map<String, ActionWrapper> actionMap, String suffix){
		logger.info("Actions begin initiating");
		List<String> packages = config.getActionPacages();
		logger.info("scaned packages : " + packages);
		for(String packagee : packages){
			logger.info("begin get classes from package : " + packagee);
			//Map<String, Class<?>> map = ClassUtil.getAllClassByDirectory(packagee);
			String[] classNames = PackageUtil.findClassesInPackage(packagee + ".*"); // 目录下通配
			for(String className : classNames){
				try {
					Class<?> actionClass = Class.forName(className);
					if(!BaseAction.class.isAssignableFrom(actionClass)){
						continue;
					}
					BaseAction baseAction = (BaseAction) actionClass
							.newInstance();
					Injector.doInject(baseAction);
					logger.info("registering action class : " + actionClass.getName());
					for(Method method : actionClass.getDeclaredMethods()){
						try{
							if(method.getModifiers() == Modifier.PUBLIC){
								String actionPath = findNamespace(actionClass, method, config) + method.getName() + suffix;
								if(actionMap.get(actionPath) != null){
									throw new DuplicateActionException(actionMap.get(actionPath).method, method, actionPath);
								}
								ActionWrapper actionWrapper = new ActionWrapper(baseAction, method, actionPath);
								actionMap.put(actionPath, actionWrapper);
								action_config.put(actionPath, getActionConfig(actionWrapper));
								logger.info("registering action[" + actionPath + "] for interceptors " + Arrays.toString(getActionConfig(actionWrapper).getInterceptors().toArray()));
							}
						}catch(InterceptorNoExistException e){
							logger.error("FAIL to initiate action config for method : " + method.getName(), e); // 配置异常
						}
					}
				} catch (InstantiationException e) {
					logger.error("FAIL to initiate action instance", e);
				} catch (IllegalAccessException e) {
					logger.error("FAIL to initiate action instance", e);
				} catch (ClassNotFoundException e) {
					logger.error("FAIL to initiate action instance", e);
				} catch(Exception e){
					logger.error("FAIL to initiate action instance", e);
				}
			}
		}
		logger.info("Actions inititalized successfully");
	}
	
	/**
	 * 根据方法和配置返回命名空间。规则是以方法的Namespace注解优先，假如没有该注解，则使用包名正则匹配
	 * @param actionClass
	 * @param method
	 * @param config
	 * @return
	 */
	private String findNamespace(Class<?> actionClass, Method method, FlowerConfig config){
		Namespace nsAnnotation = method.getAnnotation(Namespace.class);
		if(nsAnnotation != null){
			return nsAnnotation.value();
		}else{
			String retNs = null;
			for(com.flower.config.Namespace ns : config.getNamespaces()){
				if(Pattern.matches(ns.packages, actionClass.getPackage().getName())){
					retNs = ns.name;
				}
			}
			if(retNs != null){
				return retNs;
			}else{
				logger.warn("No Match namespace for method "  + method.toGenericString() + " package name is : " + actionClass.getPackage().getName());
				return "/";
			}
		}
	}
	
	
	private ActionConfig getActionConfig(ActionWrapper actionWrapper) throws InterceptorNoExistException{
		ActionConfig config = action_config.get(actionWrapper.actionPath);
		if(config != null){
			return config;
		}
		config = new ActionConfig();
		Method method = actionWrapper.method;
		List<Interceptor> methodInterceptors = new ArrayList<Interceptor>();
//		methodInterceptors.addAll(defaultInterceptors.values());
		
		// 先匹配ant表达式匹配的
		String actionMethodStr = ClassUtil.getMethodSignature(actionWrapper.method, false);
		for(InterceptorWrapper wrapper : interceptors.values()){
			if(wrapper.autoMatch != null && MatchUtil.matchStrings(wrapper.autoMatch, actionMethodStr)){
				if(wrapper.excludes == null || !MatchUtil.matchStrings(wrapper.excludes, actionMethodStr)){
					methodInterceptors.add(wrapper.interceptor);
				}
			}
		}
		
		// 再匹配显式指定的
		Interceptors refs = method.getAnnotation(Interceptors.class);
		if(refs != null){ // 被拦截器栈修饰
			com.flower.annotation.Interceptor[] interceptorRefs = refs.value();
			for(com.flower.annotation.Interceptor interceptorRef : interceptorRefs){
				if(!interceptors.containsKey(interceptorRef.id())){
					throw new InterceptorNoExistException(interceptorRef.id());
				}
				Interceptor temp = interceptors.get(interceptorRef.id()).interceptor;
				if(temp != null && !methodInterceptors.contains(temp)){
					methodInterceptors.add(temp);
				}
			}
		}else{ // 被单个拦截器修饰
			com.flower.annotation.Interceptor interceptorRef = method.getAnnotation(com.flower.annotation.Interceptor.class);
			if(interceptorRef != null){
				if(!interceptors.containsKey(interceptorRef.id())){
					throw new InterceptorNoExistException(interceptorRef.id());
				}
				Interceptor temp = interceptors.get(interceptorRef.id()).interceptor;
				if(temp != null){
					methodInterceptors.add(temp);
				}
			}else{
				// 没有拦截器修饰
				// do nothing
			}
		}
		config.setInterceptors(methodInterceptors);
		action_config.put(actionWrapper.actionPath, config);
		return config;
	}
}
