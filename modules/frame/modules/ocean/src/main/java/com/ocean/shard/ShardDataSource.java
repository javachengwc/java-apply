package com.ocean.shard;

import com.ocean.shard.rule.ShardRule;
import com.ocean.exception.ShardException;
import com.ocean.jdbc.ShardConnection;
import com.ocean.jdbc.adapter.AbstractDataSourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * 分片数据源
 */
public class ShardDataSource  extends AbstractDataSourceAdapter {

    private static Logger logger = LoggerFactory.getLogger(ShardDataSource.class);

    private ShardRule shardRule;

    private Map<String,DatabaseMetaData> databaseMetaDataMap;

    public ShardDataSource(ShardRule shardRule) {

        this.shardRule = shardRule;
        this.databaseMetaDataMap =genDatabaseMetaData();
    }

    private Map<String,DatabaseMetaData> genDatabaseMetaData() {

        Map<String,DatabaseMetaData> map = new HashMap<String,DatabaseMetaData>();

        for (DataSource each : shardRule.getDataSourceRule().getDataSourceMap().values()) {
            String databaseProductName;
            DatabaseMetaData metaData;
            try {
                metaData = each.getConnection().getMetaData();
                databaseProductName = metaData.getDatabaseProductName();
            } catch (final SQLException ex) {
                throw new ShardException("Can not get data source DatabaseProductName", ex);
            }
            map.put(databaseProductName,metaData);
        }
        return map;
    }

    @Override
    public Connection getConnection() throws SQLException {

        logger.info("ShardDataSource getConnection() invoke...");
        return new ShardConnection(shardRule, databaseMetaDataMap.values().toArray(new DatabaseMetaData[ databaseMetaDataMap.size()])[0]);
    }

    @Override
    public Connection getConnection(String username, String password) throws SQLException {
        return new ShardConnection(shardRule, databaseMetaDataMap.values().toArray(new DatabaseMetaData[ databaseMetaDataMap.size()])[0]);
    }

}
