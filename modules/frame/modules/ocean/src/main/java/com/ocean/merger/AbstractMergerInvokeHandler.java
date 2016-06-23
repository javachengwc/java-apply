package com.ocean.merger;

import com.ocean.jdbc.ShardResultSet;
import com.ocean.merger.resultset.ResultSetQueryIndex;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.SQLException;

/**
 * 结果归并动态代理抽象类
 */
public abstract class AbstractMergerInvokeHandler<T extends ShardResultSet> implements InvocationHandler {

    private T resultSet;

    public AbstractMergerInvokeHandler(T resultSet)
    {
        this.resultSet=resultSet;
    }

    public T getResultSet() {
        return resultSet;
    }

    public void setResultSet(T resultSet) {
        this.resultSet = resultSet;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if (!isGetDataMethod(method, args)) {
            return method.invoke(resultSet, args);
        }
        return doMerge(resultSet, method, new ResultSetQueryIndex(args[0]));
    }

    private boolean isGetDataMethod(final Method method, final Object[] args) {
        return method.getName().startsWith("get") && null != args && 1 == args.length;
    }

    protected abstract Object doMerge(T resultSet, Method method, ResultSetQueryIndex resultSetQueryIndex) throws ReflectiveOperationException, SQLException;

    protected Object invokeOriginal(final Method method, final ResultSetQueryIndex resultSetQueryIndex) throws ReflectiveOperationException {
        return method.invoke(resultSet, new Object[] {resultSetQueryIndex.getRawQueryIndex()});
    }
}
