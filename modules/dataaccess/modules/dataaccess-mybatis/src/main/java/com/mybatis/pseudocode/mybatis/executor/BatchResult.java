package com.mybatis.pseudocode.mybatis.executor;


import com.mybatis.pseudocode.mybatis.mapping.MappedStatement;

import java.util.ArrayList;
import java.util.List;

public class BatchResult
{
    private final MappedStatement mappedStatement;
    private final String sql;
    private final List<Object> parameterObjects;
    private int[] updateCounts;

    public BatchResult(MappedStatement mappedStatement, String sql)
    {
        this.mappedStatement = mappedStatement;
        this.sql = sql;
        this.parameterObjects = new ArrayList();
    }

    public BatchResult(MappedStatement mappedStatement, String sql, Object parameterObject) {
        this(mappedStatement, sql);
        addParameterObject(parameterObject);
    }

    public MappedStatement getMappedStatement() {
        return this.mappedStatement;
    }

    public String getSql() {
        return this.sql;
    }

    @Deprecated
    public Object getParameterObject() {
        return this.parameterObjects.get(0);
    }

    public List<Object> getParameterObjects() {
        return this.parameterObjects;
    }

    public int[] getUpdateCounts() {
        return this.updateCounts;
    }

    public void setUpdateCounts(int[] updateCounts) {
        this.updateCounts = updateCounts;
    }

    public void addParameterObject(Object parameterObject) {
        this.parameterObjects.add(parameterObject);
    }
}
