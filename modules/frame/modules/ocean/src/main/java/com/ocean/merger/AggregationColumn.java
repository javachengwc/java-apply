package com.ocean.merger;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合列对象
 */
public class AggregationColumn {

    private String expression;

    private AggregationType aggregationType;

    private Optional<String> alias;

    private Optional<String> option;

    private final List<AggregationColumn> derivedColumns = new ArrayList<AggregationColumn>(2);

    private int index = -1;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public void setAggregationType(AggregationType aggregationType) {
        this.aggregationType = aggregationType;
    }

    public Optional<String> getAlias() {
        return alias;
    }

    public void setAlias(Optional<String> alias) {
        this.alias = alias;
    }

    public Optional<String> getOption() {
        return option;
    }

    public void setOption(Optional<String> option) {
        this.option = option;
    }

    public List<AggregationColumn> getDerivedColumns() {
        return derivedColumns;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public AggregationColumn()
    {

    }

    public AggregationColumn(String expression,AggregationType aggregationType,Optional<String> alias, Optional<String> option)
    {
        this.expression=expression;
        this.aggregationType=aggregationType;
        this.alias=alias;
        this.option=option;
    }

    public AggregationColumn(String expression,AggregationType aggregationType,Optional<String> alias, Optional<String> option,int index)
    {
        this.expression=expression;
        this.aggregationType=aggregationType;
        this.alias=alias;
        this.option=option;
        this.index=index;
    }

    /**
     * 聚合函数类型
     */
    public enum AggregationType {
        MAX, MIN, SUM, COUNT, AVG
    }
}
