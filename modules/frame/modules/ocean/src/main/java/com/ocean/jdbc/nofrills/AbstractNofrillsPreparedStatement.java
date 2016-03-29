package com.ocean.jdbc.nofrills;

import com.ocean.jdbc.ShardConnection;
import com.ocean.jdbc.ShardStatement;
import com.ocean.router.SqlRouteEngine;

import java.io.Reader;
import java.sql.*;

/**
 * 声明基本预编译语句对象
 */
public abstract class AbstractNofrillsPreparedStatement  extends ShardStatement implements PreparedStatement {

    public AbstractNofrillsPreparedStatement(SqlRouteEngine sqlRouteEngine, ShardConnection shardConnection,
                                                         int resultSetType, int resultSetConcurrency, int resultSetHoldability) throws SQLException {
        super(sqlRouteEngine, shardConnection, resultSetType, resultSetConcurrency, resultSetHoldability);
    }

    @Override
    public final ResultSetMetaData getMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("getMetaData");
    }

    @Override
    public final ParameterMetaData getParameterMetaData() throws SQLException {
        throw new SQLFeatureNotSupportedException("ParameterMetaData");
    }

    @Override
    public final void setNString(final int parameterIndex, final String x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNString");
    }

    @Override
    public final void setNClob(final int parameterIndex, final NClob x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNClob");
    }

    @Override
    public final void setNClob(final int parameterIndex, final Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNClob");
    }

    @Override
    public final void setNClob(final int parameterIndex, final Reader x, final long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNClob");
    }

    @Override
    public final void setNCharacterStream(final int parameterIndex, final Reader x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNCharacterStream");
    }

    @Override
    public final void setNCharacterStream(final int parameterIndex, final Reader x, final long length) throws SQLException {
        throw new SQLFeatureNotSupportedException("setNCharacterStream");
    }

    @Override
    public final void setArray(final int parameterIndex, final Array x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setArray");
    }

    @Override
    public final void setRowId(final int parameterIndex, final RowId x) throws SQLException {
        throw new SQLFeatureNotSupportedException("setRowId");
    }
}