package com.util.inject;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.util.lang.PackageUtil;
import com.util.aop.Aop;
import com.util.aop.AopProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类说明：Bean工厂
 */
public class BeanFactory {
	
	private static Logger m_logger = LoggerFactory.getLogger(BeanFactory.class);
	
	private static Map<String,Object> beanMap = new HashMap<String,Object>();

	public static void init(String... packages){
		Set<String> clazzs =new HashSet<String>();
		for(String perPak:packages)
		{
			String[] classes = PackageUtil.findClassesInPackage(perPak);
			Collections.addAll(clazzs,classes);
		}
		BeanFactory.regist(clazzs);
	}
	
	public static void regist(Set<String> clazzs)
	{
		for(String clazzName:clazzs)
		{
			regist(clazzName);
		}
		show();
	}
	public static void regist(String clazzName)
	{
		if(exist(clazzName))
			return;
		try{
			m_logger.debug("invoke class forname:"+clazzName);
			Class<?> clazz=Class.forName(clazzName);
			m_logger.debug("invoke class forname over");
			
			//如果是注解Exclude就不管理
			Exclude beanAnnotation=(Exclude)clazz.getAnnotation(Exclude.class);
			if(beanAnnotation!=null)
				return;
	
			//逆名类
			if(clazz.isAnonymousClass()){			
				return;
			}
			
			Object bean =null;
			boolean useDefaultConstructor=false;//是否默认用无参的构造方法
			Constructor<?>[] css =clazz.getConstructors();
			for(Constructor<?> cs:css )
			{
				Class<?>[] pss=cs.getParameterTypes();
				if(pss==null || pss.length==0)
				{
					useDefaultConstructor=true;
					break;
				}
			}
			if(useDefaultConstructor)
			{
				bean =clazz.newInstance();
			}else
			{
				if(css.length>1)
				{
					m_logger.error("BeanFactory regist error when initiating " + clazzName+",not confirm which constructor");
					return;
				}
				else
				{
					Constructor<?> cc = css[0];
					Class<?>[] params=cc.getParameterTypes();
					Object [] values = new Object [params.length];
					for(int i=0;i<params.length;i++)
					{
						Class<?> param = params[i];
						Object obj = getBeanByClass(param);
						if(obj==null)
						{
                            BeanFactory.regist(param.getName());
                            obj = getBeanByClass(param);
						}
                        if(obj==null)
                        {
                            m_logger.error("BeanFactory regist error when initiating " + clazzName+",constructor init error ,not constrnctor param object");
                            return;
                        }
						else
						{
							values[i] =obj;
						}
					}
					bean = cc.newInstance(values);
				}
			}
			//AOP类的注入
			Class<?>[] interfaces = clazz.getInterfaces();
			for(Class<?> c : interfaces){
				Around around = c.getAnnotation(Around.class);
				if(around != null){
					Class<? extends Aop>[] classNames = around.classNames();
					for(int i = 0;i<classNames.length;i++){
						Aop aopInstance = classNames[i].newInstance();
						bean = AopProxy.getProxyInstance(bean, aopInstance);

						Injector.doInject(bean);
						beanMap.put(clazzName,bean);
					}
				}
			}
			
			
			Injector.doInject(bean);
			beanMap.put(clazzName,bean);
		}catch(Exception e)
		{
	    	m_logger.error("BeanFactory regist error when initiating " + clazzName, e);
		}
	}
	
	public static void prepareAl(Map<String, Object> map){
		beanMap.putAll(map);
	}
	
	public static void prepare(String key, Object value){
		beanMap.put(key, value);
	}
	
	public static void show()
	{
		for(String key:beanMap.keySet())
		{
			m_logger.debug(key+":"+beanMap.get(key));
		}
	}
	
	/**
	 * 根据关键字获取实例
	 * @param key 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static<T> T getBean(String key)
	{
		return (T) beanMap.get(key);
	}
	/**
	 * 查找某个类型的对象
	 * @param clazz
	 * @return
	 */
	public static Object getBeanByClass(Class<?> clazz)
	{   
		if(beanMap==null ||beanMap.size()<=0)
	    {
			return null;
	    }
		List<Object> list = new ArrayList<Object>();
		for(Object obj:beanMap.values())
		{
			//找类型一致的对象
			if(clazz.isAssignableFrom(obj.getClass()))
			{
				list.add(obj);
			}
		}
		if(list!=null && list.size()>0)
		{
			int step =Integer.MAX_VALUE;
			Object suitMax =null;
			for(Object obj:list)
			{   
				int st=0;
				Class<?> cs=obj.getClass();
				//遍历找层次最近的
				while(cs!=Object.class && clazz.isAssignableFrom(cs))
				{
					if(cs.getName().equals(clazz.getName()))
					{
						if(st<step)
						{
							step =st;
							suitMax=obj;
						}
						break;
					}else
					{
						cs =cs.getSuperclass(); 
					}
					st++;
				}
			}
			if(suitMax!=null)
			{
				return suitMax;
			}
			else
			{
				return list.get(0);
			}
			
		}
		return null;
	}
	
	public static boolean exist(String key)
	{
		return beanMap.containsKey(key);
	}
	
}
