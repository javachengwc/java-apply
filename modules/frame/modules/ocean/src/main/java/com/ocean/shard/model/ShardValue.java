package com.ocean.shard.model;

import com.google.common.collect.Range;

import java.util.Collection;
import java.util.Collections;

/**
 * 分片值
 */
public class ShardValue<T extends Comparable<?>> {

    private String columnName;

    private T value;

    private Collection<T> values;

    private  Range<T> valueRange;

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

    public Range<T> getValueRange() {
        return valueRange;
    }

    public void setValueRange(Range<T> valueRange) {
        this.valueRange = valueRange;
    }

    public ShardValue(String columnName, T value) {
        this(columnName, value, Collections.<T>emptyList());
    }

    public ShardValue(String columnName,Collection<T> values)
    {
        this.columnName=columnName;
        this.values=values;
    }

    public ShardValue(String columnName,  T value,Collection<T> values) {
        this.columnName=columnName;
        this.value=value;
        this.values=values;
    }

    public ShardValue(String columnName,Range<T> valueRange) {
        this.columnName=columnName;
        this.values=Collections.<T>emptyList();
        this.valueRange=valueRange;
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
