package com.util.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class AopProxy<T> implements InvocationHandler {

    private Object target; 

    private Object proxyInstance = null;
    
    private Aop aop;

    public AopProxy(Object target,Aop aop) { 
        this.target = target; 
        this.aop = aop;
    } 
   
	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		Object result = null;
		aop.before(method,args);
		result = method.invoke(this.target, args);
		aop.after(method,args);
		return result;
	}

    /**入口**/
    public static Object getProxyInstance(Object target, Aop aop) { 
        Class<?> targetClass = target.getClass();  
        ClassLoader loader = targetClass.getClassLoader(); 
        Class<?>[] interfaces = targetClass.getInterfaces(); 
        AopProxy<Object> handler = new AopProxy<Object>(target,aop); 
        if(handler.proxyInstance == null){
        	handler.proxyInstance = Proxy.newProxyInstance(loader, interfaces, handler);
        }
        return handler.proxyInstance;
    }

    /**例子**/
    public static void main(String args[])
    {
        Runnable runStan = new Runnable() {
            @Override
            public void run() {
                System.out.println("--------doing-----");
            }
        };
        Aop runAop = new Aop(){
            public void before(Method method, Object[] args) throws Exception {

                System.out.println("--------before do-----");
            }
            public void after(Method method, Object[] args) throws Exception {
                System.out.println("--------after do-----");
            }
        };

        Runnable runProxy =(Runnable)AopProxy.getProxyInstance(runStan,runAop);
        runProxy.run();
    }
}
