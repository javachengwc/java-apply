package com.ocean.jdbc.nofrills;

import com.ocean.jdbc.adapter.WrapperAdapter;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * 基本操作的数据源对象
 */
public abstract class AbstractNofrillsDataSource extends WrapperAdapter implements DataSource {

    @Override
    public final int getLoginTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException("unsupported getLoginTimeout()");
    }

    @Override
    public final void setLoginTimeout( int seconds) throws SQLException {
        throw new SQLFeatureNotSupportedException("unsupported setLoginTimeout(int seconds)");
    }
}

