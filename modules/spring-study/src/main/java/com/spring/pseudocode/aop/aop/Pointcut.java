package com.spring.pseudocode.aop.aop;

//切入点
//PointCut简单来说是一个基于表达式的拦截条件。用来指明Advice（增强）所作用的地方（一般指方法）
//PointCut接口及其实现类就是根据配置的类及方法的过滤规则在调用Advice之前进行过滤，看看是否需要调用Advice
public abstract interface Pointcut
{
    //匹配所有的类及方法，默认返回true
    public static final Pointcut TRUE = TruePointcut.INSTANCE;

    //通过pointcut表达式对类进行过滤
    public abstract ClassFilter getClassFilter();

    //通过pointcut表达式对方法进行过滤
    public abstract MethodMatcher getMethodMatcher();
}
