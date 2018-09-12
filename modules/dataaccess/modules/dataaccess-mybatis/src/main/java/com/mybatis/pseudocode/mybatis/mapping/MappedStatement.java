package com.mybatis.pseudocode.mybatis.mapping;


import com.mybatis.pseudocode.mybatis.cache.Cache;
import com.mybatis.pseudocode.mybatis.executor.kengen.KeyGenerator;
import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.scripting.LanguageDriver;

import java.util.List;

//MappedStatement维护了一条<select|update|delete|insert>节点的封装
public final class MappedStatement
{
    private String resource;
    private Configuration configuration;
    private String id;
    private Integer fetchSize;
    private Integer timeout;
    private StatementType statementType;
    private ResultSetType resultSetType;
    private ParameterMap parameterMap;
    private List<ResultMap> resultMaps;
    private boolean flushCacheRequired;
    private boolean useCache;
    private boolean resultOrdered;
    private SqlSource sqlSource;

    //二级缓存
    //在Mapper.xml文件解析时会根据文件中的cache标签创建Cache实例，并将该实例放入每一个MappedStatement中
    //在MappedStatement执行select操作时候会获取该cache;
    private Cache cache;

    private SqlCommandType sqlCommandType;

    private KeyGenerator keyGenerator;

    private String[] keyProperties;
    private String[] keyColumns;
    private boolean hasNestedResultMaps;
    private String databaseId;
    private LanguageDriver lang;
    private Log statementLog;
    private String[] resultSets;

    public KeyGenerator getKeyGenerator()
    {
        return this.keyGenerator;
    }

    public SqlCommandType getSqlCommandType() {
        return this.sqlCommandType;
    }

    public String getResource() {
        return this.resource;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    public String getId() {
        return this.id;
    }

    public boolean hasNestedResultMaps() {
        return this.hasNestedResultMaps;
    }

    public Integer getFetchSize() {
        return this.fetchSize;
    }

    public Integer getTimeout() {
        return this.timeout;
    }

    public StatementType getStatementType() {
        return this.statementType;
    }

    public ResultSetType getResultSetType() {
        return this.resultSetType;
    }


    public ParameterMap getParameterMap() {
        return this.parameterMap;
    }

    public List<ResultMap> getResultMaps() {
        return this.resultMaps;
    }

    public boolean isFlushCacheRequired() {
        return this.flushCacheRequired;
    }

    public boolean isUseCache() {
        return this.useCache;
    }

    public boolean isResultOrdered() {
        return this.resultOrdered;
    }

    public String getDatabaseId() {
        return this.databaseId;
    }

    public String[] getKeyProperties() {
        return this.keyProperties;
    }

    public String[] getKeyColumns() {
        return this.keyColumns;
    }


    public LanguageDriver getLang() {
        return this.lang;
    }

    public String[] getResultSets() {
        return this.resultSets;
    }

    public Log getStatementLog() {
        return statementLog;
    }

    public void setStatementLog(Log statementLog) {
        this.statementLog = statementLog;
    }

    public Cache getCache() {
        return cache;
    }

    public void setCache(Cache cache) {
        this.cache = cache;
    }

    @Deprecated
    public String[] getResulSets() {
        return this.resultSets;
    }


    public BoundSql getBoundSql(Object parameterObject) {
        BoundSql boundSql = this.sqlSource.getBoundSql(parameterObject);
        List parameterMappings = boundSql.getParameterMappings();
        if ((parameterMappings == null) || (parameterMappings.isEmpty())) {
            boundSql = new BoundSql(this.configuration, boundSql.getSql(), this.parameterMap.getParameterMappings(), parameterObject);
        }

        for (ParameterMapping pm : boundSql.getParameterMappings()) {
            String rmId = pm.getResultMapId();
            if (rmId != null) {
                ResultMap rm = this.configuration.getResultMap(rmId);
                if (rm != null) {
                    this.hasNestedResultMaps |= rm.hasNestedResultMaps();
                }
            }
        }

        return boundSql;
    }


}