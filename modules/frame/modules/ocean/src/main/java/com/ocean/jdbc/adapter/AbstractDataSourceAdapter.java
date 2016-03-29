package com.ocean.jdbc.adapter;

import com.ocean.jdbc.nofrills.AbstractNofrillsDataSource;

import java.io.PrintWriter;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

/**
 * 数据源适配类
 */
public abstract class AbstractDataSourceAdapter  extends AbstractNofrillsDataSource {


    private PrintWriter logWriter = new PrintWriter(System.out);

    @Override
    public PrintWriter getLogWriter() throws SQLException {
        return logWriter;
    }

    @Override
    public void setLogWriter(final PrintWriter out) throws SQLException {
        this.logWriter = out;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    }
}