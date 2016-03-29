package com.util.aop.demo;

import com.util.aop.Areacut;
import com.util.aop.advice.BeforeAdvice;
import com.util.aop.pointcut.PointcutImpl;
import com.util.aop.proxy.CgLibProxyFactoryImpl;
import com.util.aop.proxy.ProxyFactory;

import java.lang.reflect.Method;

/**
 * cglig aop例子程序
 */
public class CgLibAopPrg {

    public static void main(String args[]){

		ProxyFactory pf = new CgLibProxyFactoryImpl();

        //父类
		pf.setSuperClass(A.class);

        //切面
        Areacut areacut = new Areacut();
        areacut.setAdvice(new BeforeAdvice(){

            public void before(Method method, Object[] args, Object target)throws Throwable
            {
                System.out.println("---invoke before"+method.getName());
            }
        });
        areacut.setPointcut(new PointcutImpl("who"));

        pf.addAreacut(areacut);


        //接口类以及实现
		pf.addDelegate(IMan.class, new SpecMan());

        //获取代理类
		Object proxy = pf.getProxy();
		A a = (A)proxy;
        //调用
		a.a();

		IMan man = (IMan)proxy;
        //调用
        String who =man.who();
		System.out.println(who);
    }

}
