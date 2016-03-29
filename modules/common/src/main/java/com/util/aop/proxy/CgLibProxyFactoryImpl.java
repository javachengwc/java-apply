package com.util.aop.proxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.util.aop.Areacut;
import com.util.aop.advice.MethodInvocation;
import com.util.aop.advice.AfterAdvice;
import com.util.aop.advice.AroundAdvice;
import com.util.aop.advice.BeforeAdvice;
import com.util.aop.advice.ThrowsAdvice;
import com.util.asm.AsmClassUtil;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;


public class CgLibProxyFactoryImpl implements MethodInterceptor, ProxyFactory{

    //父类
	private Class<?> superClass;
    //接口类列表
	private List<Class<?>> interfaceList = new ArrayList<Class<?>>();
    //接口实现类
	private Map<Class<?>, Object> interfaceDelegateObjectMap = new HashMap<Class<?>, Object>();

    //各增强切面列表
	private List<Areacut> beforeAreacutList = new ArrayList<Areacut>();
	private List<Areacut> afterAreacutList = new ArrayList<Areacut>();
	private List<Areacut> AroundAreacutList = new ArrayList<Areacut>();
	private List<Areacut> throwsAreacutList = new ArrayList<Areacut>();
	
	public void setSuperClass(Class<?> superClass){
		this.superClass = superClass;
	}

    //增加接口，及接口实现实例
	public void addDelegate(Class<?> interfaceClass, Object interfaceDelegateObject){
		interfaceList.add(interfaceClass);
		interfaceDelegateObjectMap.put(interfaceClass, interfaceDelegateObject);
	}

	//增加增强切面
	public void addAreacut(Areacut areacut){
		if(areacut.getAdvice() instanceof BeforeAdvice){
			beforeAreacutList.add(areacut);
		}
		if(areacut.getAdvice() instanceof AfterAdvice){
			afterAreacutList.add(areacut);
		}
		if(areacut.getAdvice() instanceof AroundAdvice){
			AroundAreacutList.add(areacut);
		}
		if(areacut.getAdvice() instanceof ThrowsAdvice){
			throwsAreacutList.add(areacut);
		}
		
	}

    //获取接口数据
	private Class<?>[] getInterfaces(){
		Class<?>[] cs = new Class[interfaceList.size()];
		for(int i=0; i<interfaceList.size(); i++){
			cs[i] = interfaceList.get(i);
		}
		return cs;
	}

    //是否同方法
	private boolean isSameMethod(Method m1, Method m2){
		if(!m1.getName().equals(m2.getName())){
			return false;
		}
		Class<?>[] paramTypes1 = m1.getParameterTypes();
		Class<?>[] paramTypes2 = m2.getParameterTypes();
		if(paramTypes1.length != paramTypes2.length){
			return false;
		}
		
		for(int i=0;i<paramTypes1.length; i++){
			if(!paramTypes1[i].isAssignableFrom(paramTypes2[1])){
				return false;
			}
		}
		return true;
	}

    //根据方法获取对应的接口实现对象
	private Object getDelegateObject(Method method){
		for(Class<?> clazz : interfaceList){
			Method[] mm = clazz.getMethods();
			for(Method m : mm){
				if(isSameMethod(method, m)){
					return interfaceDelegateObjectMap.get(clazz);
				}
			}
		}
		return null;
	}

    /**具体的生成代理类对象的各方法调用处理**/
	public Object intercept(Object obj, Method method, Object[] args,MethodProxy proxy) throws Throwable {
		Object result = null;
        //根据方法找对应的接口类对象
		Object delegateObject = getDelegateObject(method);
		Object target = null;
		if(delegateObject == null){
			target = obj;
		}else{
			target = delegateObject;
		}
		try {
            //调用前增强切面处理
			beforeWeave(method, args, target);

            //调用中增强切面处理
			for(Areacut areacut : AroundAreacutList){
				if(areacut.getPointcut().matches(method, args, target)){
					Object adviceResult = null;
					try {
						if(delegateObject == null){
							adviceResult = ((AroundAdvice)areacut.getAdvice()).around(new MethodInvocation(proxy, method, args, target));
						}else{
							adviceResult = ((AroundAdvice)areacut.getAdvice()).around(new MethodInvocation(null, method, args, target));
						}

                        //调用后增强切面处理
						afterWeave(adviceResult, method, args, target);
					} catch (Exception e) {
                        //异常增强处理
						exceptionWeave(method, args, target, e);
						return adviceResult;
					}
					return adviceResult;
				}//if
			}//for

            //莫有调用中增强切面处理，才执行这里
			if(delegateObject == null){
				result =  proxy.invokeSuper(obj, args);
			}else{
				result =  method.invoke(delegateObject, args);
			}

            //调用后增强切面处理
			afterWeave(result, method, args, target);
		} catch (Exception e) {
            //异常增强处理
			exceptionWeave(method, args, target, e);
		}
		return result;
	}

    /**增强植入**/
	private void beforeWeave(Method method, Object[] args, Object target)throws Throwable{
		for(Areacut areacut : beforeAreacutList){
			if(areacut.getPointcut().matches(method, args, target)){
				((BeforeAdvice)areacut.getAdvice()).before(method, args, target);
			}
		}
	}
	private void afterWeave(Object returnObj, Method method, Object[] args, Object target)throws Throwable{
		for(Areacut areacut : afterAreacutList){
			if(areacut.getPointcut().matches(method, args, target)){
				((AfterAdvice)areacut.getAdvice()).afterReturning(returnObj, method, args, target);
			}
		}
	}
	private void exceptionWeave(Method method, Object[] args, Object target, Throwable t)throws Throwable{
		for(Areacut areacut : throwsAreacutList){
			if(areacut.getPointcut().matches(method, args, target)){
				((ThrowsAdvice)areacut.getAdvice()).afterThrowing(t);
			}
		}
	}

    /**返回代理类实例**/
	public Object getProxy(){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(superClass);
		enhancer.setInterfaces(getInterfaces());
		enhancer.setCallback(this);
		return enhancer.create();
	}

    /**返回生成的代理类**/
	public Class<?> createProxyClass(){
		Enhancer enhancer = new Enhancer();
		enhancer.setSuperclass(superClass);
		enhancer.setInterfaces(getInterfaces());
		enhancer.setCallbackType(this.getClass());
		return enhancer.createClass();
	}

    /**把生成的类class写入某文件**/
	public void writeToFile(String dir)throws IOException{
		Class<?> proxyClass = createProxyClass();
		String path = dir + AsmClassUtil.getByteCodeName(proxyClass) + ".class";
		if(!new File(path).getParentFile().exists()){
			new File(path).getParentFile().mkdirs();
		}
		byte[] byteCode = AsmClassUtil.getByteCode(proxyClass);
		FileOutputStream fos = new FileOutputStream(path);
		fos.write(byteCode);
		fos.close();
	}

}
