package com.mybatis.pseudocode.mybatis.executor.result;

import com.mybatis.pseudocode.mybatis.session.ResultContext;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import org.apache.ibatis.reflection.factory.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

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
