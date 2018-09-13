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

    //statment节点id
    private String id;

    private Integer fetchSize;

    private Integer timeout;

    private StatementType statementType;

    private ResultSetType resultSetType;

    private ParameterMap parameterMap;

    private List<ResultMap> resultMaps;

    //默认下，在select的statment中flushCache为false，在insert、update、delete的statment中flushCache为true
    //可在statment节点中配置，比如:<select id="a" parameterType="aa" flushCache="true" useCache="false"> </select>
    //相当于此Mapper.xml中的任意一个insert、update、delete的statment执行，都会将二级缓存刷新
    private boolean flushCacheRequired;

    //默认下，在select的statment中useCache为true，在insert、update、delete的statment中useCache为false
    //可在statment节点中配置，比如:<select id="a" parameterType="aa" flushCache="true" useCache="false"> </select>
    //但前提是所在Mapper.xml文件配置了二级缓存。
    private boolean useCache;

    private boolean resultOrdered;

    private SqlSource sqlSource;

    //二级缓存
    //在Mapper.xml文件解析时会根据文件中的cache标签创建Cache实例，并将该实例放入Mapper.xml中每一个MappedStatement中
    //比如在Mapper.xml有配置<cache eviction="FIFO" flushInterval="600000" size="4096" readOnly="false"/>,才会产生真正的二级缓存
    //二级缓存是mapper级别的缓存，多个SqlSession可去操作同一个Mapper的sql语句，多个SqlSession可以共用二级缓存，二级缓存是跨SqlSession的
    private Cache cache;

    //标记语句增删改查类型
    private SqlCommandType sqlCommandType;

    //主键生成器
    private KeyGenerator keyGenerator;

    //就是mapper.xml文件中个statment节点的keyProperty属性
    //比如:<insert id="addUser" useGeneratedKeys="true" keyProperty="id" parameterType="com.User">
    private String[] keyProperties;

    private String[] keyColumns;

    private boolean hasNestedResultMaps;

    private String databaseId;
    private LanguageDriver lang;
    private Log statementLog;
    private String[] resultSets;

    public MappedStatement() {

    }

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

    public void setFlushCacheRequired(boolean flushCacheRequired) {
        this.flushCacheRequired = flushCacheRequired;
    }

    public void setUseCache(boolean useCache) {
        this.useCache = useCache;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public void setParameterMap(ParameterMap parameterMap) {
        this.parameterMap = parameterMap;
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

    public static class Builder
    {
        private MappedStatement mappedStatement = new MappedStatement();

        public Builder(Configuration configuration, String id, SqlSource sqlSource, SqlCommandType sqlCommandType) {
            //...
        }

        public Builder resource(String resource) {
            mappedStatement.setResource(resource);
            return this;
        }

        public Builder cache(Cache cache) {
            mappedStatement.setCache( cache);
            return this;
        }

        public Builder flushCacheRequired(boolean flushCacheRequired) {
            mappedStatement.setFlushCacheRequired(flushCacheRequired);
            return this;
        }

        public Builder useCache(boolean useCache) {
            mappedStatement.setUseCache(useCache);
            return this;
        }

        public Builder parameterMap(ParameterMap parameterMap) {
            mappedStatement.setParameterMap(parameterMap);
            return this;
        }

        public MappedStatement build() {
            assert (this.mappedStatement.configuration != null);
            assert (this.mappedStatement.id != null);
            assert (this.mappedStatement.sqlSource != null);
            assert (this.mappedStatement.lang != null);
            //...
            return this.mappedStatement;
        }
    }

}