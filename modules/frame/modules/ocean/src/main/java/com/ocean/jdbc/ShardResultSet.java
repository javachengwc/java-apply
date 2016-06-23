package com.ocean.jdbc;

import com.ocean.jdbc.adapter.AbstractResultSetAdapter;
import com.ocean.parser.Limit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * 支持分片的结果集类
 */
public class ShardResultSet  extends AbstractResultSetAdapter {

    private Logger logger = LoggerFactory.getLogger(ShardResultSet.class);

    private Limit limit;

    private boolean offsetSkipped;

    private int readCount;

    protected ShardResultSet(List<ResultSet> resultSets,Limit limit ) {
        this.resultSets=resultSets;
        this.limit = limit;
        setCurrentResultSet(resultSets.get(0));
    }

    @Override
    public boolean next() throws SQLException {
        if (!offsetSkipped) {
            skipOffset();
        }
        return ++readCount <= limit.getRowCount() && nextForShard();
    }

    private void skipOffset() {
        for (int i = 0; i <limit.getOffset(); i++) {
            try {
                if (!nextForShard()) {
                    break;
                }
            } catch (SQLException ignored) {
                logger.warn("skipOffset error", ignored);
            }
        }
        offsetSkipped = true;
    }

    /**
     * 迭代结果集
     */
    protected  boolean nextForShard() throws SQLException
    {
        return false;
    }
}
