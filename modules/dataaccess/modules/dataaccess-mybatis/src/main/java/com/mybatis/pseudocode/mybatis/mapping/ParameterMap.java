package com.mybatis.pseudocode.mybatis.mapping;

import com.mybatis.pseudocode.mybatis.session.Configuration;

import java.util.List;

public class ParameterMap
{
    private String id;
    private Class<?> type;
    private List<ParameterMapping> parameterMappings;

    public String getId()
    {
        return this.id;
    }

    public Class<?> getType() {
        return this.type;
    }

    public List<ParameterMapping> getParameterMappings() {
        return this.parameterMappings;
    }

    public static class Builder
    {
        private ParameterMap parameterMap = new ParameterMap();

        public Builder(Configuration configuration, String id, Class<?> type, List<ParameterMapping> parameterMappings) {
            //...
        }

        public Class<?> type() {
            return this.parameterMap.type;
        }

        public ParameterMap build()
        {
            //...
            return this.parameterMap;
        }
    }
}
