package com.spring.pseudocode.aop.aopalliance.intercept;

import java.lang.reflect.AccessibleObject;

//连接点,程序执行过程中明确的点，就是Java程序执行过程中的方法。
//Joinpoint通过抽象实现成为一个个的Method，在执行每个JoinPoint所代表的Method中，会执行对应的Advice
public abstract interface Joinpoint
{
    //在实现中完成Method及Advice的执行
    public abstract Object proceed()  throws Throwable;

    public abstract Object getThis();

    public abstract AccessibleObject getStaticPart();
}
