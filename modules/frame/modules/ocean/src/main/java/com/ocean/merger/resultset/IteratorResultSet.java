package com.ocean.merger.resultset;

import com.ocean.jdbc.ShardResultSet;
import com.ocean.merger.MergeContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 迭代结果集
 */
public class IteratorResultSet  extends ShardResultSet {

    public IteratorResultSet(List<ResultSet> resultSets, MergeContext mergeContext) {
        super(resultSets, mergeContext.getLimit());
    }

    protected boolean nextForShard() throws SQLException {
        if (getCurrentResultSet().next()) {
            return true;
        }
        for (int i = getResultSets().indexOf(getCurrentResultSet()) + 1; i < getResultSets().size(); i++) {
            ResultSet each = getResultSets().get(i);
            if (each.next()) {
                setCurrentResultSet(each);
                return true;
            }
        }
        return false;
    }
}
