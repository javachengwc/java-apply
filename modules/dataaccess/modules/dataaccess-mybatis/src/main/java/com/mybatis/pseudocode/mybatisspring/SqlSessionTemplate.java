package com.mybatis.pseudocode.mybatisspring;

import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.exceptions.PersistenceException;
import com.mybatis.pseudocode.mybatis.executor.BatchResult;
import com.mybatis.pseudocode.mybatis.session.*;
import org.mybatis.spring.MyBatisExceptionTranslator;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.dao.support.PersistenceExceptionTranslator;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import static java.lang.reflect.Proxy.newProxyInstance;

//<bean id="sqlSessionTemplate" class="org.mybatis.spring.SqlSessionTemplate">
//<constructor-arg index="0" ref="sqlSessionFactory"/>
//<constructor-arg index="1" value="SIMPLE"/>
//</bean>
public class SqlSessionTemplate implements SqlSession, DisposableBean
{

    private final SqlSessionFactory sqlSessionFactory;

    //执行器类型
    private final ExecutorType executorType;

    private final SqlSession sqlSessionProxy;

    private final PersistenceExceptionTranslator exceptionTranslator;

    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory)
    {
        this(sqlSessionFactory, sqlSessionFactory.getConfiguration().getDefaultExecutorType());
    }

    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType)
    {
        this(sqlSessionFactory, executorType,
                new MyBatisExceptionTranslator(sqlSessionFactory.getConfiguration().getEnvironment().getDataSource(), true));
    }

    public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory, ExecutorType executorType, PersistenceExceptionTranslator exceptionTranslator)
    {
        this.sqlSessionFactory = sqlSessionFactory;
        this.executorType = executorType;
        this.exceptionTranslator = exceptionTranslator;
        this.sqlSessionProxy = (SqlSession) newProxyInstance(SqlSessionFactory.class.getClassLoader(), new Class[] { SqlSession.class },
                new SqlSessionTemplate.SqlSessionInterceptor());
    }

    public SqlSessionFactory getSqlSessionFactory()
    {
        return this.sqlSessionFactory;
    }

    public ExecutorType getExecutorType() {
        return this.executorType;
    }

    public PersistenceExceptionTranslator getPersistenceExceptionTranslator() {
        return this.exceptionTranslator;
    }

    public <T> T selectOne(String statement)
    {
        return this.sqlSessionProxy.selectOne(statement);
    }

    public <T> T selectOne(String statement, Object parameter)
    {
        return this.sqlSessionProxy.selectOne(statement, parameter);
    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey)
    {
        return this.sqlSessionProxy.selectMap(statement, mapKey);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey)
    {
        return this.sqlSessionProxy.selectMap(statement, parameter, mapKey);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds)
    {
        return this.sqlSessionProxy.selectMap(statement, parameter, mapKey, rowBounds);
    }

    public <T> Cursor<T> selectCursor(String statement)
    {
        return this.sqlSessionProxy.selectCursor(statement);
    }

    public <T> Cursor<T> selectCursor(String statement, Object parameter)
    {
        return this.sqlSessionProxy.selectCursor(statement, parameter);
    }

    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds)
    {
        return this.sqlSessionProxy.selectCursor(statement, parameter, rowBounds);
    }

    public <E> List<E> selectList(String statement)
    {
        return this.sqlSessionProxy.selectList(statement);
    }

    public <E> List<E> selectList(String statement, Object parameter)
    {
        return this.sqlSessionProxy.selectList(statement, parameter);
    }

    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds)
    {
        return this.sqlSessionProxy.selectList(statement, parameter, rowBounds);
    }

    public void select(String statement, ResultHandler handler)
    {
        this.sqlSessionProxy.select(statement, handler);
    }

    public void select(String statement, Object parameter, ResultHandler handler)
    {
        this.sqlSessionProxy.select(statement, parameter, handler);
    }

    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler)
    {
        this.sqlSessionProxy.select(statement, parameter, rowBounds, handler);
    }

    public int insert(String statement)
    {
        return this.sqlSessionProxy.insert(statement);
    }

    public int insert(String statement, Object parameter)
    {
        return this.sqlSessionProxy.insert(statement, parameter);
    }

    public int update(String statement)
    {
        return this.sqlSessionProxy.update(statement);
    }

    public int update(String statement, Object parameter)
    {
        return this.sqlSessionProxy.update(statement, parameter);
    }

    public int delete(String statement)
    {
        return this.sqlSessionProxy.delete(statement);
    }

    public int delete(String statement, Object parameter)
    {
        return this.sqlSessionProxy.delete(statement, parameter);
    }

    public <T> T getMapper(Class<T> type)
    {
        return getConfiguration().getMapper(type, this);
    }

    public void commit()
    {
        throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
    }

    public void commit(boolean force)
    {
        throw new UnsupportedOperationException("Manual commit is not allowed over a Spring managed SqlSession");
    }

    public void rollback()
    {
        throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
    }

    public void rollback(boolean force)
    {
        throw new UnsupportedOperationException("Manual rollback is not allowed over a Spring managed SqlSession");
    }

    public void close()
    {
        throw new UnsupportedOperationException("Manual close is not allowed over a Spring managed SqlSession");
    }

    public void clearCache()
    {
        this.sqlSessionProxy.clearCache();
    }

    public Configuration getConfiguration()
    {
        return this.sqlSessionFactory.getConfiguration();
    }

    public Connection getConnection()
    {
        return this.sqlSessionProxy.getConnection();
    }

    public List<BatchResult> flushStatements()
    {
        return this.sqlSessionProxy.flushStatements();
    }

    public void destroy()
            throws Exception
    {
    }

    private class SqlSessionInterceptor implements InvocationHandler
    {
        private SqlSessionInterceptor()
        {

        }
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            SqlSession sqlSession = SqlSessionUtils.getSqlSession(SqlSessionTemplate.this.sqlSessionFactory,
                    SqlSessionTemplate.this.executorType,
                    SqlSessionTemplate.this.exceptionTranslator);
            try
            {
                Object result = method.invoke(sqlSession, args);
                if (!SqlSessionUtils.isSqlSessionTransactional(sqlSession, SqlSessionTemplate.this.sqlSessionFactory))
                {
                    sqlSession.commit(true);
                }
                Object localObject1 = result;
                return localObject1;
            } catch (Throwable t) {
                //Throwable unwrapped = ExceptionUtil.unwrapThrowable(t);
                Throwable unwrapped=null;
                if ((SqlSessionTemplate.this.exceptionTranslator != null) && ((unwrapped instanceof PersistenceException)))
                {
                    SqlSessionUtils.closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
                    sqlSession = null;
                    Throwable translated = SqlSessionTemplate.this.exceptionTranslator.translateExceptionIfPossible((PersistenceException)unwrapped);
                    if (translated != null) {
                        unwrapped = translated;
                    }
                }
                throw unwrapped;
            } finally {
                if (sqlSession != null) {
                    SqlSessionUtils.closeSqlSession(sqlSession, SqlSessionTemplate.this.sqlSessionFactory);
                }
            }
        }
    }
}