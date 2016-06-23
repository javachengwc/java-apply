package com.ocean.router;

import com.ocean.merger.MergeContext;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * sql路由结果
 */
public class SqlRouteResult {

    private MergeContext mergeContext;

    private List<SqlExecutionUnit> executionUnits = new ArrayList<SqlExecutionUnit>();

    public SqlRouteResult()
    {

    }

    public SqlRouteResult(MergeContext mergeContext)
    {
        this.mergeContext = mergeContext;
    }

    public SqlRouteResult(MergeContext mergeContext, List<SqlExecutionUnit> executionUnits )
    {
        this.mergeContext = mergeContext;
        this.executionUnits = executionUnits;
    }

    public MergeContext getMergeContext() {
        return mergeContext;
    }

    public void setMergeContext(MergeContext mergeContext) {
        this.mergeContext = mergeContext;
    }

    public List<SqlExecutionUnit> getExecutionUnits() {
        return executionUnits;
    }

    public void setExecutionUnits(List<SqlExecutionUnit> executionUnits) {
        this.executionUnits = executionUnits;
    }

    public String toString()
    {
        return ToStringBuilder.reflectionToString(this);
    }
}
