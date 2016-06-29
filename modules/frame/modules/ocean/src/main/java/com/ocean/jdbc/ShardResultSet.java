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

    protected static Logger logger = LoggerFactory.getLogger(ShardResultSet.class);

    private Limit limit;

    //是否对结果集移位到limit的start位置
    private boolean offsetSkipped=false;

    //迭代读取的数据数量
    private int readCount=0;

    protected ShardResultSet(List<ResultSet> resultSets,Limit limit ) {
        this.resultSets=resultSets;
        this.limit = limit;
        setCurrentResultSet(resultSets.get(0));
    }

    @Override
    public boolean next() throws SQLException {
        logger.info("ShardResultSet next.");
        if (!offsetSkipped && limit !=null) {
            //sql语句有limit,在结果第一次迭代中移位到limit的start位置
            skipOffset();
        }
        if(limit==null)
        {
            return nextForShard();
        }
        //此处就是截取limit的数据片段
        readCount++;
        if( readCount>limit.getRowCount())
        {
            //读取的数据数量超过limit中的rowCount数量,直接返回false;
            return false;
        }
        return nextForShard();
    }

    private void skipOffset() {
        //对结果集移位到limit的start位置
        for (int i = 0; i <limit.getOffset(); i++) {
            try {
                if (!nextForShard()) {
                    break;
                }
            } catch (SQLException e) {
                logger.warn("ShardResultSet skipOffset error", e);
            }
        }
        //标记已移位
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
