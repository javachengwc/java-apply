package com.mybatis.pseudocode.mybatis.session.defaults;

import com.mybatis.pseudocode.mybatis.binding.BindingException;
import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.executor.BatchResult;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.session.SqlSession;
import org.apache.ibatis.exceptions.ExceptionFactory;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.result.DefaultMapResultHandler;
import org.apache.ibatis.executor.result.DefaultResultContext;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

public class DefaultSqlSession implements SqlSession
{
    private final Configuration configuration;
    private final Executor executor;
    private final boolean autoCommit;

    private boolean dirty;

    private List<Cursor<?>> cursorList;

    public DefaultSqlSession(Configuration configuration, Executor executor, boolean autoCommit)
    {
        this.configuration = configuration;
        this.executor = executor;
        this.dirty = false;
        this.autoCommit = autoCommit;
    }

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this(configuration, executor, false);
    }

    public <T> T selectOne(String statement)
    {
        return selectOne(statement, null);
    }

    public <T> T selectOne(String statement, Object parameter)
    {
        List<T> list = selectList(statement, parameter);
        if (list.size() == 1)
            return list.get(0);
        if (list.size() > 1) {
            throw new TooManyResultsException("Expected one result (or null) to be returned by selectOne(), but found: " + list.size());
        }
        return null;
    }

    public <K, V> Map<K, V> selectMap(String statement, String mapKey)
    {
        return selectMap(statement, null, mapKey, RowBounds.DEFAULT);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey)
    {
        return selectMap(statement, parameter, mapKey, RowBounds.DEFAULT);
    }

    public <K, V> Map<K, V> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds)
    {
        List list = selectList(statement, parameter, rowBounds);

        DefaultMapResultHandler mapResultHandler = new DefaultMapResultHandler(mapKey, this.configuration
                .getObjectFactory(), this.configuration.getObjectWrapperFactory(), this.configuration.getReflectorFactory());
        DefaultResultContext context = new DefaultResultContext();
        for (Iterator localIterator = list.iterator(); localIterator.hasNext(); ) { Object o = localIterator.next();
            context.nextResultObject(o);
            mapResultHandler.handleResult(context);
        }
        return mapResultHandler.getMappedResults();
    }

    public <T> Cursor<T> selectCursor(String statement)
    {
        return selectCursor(statement, null);
    }

    public <T> Cursor<T> selectCursor(String statement, Object parameter)
    {
        return selectCursor(statement, parameter, RowBounds.DEFAULT);
    }

    public <T> Cursor<T> selectCursor(String statement, Object parameter, RowBounds rowBounds)
    {
        try {
            MappedStatement ms = this.configuration.getMappedStatement(statement);
            Cursor cursor = this.executor.queryCursor(ms, wrapCollection(parameter), rowBounds);
            registerCursor(cursor);
            Cursor localCursor1 = cursor;
            return localCursor1;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
        } finally {
           // ErrorContext.instance().reset();
        }
    }

    public <E> List<E> selectList(String statement)
    {
        return selectList(statement, null);
    }

    public <E> List<E> selectList(String statement, Object parameter)
    {
        return selectList(statement, parameter, RowBounds.DEFAULT);
    }

    public <E> List<E> selectList(String statement, Object parameter, RowBounds rowBounds)
    {
        try {
            MappedStatement ms = this.configuration.getMappedStatement(statement);
            List localList = this.executor.query(ms, wrapCollection(parameter), rowBounds, Executor.NO_RESULT_HANDLER);
            return localList;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }

    public void select(String statement, Object parameter, ResultHandler handler)
    {
        select(statement, parameter, RowBounds.DEFAULT, handler);
    }

    public void select(String statement, ResultHandler handler)
    {
        select(statement, null, RowBounds.DEFAULT, handler);
    }

    public void select(String statement, Object parameter, RowBounds rowBounds, ResultHandler handler)
    {
        try {
            MappedStatement ms = this.configuration.getMappedStatement(statement);
            this.executor.query(ms, wrapCollection(parameter), rowBounds, handler);
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error querying database.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }

    public int insert(String statement)
    {
        return insert(statement, null);
    }

    public int insert(String statement, Object parameter)
    {
        return update(statement, parameter);
    }

    public int update(String statement)
    {
        return update(statement, null);
    }

    public int update(String statement, Object parameter)
    {
        try {
            //执行了update操作则会将标志位dirty赋值为true
            this.dirty = true;
            MappedStatement ms = this.configuration.getMappedStatement(statement);
            int i = this.executor.update(ms, wrapCollection(parameter));
            return i;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error updating database.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }

    public int delete(String statement)
    {
        return update(statement, null);
    }

    public int delete(String statement, Object parameter)
    {
        return update(statement, parameter);
    }

    public void commit()
    {
        commit(false);
    }

    public void commit(boolean force)
    {
        try {
            this.executor.commit(isCommitOrRollbackRequired(force));
            //在事务提交时会将dirty赋值为false
            this.dirty = false;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error committing transaction.  Cause: " + e, e);
        } finally {
           //ErrorContext.instance().reset();
        }
    }

    public void rollback()
    {
        rollback(false);
    }

    public void rollback(boolean force)
    {
        try {
            this.executor.rollback(isCommitOrRollbackRequired(force));
            this.dirty = false;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error rolling back transaction.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }

    public List<BatchResult> flushStatements()
    {
        try {
            List localList = this.executor.flushStatements();
            return localList;
        } catch (Exception e) {
            throw ExceptionFactory.wrapException("Error flushing statements.  Cause: " + e, e);
        } finally {
            //ErrorContext.instance().reset();
        }
    }

    public void close()
    {
        try {
            this.executor.close(isCommitOrRollbackRequired(false));
            closeCursors();
            this.dirty = false;

        } finally
        {
            //ErrorContext.instance().reset();
        }
    }

    private void closeCursors()
    {
        if ((this.cursorList != null) && (this.cursorList.size() != 0)) {
            for (Cursor cursor : this.cursorList) {
                try {
                    cursor.close();
                } catch (IOException e) {
                    throw ExceptionFactory.wrapException("Error closing cursor.  Cause: " + e, e);
                }
            }
            this.cursorList.clear();
        }
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

    public <T> T getMapper(Class<T> type)
    {
        return this.configuration.getMapper(type, this);
    }

    public Connection getConnection()
    {
        try {
            return this.executor.getTransaction().getConnection();
        } catch (SQLException e) {
            throw ExceptionFactory.wrapException("Error getting a new connection.  Cause: " + e, e);
        }
    }

    public void clearCache()
    {
        this.executor.clearLocalCache();
    }

    private <T> void registerCursor(Cursor<T> cursor) {
        if (this.cursorList == null) {
            this.cursorList = new ArrayList();
        }
        this.cursorList.add(cursor);
    }

    private boolean isCommitOrRollbackRequired(boolean force) {
        return ((!this.autoCommit) && (this.dirty)) || (force);
    }

    private Object wrapCollection(Object object) {
        if ((object instanceof Collection)) {
            StrictMap map = new StrictMap();
            map.put("collection", object);
            if ((object instanceof List)) {
                map.put("list", object);
            }
            return map;
        }if ((object != null) && (object.getClass().isArray())) {
            StrictMap map = new StrictMap();
            map.put("array", object);
            return map;
        }
        return object;
    }

    public static class StrictMap<V> extends HashMap<String, V>
    {
        private static final long serialVersionUID = -5741767162221585340L;

        public V get(Object key) {
            if (!super.containsKey(key)) {
                throw new BindingException("Parameter '" + key + "' not found. Available parameters are " + keySet());
            }
            return super.get(key);
        }
    }
}
