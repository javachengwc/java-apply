package com.ground.core.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * db查询结果包装器 针对查询列表中每行数据的包装
 */
public interface IRowMapper<T> {

	public abstract T mapRow(ResultSet paramResultSet, int paramInt) throws SQLException;

}
