package com.mybatis.pseudocode.mybatis.type;


import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract interface TypeHandler<T>
{
    public abstract void setParameter(PreparedStatement preparedStatement, int paramInt, T t, JdbcType jdbcType) throws SQLException;

    public abstract T getResult(ResultSet resultSet, String param)  throws SQLException;

    public abstract T getResult(ResultSet resultSet, int paramInt) throws SQLException;

    public abstract T getResult(CallableStatement callableStatement, int paramInt) throws SQLException;
}
