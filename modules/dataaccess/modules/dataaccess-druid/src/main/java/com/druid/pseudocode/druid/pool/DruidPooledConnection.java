/*
 * Copyright 1999-2011 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.druid.pseudocode.druid.pool;


import java.sql.Array;
import java.sql.Blob;
import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.NClob;
import java.sql.PreparedStatement;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Savepoint;
import java.sql.Statement;
import java.sql.Struct;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

import javax.sql.ConnectionEvent;
import javax.sql.ConnectionEventListener;
import javax.sql.StatementEventListener;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.FilterChainImpl;
import com.alibaba.druid.pool.*;
import com.alibaba.druid.pool.DruidConnectionHolder;
import com.alibaba.druid.pool.DruidPooledPreparedStatement.PreparedStatementKey;
import com.alibaba.druid.pool.PreparedStatementPool.MethodType;
import com.alibaba.druid.proxy.jdbc.TransactionInfo;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;

public class DruidPooledConnection extends PoolableWrapper implements javax.sql.PooledConnection, Connection {

    private final static Log                 LOG                  = LogFactory.getLog(DruidPooledConnection.class);

    public static final int                  MAX_RECORD_SQL_COUNT = 10;

    protected Connection                     conn;
    protected volatile com.alibaba.druid.pool.DruidConnectionHolder holder;
    protected TransactionInfo                transactionInfo;
    private final boolean                    dupCloseLogEnable;
    private volatile boolean                 traceEnable          = false;
    private boolean                          disable              = false;
    private boolean                          closed               = false;
    private final Thread                     ownerThread;

    private long                             connectedTimeNano;
    private volatile boolean                 running              = false;

    private volatile boolean                 abandoned            = false;

    private StackTraceElement[]              connectStackTrace;

    private Throwable                        disableError         = null;

    public DruidPooledConnection(com.alibaba.druid.pool.DruidConnectionHolder holder){
        super(holder.getConnection());

        this.conn = holder.getConnection();
        this.holder = holder;
        dupCloseLogEnable = holder.getDataSource().isDupCloseLogEnable();
        ownerThread = Thread.currentThread();
    }

    public Thread getOwnerThread() {
        return ownerThread;
    }

    public StackTraceElement[] getConnectStackTrace() {
        return connectStackTrace;
    }

    public void setConnectStackTrace(StackTraceElement[] connectStackTrace) {
        this.connectStackTrace = connectStackTrace;
    }

    public long getConnectedTimeNano() {
        return connectedTimeNano;
    }

    public void setConnectedTimeNano() {
        if (connectedTimeNano <= 0) {
            this.setConnectedTimeNano(System.nanoTime());
        }
    }

    public void setConnectedTimeNano(long connectedTimeNano) {
        this.connectedTimeNano = connectedTimeNano;
    }

    public boolean isTraceEnable() {
        return traceEnable;
    }

    public void setTraceEnable(boolean traceEnable) {
        this.traceEnable = traceEnable;
    }

    public SQLException handleException(Throwable t) throws SQLException {
        final com.alibaba.druid.pool.DruidConnectionHolder holder = this.holder;

        //
        if (holder != null) {
            DruidAbstractDataSource dataSource = holder.getDataSource();
            //dataSource.handleConnectionException(this, t);
        }

        if (t instanceof SQLException) {
            throw (SQLException) t;
        }

        throw new SQLException("Error", t);
    }

    public boolean isOracle() {
        return holder.getDataSource().isOracle();
    }

    public void closePoolableStatement(DruidPooledPreparedStatement stmt) throws SQLException {
        PreparedStatement rawStatement = stmt.getRawPreparedStatement();

        if (holder == null) {
            return;
        }

        if (stmt.isPooled()) {
            try {
                rawStatement.clearParameters();
            } catch (SQLException ex) {
                this.handleException(ex);
                if (rawStatement.getConnection().isClosed()) {
                    return;
                }

                LOG.error("clear parameter error", ex);
            }
        }

        stmt.getPreparedStatementHolder().decrementInUseCount();
        if (stmt.isPooled() && holder.isPoolPreparedStatements()) {
            holder.getStatementPool().put(stmt.getPreparedStatementHolder());

            //stmt.clearResultSet();
            holder.removeTrace(stmt);

            stmt.getPreparedStatementHolder().setFetchRowPeak(stmt.getFetchRowPeak());

            //stmt.setClosed(true); // soft set close
        } else {
            //stmt.closeInternal();
            holder.getDataSource().incrementClosedPreparedStatementCount();
        }
    }

    public com.alibaba.druid.pool.DruidConnectionHolder getConnectionHolder() {
        return holder;
    }

    @Override
    public Connection getConnection() {
        return conn;
    }

    public void disable() {
        disable(null);
    }

    public void disable(Throwable error) {
        if (this.holder != null) {
            this.holder.clearStatementCache();
        }

        this.traceEnable = false;
        this.holder = null;
        this.transactionInfo = null;
        this.disable = true;
        this.disableError = error;
    }

    public boolean isDisable() {
        return disable;
    }

    @Override
    public void close() throws SQLException {
        if (this.disable) {
            return;
        }

        com.alibaba.druid.pool.DruidConnectionHolder holder = this.holder;
        if (holder == null) {
            if (dupCloseLogEnable) {
                LOG.error("dup close");
            }
            return;
        }

        DruidAbstractDataSource dataSource = holder.getDataSource();
        boolean isSameThread = this.getOwnerThread() == Thread.currentThread();

        if (!isSameThread) {
            dataSource.setAsyncCloseConnectionEnable(true);
        }

        if (dataSource.isAsyncCloseConnectionEnable()) {
            syncClose();
            return;
        }

        for (ConnectionEventListener listener : holder.getConnectionEventListeners()) {
            listener.connectionClosed(new ConnectionEvent(this));
        }


        List<Filter> filters = dataSource.getProxyFilters();
        if (filters.size() > 0) {
            FilterChainImpl filterChain = new FilterChainImpl(dataSource);
            //filterChain.dataSource_recycle(this);
        } else {
            recycle();
        }

        this.disable = true;
    }

    public synchronized void syncClose() throws SQLException {
        if (this.disable) {
            return;
        }

        com.alibaba.druid.pool.DruidConnectionHolder holder = this.holder;
        if (holder == null) {
            if (dupCloseLogEnable) {
                LOG.error("dup close");
            }
            return;
        }

        for (ConnectionEventListener listener : holder.getConnectionEventListeners()) {
            listener.connectionClosed(new ConnectionEvent(this));
        }

        DruidAbstractDataSource dataSource = holder.getDataSource();
        List<Filter> filters = dataSource.getProxyFilters();
        if (filters.size() > 0) {
            FilterChainImpl filterChain = new FilterChainImpl(dataSource);
            //filterChain.dataSource_recycle(this);
        } else {
            recycle();
        }

        this.disable = true;
    }

    public void recycle() throws SQLException {
        if (this.disable) {
            return;
        }

        com.alibaba.druid.pool.DruidConnectionHolder holder = this.holder;
        if (holder == null) {
            if (dupCloseLogEnable) {
                LOG.error("dup close");
            }
            return;
        }

        if (!this.abandoned) {
            DruidAbstractDataSource dataSource = holder.getDataSource();
            //dataSource.recycle(this);
        }

        this.holder = null;
        conn = null;
        transactionInfo = null;
        closed = true;
    }

    // ////////////////////

    @Override
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.M1);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);

        DruidPooledPreparedStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    private void initStatement(PreparedStatementHolder stmtHolder) throws SQLException {
        stmtHolder.incrementInUseCount();
        //holder.getDataSource().initStatement(this, stmtHolder.getStatement());
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency)
            throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.M2, resultSetType,
                resultSetConcurrency);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql, resultSetType,
                        resultSetConcurrency));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);

        DruidPooledPreparedStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int resultSetType, int resultSetConcurrency,
                                              int resultSetHoldability) throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.M3, resultSetType,
                resultSetConcurrency, resultSetHoldability);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql, resultSetType,
                        resultSetConcurrency,
                        resultSetHoldability));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);

        DruidPooledPreparedStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int[] columnIndexes) throws SQLException {
        checkState();

        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.M4, columnIndexes);
        PreparedStatementHolder stmtHolder = null;

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql, columnIndexes));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);

        DruidPooledPreparedStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, String[] columnNames) throws SQLException {
        checkState();

        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.M5, columnNames);
        PreparedStatementHolder stmtHolder = null;

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql, columnNames));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);
        DruidPooledPreparedStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    @Override
    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys) throws SQLException {
        checkState();

        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.M6, autoGeneratedKeys);
        PreparedStatementHolder stmtHolder = null;

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareStatement(sql, autoGeneratedKeys));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledPreparedStatement rtnVal = new DruidPooledPreparedStatement(this, stmtHolder);

        DruidPooledPreparedStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    // ////////////////////

    @Override
    public CallableStatement prepareCall(String sql) throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.Precall_1);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareCall(sql));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledCallableStatement rtnVal = new DruidPooledCallableStatement(this, stmtHolder);
        DruidPooledCallableStatement rtnVal = null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency,
                                         int resultSetHoldability) throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.Precall_2, resultSetType,
                resultSetConcurrency, resultSetHoldability);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key, conn.prepareCall(sql, resultSetType,
                        resultSetConcurrency,
                        resultSetHoldability));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledCallableStatement rtnVal = new DruidPooledCallableStatement(this, stmtHolder);
        DruidPooledCallableStatement rtnVal= null ;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    @Override
    public CallableStatement prepareCall(String sql, int resultSetType, int resultSetConcurrency) throws SQLException {
        checkState();

        PreparedStatementHolder stmtHolder = null;
        PreparedStatementKey key = new PreparedStatementKey(sql, getCatalog(), MethodType.Precall_3, resultSetType,
                resultSetConcurrency);

        boolean poolPreparedStatements = holder.isPoolPreparedStatements();

        if (poolPreparedStatements) {
            stmtHolder = holder.getStatementPool().get(key);
        }

        if (stmtHolder == null) {
            try {
                stmtHolder = new PreparedStatementHolder(key,
                        conn.prepareCall(sql, resultSetType, resultSetConcurrency));
                holder.getDataSource().incrementPreparedStatementCount();
            } catch (SQLException ex) {
                handleException(ex);
            }
        }

        initStatement(stmtHolder);

        //DruidPooledCallableStatement rtnVal = new DruidPooledCallableStatement(this, stmtHolder);
        DruidPooledCallableStatement rtnVal=null;
        holder.addTrace(rtnVal);

        return rtnVal;
    }

    // ////////////////////

    @Override
    public Statement createStatement() throws SQLException {
        checkState();

        Statement stmt = null;
        try {
            stmt = conn.createStatement();
        } catch (SQLException ex) {
            handleException(ex);
        }

        //holder.getDataSource().initStatement(this, stmt);

        //DruidPooledStatement poolableStatement = new DruidPooledStatement(this, stmt);
        DruidPooledStatement poolableStatement = null;
        holder.addTrace(poolableStatement);

        return poolableStatement;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        checkState();

        Statement stmt = null;
        try {
            stmt = conn.createStatement(resultSetType, resultSetConcurrency, resultSetHoldability);
        } catch (SQLException ex) {
            handleException(ex);
        }

        //holder.getDataSource().initStatement(this, stmt);

        //DruidPooledStatement poolableStatement = new DruidPooledStatement(this, stmt);
        DruidPooledStatement poolableStatement =null;
                holder.addTrace(poolableStatement);

        return poolableStatement;
    }

    @Override
    public Statement createStatement(int resultSetType, int resultSetConcurrency) throws SQLException {
        checkState();

        Statement stmt = null;
        try {
            stmt = conn.createStatement(resultSetType, resultSetConcurrency);
        } catch (SQLException ex) {
            handleException(ex);
        }

        //holder.getDataSource().initStatement(this, stmt);

        //DruidPooledStatement poolableStatement = new DruidPooledStatement(this, stmt);
        DruidPooledStatement poolableStatement =null;
                holder.addTrace(poolableStatement);

        return poolableStatement;
    }

    @Override
    public String nativeSQL(String sql) throws SQLException {
        checkState();

        return conn.nativeSQL(sql);
    }

    @Override
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        checkState();

        boolean useLocalSessionState = holder.getDataSource().isUseLocalSessionState();

        if (useLocalSessionState) {
            if (autoCommit == holder.isUnderlyingAutoCommit()) {
                return;
            }
        }

        try {
            conn.setAutoCommit(autoCommit);
            holder.setUnderlyingAutoCommit(autoCommit);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    protected void transactionRecord(String sql) throws SQLException {
        if (transactionInfo == null && (!conn.getAutoCommit())) {
            DruidAbstractDataSource dataSource = holder.getDataSource();
            dataSource.incrementStartTransactionCount();
            transactionInfo = new TransactionInfo(dataSource.createTransactionId());
        }

        if (transactionInfo != null) {
            List<String> sqlList = transactionInfo.getSqlList();
            if (sqlList.size() < MAX_RECORD_SQL_COUNT) {
                sqlList.add(sql);
            }
        }
    }

    @Override
    public boolean getAutoCommit() throws SQLException {
        checkState();

        return conn.getAutoCommit();
    }

    @Override
    public void commit() throws SQLException {
        checkState();

        DruidAbstractDataSource dataSource = holder.getDataSource();
        dataSource.incrementCommitCount();

        try {
            conn.commit();
        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            handleEndTransaction(dataSource, null);
        }
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    @Override
    public void rollback() throws SQLException {
        if (transactionInfo == null) {
            return;
        }

        if (holder == null) {
            return;
        }

        DruidAbstractDataSource dataSource = holder.getDataSource();
        dataSource.incrementRollbackCount();

        try {
            conn.rollback();
        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            handleEndTransaction(dataSource, null);
        }
    }

    @Override
    public Savepoint setSavepoint(String name) throws SQLException {
        checkState();

        try {
            return conn.setSavepoint(name);
        } catch (SQLException ex) {
            handleException(ex);
            return null; // never arrive
        }
    }

    @Override
    public void rollback(Savepoint savepoint) throws SQLException {
        if (holder == null) {
            return;
        }

        DruidAbstractDataSource dataSource = holder.getDataSource();
        dataSource.incrementRollbackCount();

        try {
            conn.rollback(savepoint);
        } catch (SQLException ex) {
            handleException(ex);
        } finally {
            handleEndTransaction(dataSource, savepoint);
        }
    }

    private void handleEndTransaction(DruidAbstractDataSource dataSource, Savepoint savepoint) {
        if (transactionInfo != null && savepoint == null) {
            transactionInfo.setEndTimeMillis();

            long transactionMillis = transactionInfo.getEndTimeMillis() - transactionInfo.getStartTimeMillis();
            dataSource.getTransactionHistogram().record(transactionMillis);

            dataSource.logTransaction(transactionInfo);

            transactionInfo = null;
        }
    }

    @Override
    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkState();
        try {
            conn.releaseSavepoint(savepoint);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    @Override
    public Clob createClob() throws SQLException {
        checkState();

        try {
            return conn.createClob();
        } catch (SQLException ex) {
            handleException(ex);
            return null; // never arrive
        }
    }

    @Override
    public boolean isClosed() throws SQLException {
        if (holder == null) {
            return true;
        }

        return conn.isClosed();
    }

    public boolean isAbandonded() {
        return this.abandoned;
    }

    @Override
    public DatabaseMetaData getMetaData() throws SQLException {
        checkState();

        return conn.getMetaData();
    }

    @Override
    public void setReadOnly(boolean readOnly) throws SQLException {
        checkState();

        boolean useLocalSessionState = holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState) {
            if (readOnly == holder.isUnderlyingReadOnly()) {
                return;
            }
        }

        try {
            conn.setReadOnly(readOnly);
        } catch (SQLException ex) {
            handleException(ex);
        }

        holder.setUnderlyingReadOnly(readOnly);
    }

    @Override
    public boolean isReadOnly() throws SQLException {
        checkState();

        return conn.isReadOnly();
    }

    @Override
    public void setCatalog(String catalog) throws SQLException {
        checkState();

        try {
            conn.setCatalog(catalog);
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    @Override
    public String getCatalog() throws SQLException {
        checkState();

        return conn.getCatalog();
    }

    @Override
    public void setTransactionIsolation(int level) throws SQLException {
        checkState();

        boolean useLocalSessionState = holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState) {
            if (level == holder.getUnderlyingTransactionIsolation()) {
                return;
            }
        }

        try {
            conn.setTransactionIsolation(level);
        } catch (SQLException ex) {
            handleException(ex);
        }
        holder.setUnderlyingTransactionIsolation(level);
    }

    @Override
    public int getTransactionIsolation() throws SQLException {
        checkState();

        return holder.getUnderlyingTransactionIsolation();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        checkState();

        return conn.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        checkState();
        try {
            conn.clearWarnings();
        } catch (SQLException ex) {
            handleException(ex);
        }
    }

    @Override
    public Map<String, Class<?>> getTypeMap() throws SQLException {
        checkState();

        return conn.getTypeMap();
    }

    @Override
    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        checkState();

        conn.setTypeMap(map);
    }

    @Override
    public void setHoldability(int holdability) throws SQLException {
        checkState();

        boolean useLocalSessionState = holder.getDataSource().isUseLocalSessionState();
        if (useLocalSessionState) {
            if (holdability == holder.getUnderlyingHoldability()) {
                return;
            }
        }

        conn.setHoldability(holdability);
        holder.setUnderlyingHoldability(holdability);
    }

    @Override
    public int getHoldability() throws SQLException {
        checkState();

        return conn.getHoldability();
    }

    @Override
    public Savepoint setSavepoint() throws SQLException {
        checkState();

        try {
            return conn.setSavepoint();
        } catch (SQLException ex) {
            handleException(ex);
            return null;
        }
    }

    @Override
    public Blob createBlob() throws SQLException {
        checkState();

        return conn.createBlob();
    }

    @Override
    public NClob createNClob() throws SQLException {
        checkState();

        return conn.createNClob();
    }

    @Override
    public SQLXML createSQLXML() throws SQLException {
        checkState();

        return conn.createSQLXML();
    }

    @Override
    public boolean isValid(int timeout) throws SQLException {
        checkState();

        return conn.isValid(timeout);
    }

    @Override
    public void setClientInfo(String name, String value) throws SQLClientInfoException {
        if (holder == null) {
            throw new SQLClientInfoException();
        }

        conn.setClientInfo(name, value);
    }

    @Override
    public void setClientInfo(Properties properties) throws SQLClientInfoException {
        if (holder == null) {
            throw new SQLClientInfoException();
        }

        conn.setClientInfo(properties);
    }

    @Override
    public String getClientInfo(String name) throws SQLException {
        checkState();

        return conn.getClientInfo(name);
    }

    @Override
    public Properties getClientInfo() throws SQLException {
        checkState();

        return conn.getClientInfo();
    }

    @Override
    public Array createArrayOf(String typeName, Object[] elements) throws SQLException {
        checkState();

        return conn.createArrayOf(typeName, elements);
    }

    @Override
    public Struct createStruct(String typeName, Object[] attributes) throws SQLException {
        checkState();

        return conn.createStruct(typeName, attributes);
    }

    @Override
    public void addConnectionEventListener(ConnectionEventListener listener) {
        if (holder == null) {
            throw new IllegalStateException();
        }

        holder.getConnectionEventListeners().add(listener);
    }

    @Override
    public void removeConnectionEventListener(ConnectionEventListener listener) {
        if (holder == null) {
            throw new IllegalStateException();
        }

        holder.getConnectionEventListeners().remove(listener);
    }

    @Override
    public void addStatementEventListener(StatementEventListener listener) {
        if (holder == null) {
            throw new IllegalStateException();
        }

        holder.getStatementEventListeners().add(listener);
    }

    @Override
    public void removeStatementEventListener(StatementEventListener listener) {
        if (holder == null) {
            throw new IllegalStateException();
        }

        holder.getStatementEventListeners().remove(listener);
    }

    public Throwable getDisableError() {
        return disableError;
    }

    public void checkState() throws SQLException {
        final boolean asyncCloseEnabled;
        if (holder != null) {
            asyncCloseEnabled = holder.getDataSource().isAsyncCloseConnectionEnable();
        } else {
            asyncCloseEnabled = false;
        }

        if (asyncCloseEnabled) {
            synchronized (this) {
                checkStateInternal();
            }
        } else {
            checkStateInternal();
        }
    }

    private void checkStateInternal() throws SQLException {
        if (holder == null) {
            if (disableError != null) {
                throw new SQLException("connection holder is null", disableError);
            } else {
                throw new SQLException("connection holder is null");
            }
        }

        if (closed) {
            if (disableError != null) {
                throw new SQLException("connection closed", disableError);
            } else {
                throw new SQLException("connection closed");
            }
        }

        if (disable) {
            if (disableError != null) {
                throw new SQLException("connection disabled", disableError);
            } else {
                throw new SQLException("connection disabled");
            }
        }
    }

    public String toString() {
        if (conn != null) {
            return conn.toString();
        } else {
            return "closed-conn-" + System.identityHashCode(this);
        }
    }

    public void setSchema(String schema) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public String getSchema() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void abort(Executor executor) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public void setNetworkTimeout(Executor executor, int milliseconds) throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    public int getNetworkTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException();
    }

    final void beforeExecute() {
        final com.alibaba.druid.pool.DruidConnectionHolder holder = this.holder;
        if (holder != null && holder.getDataSource().isRemoveAbandoned()) {
            running = true;
        }
    }

    final void afterExecute() {
        final DruidConnectionHolder holder = this.holder;
        if (holder != null && holder.getDataSource().isRemoveAbandoned()) {
            running = false;
            holder.setLastActiveTimeMillis(System.currentTimeMillis());
        }
    }

    boolean isRunning() {
        return running;
    }

    public void abandond() {
        this.abandoned = true;
    }
}
