package com.mybatis.interceptor;

import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class,method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class,method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })})
public class ExampleInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(ExampleInterceptor.class);

    private Properties properties;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object[] queryArgs = invocation.getArgs();
        MappedStatement mappedStatement = (MappedStatement) queryArgs[0];
        MapperMethod.ParamMap paramMap = (MapperMethod.ParamMap) queryArgs[1];
        //获取sql
        BoundSql boundSql = mappedStatement.getBoundSql(paramMap);
        String sql = boundSql.getSql();
        logger.info("ExampleInterceptor exe sql={}"+sql);
        long startTime = System.currentTimeMillis();
        Configuration configuration = mappedStatement.getConfiguration();
        String sqlId = mappedStatement.getId();

        Object proceed = invocation.proceed();
        long endTime=System.currentTimeMillis();
        long time = endTime - startTime;
        printSqlLog(configuration,boundSql,sqlId,time);
        return proceed;
    }

    public static void printSqlLog(Configuration configuration, BoundSql boundSql, String sqlId, long time){
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        String sql= boundSql.getSql().replaceAll("[\\s]+", " ");
        StringBuffer sb=new StringBuffer("==> PARAM:");
        if (parameterMappings.size()>0 && parameterObject!=null){
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                sql = sql.replaceFirst("\\?", parameterObject.toString());
            } else {
                MetaObject metaObject = configuration.newMetaObject(parameterObject);
                for (ParameterMapping parameterMapping : parameterMappings) {
                    String propertyName = parameterMapping.getProperty();
                    if (metaObject.hasGetter(propertyName)) {
                        Object obj = metaObject.getValue(propertyName);
                        String parameterValue = obj.toString();
                        sql = sql.replaceFirst("\\?", parameterValue);
                        sb.append(parameterValue).append("(").append(obj.getClass().getSimpleName()).append("),");
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        Object obj = boundSql.getAdditionalParameter(propertyName);
                        String parameterValue = obj.toString();
                        sql = sql.replaceFirst("\\?", parameterValue);
                        sb.append(parameterValue).append("(").append(obj.getClass().getSimpleName()).append("),");
                    }
                }
            }
            sb.deleteCharAt(sb.length()-1);
        }
        logger.info("==> SQL:"+sql);
        logger.info(sb.toString());
        logger.info("==> SQL TIME:"+time+" ms");
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        this.properties=properties;
    }
}