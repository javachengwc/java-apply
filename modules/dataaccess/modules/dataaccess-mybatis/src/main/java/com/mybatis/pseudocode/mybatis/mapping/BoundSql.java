package com.mybatis.pseudocode.mybatis.mapping;


import com.mybatis.pseudocode.mybatis.session.Configuration;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoundSql
{
    private final String sql;
    private final List<ParameterMapping> parameterMappings;
    private final Object parameterObject;
    private final Map<String, Object> additionalParameters;
    private  MetaObject metaParameters;

    public BoundSql(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Object parameterObject)
    {
        this.sql = sql;
        this.parameterMappings = parameterMappings;
        this.parameterObject = parameterObject;
        this.additionalParameters = new HashMap();
        //this.metaParameters = configuration.newMetaObject(this.additionalParameters);
    }

    public String getSql() {
        return this.sql;
    }

    public List<ParameterMapping> getParameterMappings() {
        return this.parameterMappings;
    }

    public Object getParameterObject() {
        return this.parameterObject;
    }

    public boolean hasAdditionalParameter(String name) {
        String paramName = new PropertyTokenizer(name).getName();
        return this.additionalParameters.containsKey(paramName);
    }

    public void setAdditionalParameter(String name, Object value) {
        this.metaParameters.setValue(name, value);
    }

    public Object getAdditionalParameter(String name) {
        return this.metaParameters.getValue(name);
    }
}
