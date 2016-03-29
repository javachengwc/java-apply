package com.ocean.jdbc.adapter;

/**
 * 结果集适配器
 */

import com.ocean.jdbc.nofrills.AbstractNofrillsResultSet;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public abstract class AbstractResultSetAdapter extends AbstractNofrillsResultSet {

    protected List<ResultSet> resultSets;

    protected ResultSet currentResultSet;

    protected boolean closed;

    public List<ResultSet> getResultSets() {
        return resultSets;
    }

    public void setResultSets(List<ResultSet> resultSets) {
        this.resultSets = resultSets;
    }

    public ResultSet getCurrentResultSet() {
        return currentResultSet;
    }

    public void setCurrentResultSet(ResultSet currentResultSet) {
        this.currentResultSet = currentResultSet;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public boolean getBoolean(int columnIndex) throws SQLException {
        return currentResultSet.getBoolean(columnIndex);
    }

    @Override
    public boolean getBoolean(String columnLabel) throws SQLException {
        return currentResultSet.getBoolean(columnLabel);
    }

    @Override
    public byte getByte(int columnIndex) throws SQLException {
        return currentResultSet.getByte(columnIndex);
    }

    @Override
    public byte getByte(String columnLabel) throws SQLException {
        return currentResultSet.getByte(columnLabel);
    }

    @Override
    public short getShort(int columnIndex) throws SQLException {
        return currentResultSet.getShort(columnIndex);
    }

    @Override
    public short getShort(String columnLabel) throws SQLException {
        return currentResultSet.getShort(columnLabel);
    }

    @Override
    public int getInt(int columnIndex) throws SQLException {
        return currentResultSet.getInt(columnIndex);
    }

    @Override
    public int getInt(String columnLabel) throws SQLException {
        return currentResultSet.getInt(columnLabel);
    }

    @Override
    public long getLong(int columnIndex) throws SQLException {
        return currentResultSet.getLong(columnIndex);
    }

    @Override
    public long getLong(String columnLabel) throws SQLException {
        return currentResultSet.getLong(columnLabel);
    }

    @Override
    public float getFloat(int columnIndex) throws SQLException {
        return currentResultSet.getFloat(columnIndex);
    }

    @Override
    public float getFloat(String columnLabel) throws SQLException {
        return currentResultSet.getFloat(columnLabel);
    }

    @Override
    public double getDouble(int columnIndex) throws SQLException {
        return currentResultSet.getDouble(columnIndex);
    }

    @Override
    public double getDouble(String columnLabel) throws SQLException {
        return currentResultSet.getDouble(columnLabel);
    }

    @Override
    public String getString(int columnIndex) throws SQLException {
        return currentResultSet.getString(columnIndex);
    }

    @Override
    public String getString(String columnLabel) throws SQLException {
        return currentResultSet.getString(columnLabel);
    }

    @Override
    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return currentResultSet.getBigDecimal(columnIndex);
    }

    @Override
    public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
        return currentResultSet.getBigDecimal(columnLabel);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        return currentResultSet.getBigDecimal(columnIndex, scale);
    }

    @SuppressWarnings("deprecation")
    @Override
    public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
        return currentResultSet.getBigDecimal(columnLabel, scale);
    }

    @Override
    public byte[] getBytes(int columnIndex) throws SQLException {
        return currentResultSet.getBytes(columnIndex);
    }

    @Override
    public byte[] getBytes(String columnLabel) throws SQLException {
        return currentResultSet.getBytes(columnLabel);
    }

    @Override
    public Date getDate(int columnIndex) throws SQLException {
        return currentResultSet.getDate(columnIndex);
    }

    @Override
    public Date getDate(String columnLabel) throws SQLException {
        return currentResultSet.getDate(columnLabel);
    }

    @Override
    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return currentResultSet.getDate(columnIndex, cal);
    }

    @Override
    public Date getDate(String columnLabel, Calendar cal) throws SQLException {
        return currentResultSet.getDate(columnLabel, cal);
    }

    @Override
    public Time getTime(int columnIndex) throws SQLException {
        return currentResultSet.getTime(columnIndex);
    }

    @Override
    public Time getTime(String columnLabel) throws SQLException {
        return currentResultSet.getTime(columnLabel);
    }

    @Override
    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return currentResultSet.getTime(columnIndex, cal);
    }

    @Override
    public Time getTime(String columnLabel, Calendar cal) throws SQLException {
        return currentResultSet.getTime(columnLabel, cal);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return currentResultSet.getTimestamp(columnIndex);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel) throws SQLException {
        return currentResultSet.getTimestamp(columnLabel);
    }

    @Override
    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return currentResultSet.getTimestamp(columnIndex, cal);
    }

    @Override
    public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
        return currentResultSet.getTimestamp(columnLabel, cal);
    }

    @Override
    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return currentResultSet.getAsciiStream(columnIndex);
    }

    @Override
    public InputStream getAsciiStream(String columnLabel) throws SQLException {
        return currentResultSet.getAsciiStream(columnLabel);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        return currentResultSet.getUnicodeStream(columnIndex);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InputStream getUnicodeStream(String columnLabel) throws SQLException {
        return currentResultSet.getUnicodeStream(columnLabel);
    }

    @Override
    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return currentResultSet.getBinaryStream(columnIndex);
    }

    @Override
    public InputStream getBinaryStream(String columnLabel) throws SQLException {
        return currentResultSet.getBinaryStream(columnLabel);
    }

    @Override
    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return currentResultSet.getCharacterStream(columnIndex);
    }

    @Override
    public Reader getCharacterStream(String columnLabel) throws SQLException {
        return currentResultSet.getCharacterStream(columnLabel);
    }

    @Override
    public Blob getBlob(int columnIndex) throws SQLException {
        return currentResultSet.getBlob(columnIndex);
    }

    @Override
    public Blob getBlob(String columnLabel) throws SQLException {
        return currentResultSet.getBlob(columnLabel);
    }

    @Override
    public Clob getClob(int columnIndex) throws SQLException {
        return currentResultSet.getClob(columnIndex);
    }

    @Override
    public Clob getClob(String columnLabel) throws SQLException {
        return currentResultSet.getClob(columnLabel);
    }

    @Override
    public URL getURL(int columnIndex) throws SQLException {
        return currentResultSet.getURL(columnIndex);
    }

    @Override
    public URL getURL(String columnLabel) throws SQLException {
        return currentResultSet.getURL(columnLabel);
    }

    @Override
    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        return currentResultSet.getSQLXML(columnIndex);
    }

    @Override
    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        return currentResultSet.getSQLXML(columnLabel);
    }

    @Override
    public Object getObject(int columnIndex) throws SQLException {
        return currentResultSet.getObject(columnIndex);
    }

    @Override
    public Object getObject(String columnLabel) throws SQLException {
        return currentResultSet.getObject(columnLabel);
    }

    @Override
    public Object getObject(int columnIndex, Map<String, Class<?>> map) throws SQLException {
        return currentResultSet.getObject(columnIndex, map);
    }

    @Override
    public Object getObject(String columnLabel, Map<String, Class<?>> map) throws SQLException {
        return currentResultSet.getObject(columnLabel, map);
    }
    
    
    @Override
    public void close() throws SQLException {
        for (ResultSet each : resultSets) {
            each.close();
        }
        closed = true;
    }

    @Override
    public boolean isClosed() throws SQLException {
        return closed;
    }

    @Override
    public boolean wasNull() throws SQLException {
        return getCurrentResultSet().wasNull();
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return getCurrentResultSet().getFetchDirection();
    }

    @Override
    public void setFetchDirection(int direction) throws SQLException {
        for (ResultSet each : resultSets) {
            each.setFetchDirection(direction);
        }
    }

    @Override
    public int getFetchSize() throws SQLException {
        return getCurrentResultSet().getFetchSize();
    }

    @Override
    public void setFetchSize(int rows) throws SQLException {
        for (ResultSet each : resultSets) {
            each.setFetchSize(rows);
        }
    }

    @Override
    public int getType() throws SQLException {
        return getCurrentResultSet().getType();
    }

    @Override
    public int getConcurrency() throws SQLException {
        return getCurrentResultSet().getConcurrency();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return getCurrentResultSet().getStatement();
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return getCurrentResultSet().getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        getCurrentResultSet().clearWarnings();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return getCurrentResultSet().getMetaData();
    }

    @Override
    public int findColumn(String columnLabel) throws SQLException {
        return getCurrentResultSet().findColumn(columnLabel);
    }
}
