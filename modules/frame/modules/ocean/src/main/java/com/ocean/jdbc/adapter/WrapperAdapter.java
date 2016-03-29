package com.ocean.jdbc.adapter;

import com.ocean.exception.ShardException;
import com.ocean.util.MethodInvocation;

import java.lang.reflect.Method;
import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;

/**
 * JDBC Wrapper适配类
 */
public class WrapperAdapter implements Wrapper {

    private Collection<JdbcMethodInvocation> jdbcMethodInvocations = new ArrayList<JdbcMethodInvocation>();

    @Override
    public final <T> T unwrap(final Class<T> iface) throws SQLException {
        if (isWrapperFor(iface)) {
            return (T) this;
        }
        throw new SQLException(String.format("%s cannot be unwrapped as %s", getClass().getName(), iface.getName()));
    }

    @Override
    public final boolean isWrapperFor(final Class<?> iface) throws SQLException {
        return iface.isInstance(this);
    }

    /**
     * 记录方法调用
     * @param targetClass 目标类
     * @param methodName 方法名称
     * @param argumentTypes 参数类型
     * @param arguments 参数
     */
    protected final void recordMethodInvocation(final Class<?> targetClass, final String methodName, final Class<?>[] argumentTypes, final Object[] arguments) {
        try {
            jdbcMethodInvocations.add(new JdbcMethodInvocation(targetClass.getMethod(methodName, argumentTypes), arguments));
        } catch (final NoSuchMethodException ex) {
            throw new ShardException(ex);
        }
    }

    /**
     * 回放记录的方法调用
     * @param target 目标对象
     */
    protected final void doMethodsInvovation(final Object target) {
        for (MethodInvocation each : jdbcMethodInvocations) {
            each.invoke(target);
        }
    }

    private class JdbcMethodInvocation extends MethodInvocation
    {
        public JdbcMethodInvocation(Method method,Object[] arguments)
        {
            super(method,arguments);
        }

        public void handleException(Exception e)
        {
            throw new ShardException("Invoke jdbc method exception", e);
        }
    }
}
