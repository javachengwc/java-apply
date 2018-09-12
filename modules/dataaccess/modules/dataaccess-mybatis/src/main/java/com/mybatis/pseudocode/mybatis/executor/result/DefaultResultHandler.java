package com.mybatis.pseudocode.mybatis.executor.result;

import com.mybatis.pseudocode.mybatis.session.ResultContext;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import org.apache.ibatis.reflection.factory.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

//此类就是把查询出来已经通过ResultSetHandler处理过的结果转存在自己的list成员中,没啥用处
public class DefaultResultHandler implements ResultHandler<Object>
{
    private final List<Object> list;

    public DefaultResultHandler()
    {
        this.list = new ArrayList();
    }

    public DefaultResultHandler(ObjectFactory objectFactory)
    {
        this.list = ((List)objectFactory.create(List.class));
    }

    public void handleResult(ResultContext<? extends Object> context)
    {
        this.list.add(context.getResultObject());
    }

    public List<Object> getResultList() {
        return this.list;
    }
}
