package com.mybatis.pseudocode.mybatisspring.transaction;


import com.mybatis.pseudocode.mybatis.transaction.Transaction;
import org.apache.ibatis.logging.Log;
import org.apache.ibatis.logging.LogFactory;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//spring中对mybatis事务的管理
public class SpringManagedTransaction implements Transaction
{
    private static final Log LOGGER = LogFactory.getLog(SpringManagedTransaction.class);
    private final DataSource dataSource;
    private Connection connection;
    private boolean isConnectionTransactional;
    private boolean autoCommit;

    public SpringManagedTransaction(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException
    {
        if (this.connection == null) {
            openConnection();
        }
        return this.connection;
    }

    private void openConnection() throws SQLException
    {
        this.connection = DataSourceUtils.getConnection(this.dataSource);
        this.autoCommit = this.connection.getAutoCommit();
        this.isConnectionTransactional = DataSourceUtils.isConnectionTransactional(this.connection, this.dataSource);

        if (LOGGER.isDebugEnabled())
            LOGGER.debug(new StringBuilder().append("JDBC Connection [").append(this.connection).append("] will").append(this.isConnectionTransactional ? " " : " not ").append("be managed by Spring").toString());
    }

    public void commit() throws SQLException
    {
        if ((this.connection != null) && (!this.isConnectionTransactional) && (!this.autoCommit)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(new StringBuilder().append("Committing JDBC Connection [").append(this.connection).append("]").toString());
            }
            this.connection.commit();
        }
    }

    public void rollback() throws SQLException
    {
        if ((this.connection != null) && (!this.isConnectionTransactional) && (!this.autoCommit)) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(new StringBuilder().append("Rolling back JDBC Connection [").append(this.connection).append("]").toString());
            }
            this.connection.rollback();
        }
    }

    public void close() throws SQLException
    {
        DataSourceUtils.releaseConnection(this.connection, this.dataSource);
    }

    public Integer getTimeout() throws SQLException
    {
        ConnectionHolder holder = (ConnectionHolder) TransactionSynchronizationManager.getResource(this.dataSource);
        if ((holder != null) && (holder.hasTimeout())) {
            return Integer.valueOf(holder.getTimeToLiveInSeconds());
        }
        return null;
    }
}
