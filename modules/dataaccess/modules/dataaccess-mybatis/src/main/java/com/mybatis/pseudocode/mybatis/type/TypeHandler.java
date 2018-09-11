package com.mybatis.pseudocode.mybatis.type;


import com.mybatis.pseudocode.mybatis.mapping.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

//负责java数据类型和jdbc数据类型之间的映射和转换
public abstract interface TypeHandler<T>
{
    //设置参数，包含参数序列化
    public abstract void setParameter(PreparedStatement preparedStatement, int paramInt, T t, JdbcType jdbcType) throws SQLException;

    //获取结果，包含查询结果的反序列化
    public abstract T getResult(ResultSet resultSet, String param)  throws SQLException;

    public abstract T getResult(ResultSet resultSet, int paramInt) throws SQLException;

    public abstract T getResult(CallableStatement callableStatement, int paramInt) throws SQLException;
}
