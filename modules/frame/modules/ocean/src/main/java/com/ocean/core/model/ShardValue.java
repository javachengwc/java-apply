package com.ocean.core.model;

import java.util.Collection;
import java.util.Collections;

/**
 * 分片值
 */
public class ShardValue<T extends Comparable<?>> {

    private String columnName;

    private T value;

    private Collection<T> values;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Collection<T> getValues() {
        return values;
    }

    public void setValues(Collection<T> values) {
        this.values = values;
    }

    public ShardValue(String columnName, T value) {
        this(columnName, value, Collections.<T>emptyList());
    }

    public ShardValue(String columnName,  T value,Collection<T> values) {
        this.columnName=columnName;
        this.value=value;
        this.values=values;
    }

    /**
     * 获取分片值类型
     */
    public ShardValueType getType() {
        if (null != value) {
            return ShardValueType.SINGLE;
        }
        if (!values.isEmpty()) {
            return ShardValueType.LIST;
        }
        return ShardValueType.RANGE;
    }

    /**
     * 分片值类型
     */
    public enum ShardValueType {
        SINGLE, LIST, RANGE
    }
}
