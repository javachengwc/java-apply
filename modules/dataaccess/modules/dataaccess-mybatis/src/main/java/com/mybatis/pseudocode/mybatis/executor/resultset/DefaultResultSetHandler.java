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

import org.apache.ibatis.executor.loader.ResultLoaderMap;
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
        //ResultSetWrapper包装ResultSet
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

    //获取第一个ResultSet，同时获取数据库的MetaData数据，包括数据表列名、列的类型、类序号等
    //这些信息都存储在ResultSetWrapper类中
    private ResultSetWrapper getFirstResultSet(Statement stmt) throws SQLException {
        ResultSet rs = stmt.getResultSet();
        while (rs == null)
        {
            if (stmt.getMoreResults()) {
                rs = stmt.getResultSet();
                continue;
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
                //调用handleRowValues方法来进行结果值的设置
                handleRowValues(rsw, resultMap, null, RowBounds.DEFAULT, parentMapping);
            }
            else if (this.resultHandler == null) {
                DefaultResultHandler defaultResultHandler = new DefaultResultHandler(this.objectFactory);
                handleRowValues(rsw, resultMap, defaultResultHandler, this.rowBounds, null);
                multipleResults.add(defaultResultHandler.getResultList());
            } else {
                handleRowValues(rsw, resultMap, this.resultHandler, this.rowBounds, null);
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
            //封装数据
            handleRowValuesForSimpleResultMap(rsw, resultMap, resultHandler, rowBounds, parentMapping);
        }
    }

    //封装数据
    private void handleRowValuesForSimpleResultMap(ResultSetWrapper rsw, ResultMap resultMap, ResultHandler<?> resultHandler,
                                                   RowBounds rowBounds, ResultMapping parentMapping) throws SQLException
    {
//        ResultContext resultContext = new DefaultResultContext();
        ResultContext resultContext =null;
        //skipRows(rsw.getResultSet(), rowBounds);
//        while ((shouldProcessMoreRows(resultContext, rowBounds)) && (rsw.getResultSet().next())) {
//            ResultMap discriminatedResultMap = resolveDiscriminatedResultMap(rsw.getResultSet(), resultMap, null);
              ResultMap discriminatedResultMap=null;
              Object rowValue = getRowValue(rsw, discriminatedResultMap);
              storeObject(resultHandler, resultContext, rowValue, parentMapping, rsw.getResultSet());
//        }
    }

    private Object getRowValue(ResultSetWrapper rsw, ResultMap resultMap) throws SQLException {
        final ResultLoaderMap lazyLoader = new ResultLoaderMap();
        // createResultObject为新创建的对象，数据表对应的类
        //Object resultObject = createResultObject(rsw, resultMap, lazyLoader, null);
        Object resultObject =null;
        //if (resultObject != null && !hasTypeHandlerForResultObject(rsw, resultMap.getType())) {
        if(true) {
            //final MetaObject metaObject = configuration.newMetaObject(resultObject);
            MetaObject metaObject =null;
            boolean foundValues = !resultMap.getConstructorResultMappings().isEmpty();
            //if (shouldApplyAutomaticMappings(resultMap, false)) {
            if(true) {
                // 把数据填充进去，metaObject中包含了resultObject信息
                foundValues = applyAutomaticMappings(rsw, resultMap, metaObject, null) || foundValues;
            }
            //foundValues = applyPropertyMappings(rsw, resultMap, metaObject, lazyLoader, null) || foundValues;
            foundValues = lazyLoader.size() > 0 || foundValues;
            resultObject = foundValues ? resultObject : null;
            return resultObject;
        }
        return resultObject;
    }

    private boolean applyAutomaticMappings(ResultSetWrapper rsw, ResultMap resultMap, MetaObject metaObject, String columnPrefix) throws SQLException {
        //List<UnMappedColumnAutoMapping> autoMapping = createAutomaticMappings(rsw, resultMap, metaObject, columnPrefix);
        List<UnMappedColumnAutoMapping> autoMapping =null;
        boolean foundValues = false;
        if (autoMapping.size() > 0) {
            // 这里进行for循环调用，因为resultMap中列有几项，就调用几次
            for (UnMappedColumnAutoMapping mapping : autoMapping) {
                // 将resultSet中查询结果转换为对应的实际类型
                final Object value = mapping.typeHandler.getResult(rsw.getResultSet(), mapping.column);
                if (value != null) {
                    foundValues = true;
                }
                if (value != null || (configuration.isCallSettersOnNulls() && !mapping.primitive)) {
                    //metaValue.setValue方法会调用到Java类中对应数据域的set方法，这样就完成了SQL查询结果集的Java类封装过程
                    metaObject.setValue(mapping.property, value);
                }
            }
        }
        return foundValues;
    }

    private void storeObject(ResultHandler<?> resultHandler, ResultContext<Object> resultContext,
                             Object rowValue, ResultMapping parentMapping, ResultSet rs) throws SQLException {
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
