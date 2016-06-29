package com.ocean.merger.resultset;

/**
 * 结果集查询索引
 */
public class ResultSetQueryIndex {

    private static final int NO_INDEX = -1;

    private final int queryIndex;

    private final String queryName;

    public ResultSetQueryIndex(Object queryParam) {
        if (queryParam instanceof Integer) {
            queryIndex = Integer.parseInt(queryParam.toString());
            queryName = null;
        } else if (queryParam instanceof String) {
            queryIndex = NO_INDEX;
            queryName = queryParam.toString();
        } else {
            throw new IllegalArgumentException(queryParam.getClass().getName());
        }
        System.out.println("queryIndex="+queryIndex+",queryName="+queryName);
    }

    public int getQueryIndex() {
        return queryIndex;
    }

    public String getQueryName() {
        return queryName;
    }

    /**
     * 获取是否通过序号查询
     */
    public boolean isQueryBySequence() {
        return NO_INDEX != queryIndex;
    }

    /**
     * 忽略类型获取查询索引
     */
    public Object getRawQueryIndex() {
        return isQueryBySequence() ? queryIndex : queryName;
    }
}

