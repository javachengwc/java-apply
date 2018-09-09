package com.mybatis.pseudocode.mybatis.executor.resultset;


import com.mybatis.pseudocode.mybatis.cache.CacheKey;
import com.mybatis.pseudocode.mybatis.cursor.Cursor;
import com.mybatis.pseudocode.mybatis.cursor.defaults.DefaultCursor;
import com.mybatis.pseudocode.mybatis.executor.Executor;
import com.mybatis.pseudocode.mybatis.executor.ExecutorException;
import com.mybatis.pseudocode.mybatis.executor.parameter.ParameterHandler;
import com.mybatis.pseudocode.mybatis.executor.result.DefaultResultHandler;
import com.mybatis.pseudocode.mybatis.mapping.*;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import com.mybatis.pseudocode.mybatis.session.ResultContext;
import com.mybatis.pseudocode.mybatis.session.ResultHandler;
import com.mybatis.pseudocode.mybatis.session.RowBounds;
import com.mybatis.pseudocode.mybatis.type.TypeHandler;
import com.mybatis.pseudocode.mybatis.type.TypeHandlerRegistry;

import org.apache.ibatis.executor.result.DefaultResultContext;
import org.apache.ibatis.mapping.ResultMapping;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectorFactory;
import org.apache.ibatis.reflection.factory.ObjectFactory;

import java.lang.reflect.Constructor;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultResultSetHandler implements ResultSetHandler
{
    private static final Object DEFERED = new Object();
    private final Executor executor;
    private final Configuration configuration;
    private final MappedStatement mappedStatement;
    private final RowBounds rowBounds;
    private final ParameterHandler parameterHandler;
    private final ResultHandler<?> resultHandler;
    private final BoundSql boundSql;
    private final TypeHandlerRegistry typeHandlerRegistry;

    private final ObjectFactory objectFactory;
    private final ReflectorFactory reflectorFactory;

    private final Map<CacheKey, Object> nestedResultObjects = new HashMap();

    private final Map<String, Object> ancestorObjects = new HashMap();

    private Object previousRowValue;
    private final Map<String, ResultMapping> nextResultMaps = new HashMap();
    private final Map<CacheKey, List<PendingRelation>> pendingRelations = new HashMap();

    private final Map<String, List<UnMappedColumnAutoMapping>> autoMappingsCache = new HashMap();
    private boolean useConstructorMappings;
    private final PrimitiveTypes primitiveTypes;

    public DefaultResultSetHandler(Executor executor, MappedStatement mappedStatement, ParameterHandler parameterHandler, ResultHandler<?> resultHandler, BoundSql boundSql, RowBounds rowBounds)
    {
        this.executor = executor;
        this.configuration = mappedStatement.getConfiguration();
        this.mappedStatement = mappedStatement;
        this.rowBounds = rowBounds;
        this.parameterHandler = parameterHandler;
        this.boundSql = boundSql;
        this.typeHandlerRegistry = this.configuration.getTypeHandlerRegistry();
        this.objectFactory = this.configuration.getObjectFactory();
        this.reflectorFactory = this.configuration.getReflectorFactory();
        this.resultHandler = resultHandler;
        this.primitiveTypes = new PrimitiveTypes();
    }

    public void handleOutputParameters(CallableStatement cs) throws SQLException
    {
        Object parameterObject = this.parameterHandler.getParameterObject();
        //MetaObject metaParam = this.configuration.newMetaObject(parameterObject);
        MetaObject metaParam=null;
        List parameterMappings = this.boundSql.getParameterMappings();
        for (int i = 0; i < parameterMappings.size(); i++) {
            ParameterMapping parameterMapping = (ParameterMapping)parameterMappings.get(i);
            if ((parameterMapping.getMode() == ParameterMode.OUT) || (parameterMapping.getMode() == ParameterMode.INOUT))
                if (ResultSet.class.equals(parameterMapping.getJavaType())) {
                    //handleRefCursorOutputParameter((ResultSet)cs.getObject(i + 1), parameterMapping, metaParam);
                } else {
                    TypeHandler typeHandler = parameterMapping.getTypeHandler();
                    metaParam.setValue(parameterMapping.getProperty(), typeHandler.getResult(cs, i + 1));
                }
        }
    }

    public List<Object> handleResultSets(Statement stmt) throws SQLException
    {
        //ErrorContext.instance().activity("handling results").object(this.mappedStatement.getId());

        List multipleResults = new ArrayList();

        int resultSetCount = 0;
        ResultSetWrapper rsw = getFirstResultSet(stmt);

        List resultMaps = this.mappedStatement.getResultMaps();
        int resultMapCount = resultMaps.size();
        //validateResultMapsCount(rsw, resultMapCount);
        while ((rsw != null) && (resultMapCount > resultSetCount)) {
            ResultMap resultMap = (ResultMap)resultMaps.get(resultSetCount);
            handleResultSet(rsw, resultMap, multipleResults, null);
            rsw = getNextResultSet(stmt);
            cleanUpAfterHandlingResultSet();
            resultSetCount++;
        }

        String[] resultSets = this.mappedStatement.getResultSets();
        if (resultSets != null) {
            while ((rsw != null) && (resultSetCount < resultSets.length)) {
                ResultMapping parentMapping = (ResultMapping)this.nextResultMaps.get(resultSets[resultSetCount]);
                if (parentMapping != null) {
                    String nestedResultMapId = parentMapping.getNestedResultMapId();
                    ResultMap resultMap = this.configuration.getResultMap(nestedResultMapId);
                    handleResultSet(rsw, resultMap, null, parentMapping);
                }
                rsw = getNextResultSet(stmt);
                cleanUpAfterHandlingResultSet();
                resultSetCount++;
            }
        }

        return collapseSingleResultList(multipleResults);
    }

    public <E> Cursor<E> handleCursorResultSets(Statement stmt) throws SQLException
    {
        //ErrorContext.instance().activity("handling cursor results").object(this.mappedStatement.getId());

        ResultSetWrapper rsw = getFirstResultSet(stmt);

        List resultMaps = this.mappedStatement.getResultMaps();

        int resultMapCount = resultMaps.size();
        //validateResultMapsCount(rsw, resultMapCount);
        if (resultMapCount != 1) {
            throw new ExecutorException("Cursor results cannot be mapped to multiple resultMaps");
        }

        ResultMap resultMap = (ResultMap)resultMaps.get(0);
        return new DefaultCursor(this, resultMap, rsw, this.rowBounds);
    }

    private ResultSetWrapper getFirstResultSet(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getResultSet();
        while (rs == null)
        {
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet(); continue;
            }
            if (stmt.getUpdateCount() != -1)
            {
                continue;
            }
        }

        return rs != null ? new ResultSetWrapper(rs, this.configuration) : null;
    }

    private ResultSetWrapper getNextResultSet(Statement stmt) throws SQLException
    {
        try {
            if (stmt.getConnection().getMetaData().supportsMultipleResultSets())
            {
                if ((stmt.getMoreResults()) || (stmt.getUpdateCount() != -1)) {
                    ResultSet rs = stmt.getResultSet();
                    if (rs == null) {
                        return getNextResultSet(stmt);
                    }
                    return new ResultSetWrapper(rs, this.configuration);
                }
            }
        }
        catch (Exception localException)
        {
        }
        return null;
    }

    private void closeResultSet(ResultSet rs) {
        try {
            if (rs != null)
                rs.close();
        }
        catch (SQLException localSQLException)
        {
        }
    }

    private void cleanUpAfterHandlingResultSet() {
        this.nestedResultObjects.clear();
    }


    private void handleResultSet(ResultSetWrapper rsw, ResultMap resultMap, List<Object> multipleResults, ResultMapping parentMapping) throws SQLException
    {
        try
        {
            if (parentMapping != null) {
                //handleRowValues(rsw, resultMap, null, RowBounds.DEFAULT, parentMapping);
            }
            else if (this.resultHandler == null) {
                DefaultResultHandler defaultResultHandler = new DefaultResultHandler(this.objectFactory);
                //handleRowValues(rsw, resultMap, defaultResultHandler, this.rowBounds, null);
                multipleResults.add(defaultResultHandler.getResultList());
            } else {
                //handleRowValues(rsw, resultMap, this.resultHandler, this.rowBounds, null);
            }
        }
        finally
        {
            closeResultSet(rsw.getResultSet());
        }
    }

    public void handleRowValues(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler,
                                RowBounds rowBounds, ResultMapping parentMapping) throws SQLException
    {
        if (resultMap.hasNestedResultMaps()) {
            //ensureNoRowBounds();
            //checkResultHandler();
            //handleRowValuesForNestedResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        } else {
            handleRowValuesForSimpleResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        }
    }


    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler,
                                                   RowBounds rowBounds, ResultMapping parentMapping) throws SQLException
    {
        DefaultResultContext resultContext = new DefaultResultContext();
        //skipRows(rsw.getResultSet(), rowBounds);
//        while ((shouldProcessMoreRows(resultContext, rowBounds)) && (rsw.getResultSet().next())) {
//            ResultMap discriminatedResultMap = resolveDiscriminatedResultMap(rsw.getResultSet(), resultMap, null);
//            Object rowValue = getRowValue(rsw, discriminatedResultMap);
//            storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet());
//        }
    }


    private void storeObject(ResultHandler<?> resultHandler, ResultContext<Object> resultContext, Object rowValue, ResultMapping parentMapping, ResultSet rs) throws SQLException {
        if (parentMapping != null) {
           // linkToParents(rs, parentMapping, rowValue);
        }
        else
            callResultHandler(resultHandler, resultContext, rowValue);
    }

    private void callResultHandler(ResultHandler<?> resultHandler, ResultContext<Object> resultContext, Object rowValue)
    {
        //resultContext.nextResultObject(rowValue);
        //resultHandler.handleResult(resultContext);
    }



    private List<Object> collapseSingleResultList(List<Object> multipleResults)
    {
        return multipleResults.size() == 1 ? (List)multipleResults.get(0) : multipleResults;
    }


    private static class UnMappedColumnAutoMapping
    {
        private final String column;
        private final String property;
        private final TypeHandler<?> typeHandler;
        private final boolean primitive;

        public UnMappedColumnAutoMapping(String column, String property, TypeHandler<?> typeHandler, boolean primitive)
        {
            this.column = column;
            this.property = property;
            this.typeHandler = typeHandler;
            this.primitive = primitive;
        }
    }

    private static class PendingRelation
    {
        public MetaObject metaObject;
        public ResultMapping propertyMapping;
    }
}
