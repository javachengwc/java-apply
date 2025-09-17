package com.util.inject;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类说明：注入器，提供注入服务，注入的来源是BeanFactory
 */
public class Injector {
	private static Logger m_logger = LoggerFactory.getLogger(Injector.class);

	
	/**
	 * 传入要注入的对象，方法将扫描该对象的域成员，假如有做注入注解的将做注入。 
	 * @param toInject 要做注入的对象
	 * @throws Exception 
	 */
	public static void doInject(Object toInject) throws Exception{
		if(toInject == null){
			throw new IllegalArgumentException("object to inject SHOULD NOT be NULL");
		}else{
			doInjectInternal(toInject, toInject.getClass());
		}
	}
	
	/**
	 * 做实际的注入工作，方法将扫描类对象，并找出要做注入的域一一注入。
	 * @param bean 要做注入的对象
	 * @param clazz 要做注入的对象的类对象
	 * @throws Exception 
	 */
	private static void doInjectInternal(Object bean, Class<?> clazz) throws Exception{
		m_logger.info("bean:"+clazz.getName());
		Field fields[] =clazz.getDeclaredFields();
		try{
			for(Field field:fields)
			{
				Inject injectAnnotation=field.getAnnotation(Inject.class);
				if(injectAnnotation==null)
					continue;
				boolean single =injectAnnotation.isSingle();
				String instance =injectAnnotation.instance().getName();		
				Class<?> type=field.getType();
				String dependClassName=type.getName();
				if(null == instance || "".equals(instance))
				{
					if(type.isInterface())
					{
						throw new RuntimeException("通过接口的依赖未指定其实现! field name : " + type + " of class : " + clazz.getName());
					}
				}else
				{
					dependClassName=instance;
				}
				Object inst=null;
				if(single)
				{
				    inst = getBean(dependClassName);
				    if(inst==null)
				    {
				    	m_logger.debug("regist dependClassName:"+dependClassName);
				    	BeanFactory.regist(dependClassName);
				    	inst = getBean(dependClassName); 
				    }
				}else
				{
					//待再处理 浅复制
					inst =Class.forName(dependClassName).newInstance();
				}
				field.setAccessible(true);
				field.set(bean, inst);
			}
			Class<?> superClass = clazz.getSuperclass();
			if(superClass == Object.class){
				return ;
			}else{
				doInjectInternal(bean, superClass);
			}
		}catch(Exception e){
			m_logger.error("FAIL to DO INJECT FOR CLASS	 : " + bean.getClass(), e);
			throw e;
		}
	}
	
	/**
	 * 根据关键字获取实例
	 * @param key 
	 * @return
	 */
	private static Object getBean(String key)
	{
		return BeanFactory.getBean(key);
	}
}
