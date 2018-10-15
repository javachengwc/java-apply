package com.mybatis.interceptor;

import com.mybatis.pseudocode.mybatis.session.RowBounds;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class }) })
public class MyInterceptor implements Interceptor {

    private static Logger logger= LoggerFactory.getLogger(Logger.class);

    private Properties properties;

    public Object intercept(Invocation invocation) throws Throwable {

        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
        String sqlId = mappedStatement.getId();
        //String namespace = sqlId.substring(0, sqlId.indexOf('.'));
        //Executor executor = (Executor) invocation.getTarget();
        String methodName = invocation.getMethod().getName();

        if (methodName.equals("query")) {
            //Object parameter = invocation.getArgs()[1];
            RowBounds rowBounds = (RowBounds) invocation.getArgs()[2];
            logger.info("MyInterceptor query: "+sqlId+",sql:###"+getSqlByInvocation(invocation)+
                        "###,param"+rowBounds.toString());
        }
        return invocation.proceed();
    }

    private String getSqlByInvocation(Invocation invocation) {
        final Object[] args = invocation.getArgs();
        MappedStatement ms = (MappedStatement) args[0];
        Object parameterObject = args[1];
        BoundSql boundSql = ms.getBoundSql(parameterObject);
        return boundSql.getSql();
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties=properties;
    }
}
