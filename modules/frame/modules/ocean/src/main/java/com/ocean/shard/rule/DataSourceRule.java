package com.ocean.shard.rule;

import com.google.common.base.Preconditions;

import javax.sql.DataSource;
import java.util.Collection;
import java.util.Map;

/**
 * 数据源配置对象
 */
public class DataSourceRule {

    private Map<String, DataSource> dataSourceMap;

    public DataSourceRule()
    {

    }

    public DataSourceRule(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    public Map<String, DataSource> getDataSourceMap() {
        return dataSourceMap;
    }

    public void setDataSourceMap(Map<String, DataSource> dataSourceMap) {
        this.dataSourceMap = dataSourceMap;
    }

    /**
     * 获取数据源实例
     */
    public DataSource getDataSource(final String name) {
        return dataSourceMap.get(name);
    }

    /**
     * 获取所有数据源名称
     */
    public Collection<String> getDataSourceNames() {
        return dataSourceMap.keySet();
    }

    /**
     * 获取所有数据源
     */
    public Collection<DataSource> getDataSources() {
        return dataSourceMap.values();
    }
}

