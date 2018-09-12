package com.mybatis.pseudocode.mybatis.session;

//这个是对查询出来已经通过ResultSetHandler处理过的结果进行再处理的处理类，相当于是查询结果的后置处理器
//一般情况下都用不上它
public abstract interface ResultHandler<T>
{
    public abstract void handleResult(ResultContext<? extends T> resultContext);
}
