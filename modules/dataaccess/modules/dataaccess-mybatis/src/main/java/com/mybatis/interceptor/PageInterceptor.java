package com.mybatis.interceptor;

import com.github.pagehelper.Dialect;
import com.mybatis.pseudocode.mybatis.cache.CacheKey;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.mapping.BoundSql;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.plugin.Interceptor;
import com.mybatis.pseudocode.mybatis.plugin.Intercepts;
import com.mybatis.pseudocode.mybatis.plugin.Invocation;
import com.mybatis.pseudocode.mybatis.plugin.Signature;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import org.apache.ibatis.plugin.Plugin;
import java.util.List;
import java.util.Properties;

@SuppressWarnings({"rawtypes", "unchecked"})
@Intercepts(
    {
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    }
)
public class PageInterceptor implements Interceptor {

    private String default_dialect_class = "com.github.pagehelper.PageHelper";

    private volatile Dialect dialect;

    public Object intercept(Invocation invocation) throws Throwable {
        try {
            Object[] args = invocation.getArgs();
            MappedStatement ms = (MappedStatement) args[0];
            Object parameter = args[1];
            RowBounds rowBounds = (RowBounds) args[2];
            ResultHandler resultHandler = (ResultHandler) args[3];
            Executor executor = (Executor) invocation.getTarget();
            CacheKey cacheKey;
            BoundSql boundSql;
            if (args.length == 4) {
                boundSql = ms.getBoundSql(parameter);
                cacheKey = executor.createCacheKey(ms, parameter, rowBounds, boundSql);
            } else {
                cacheKey = (CacheKey) args[4];
                boundSql = (BoundSql) args[5];
            }
            checkDialectExists();

            List resultList=null;
            //调用方法判断是否需要进行分页，如果不需要，直接返回结果
            //boolean doPage =!dialect.skip(ms, parameter, rowBounds);
            boolean doPage =true;
            if (doPage) {
                //...
//                resultList = ExecutorUtil.pageQuery(dialect, executor,
//                        ms, parameter, rowBounds, resultHandler, boundSql, cacheKey);
            } else {
                resultList = executor.query(ms, parameter, rowBounds, resultHandler, cacheKey, boundSql);
            }
            //Object object = dialect.afterPage(resultList, parameter, rowBounds);
            Object object = null;
            return object;
        } finally {
            dialect.afterAll();
        }
    }

    private void checkDialectExists() {
        if (dialect == null) {
            synchronized (default_dialect_class) {
                if (dialect == null) {
                    setProperties(new Properties());
                }
            }
        }
    }

    @Override
    public Object plugin(Object target) {
        //return Plugin.wrap(target, this);
        return null;
    }

    @Override
    public void setProperties(Properties properties) {
        //...
    }

}
