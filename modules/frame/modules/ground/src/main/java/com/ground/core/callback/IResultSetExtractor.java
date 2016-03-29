package com.ground.core.callback;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * db查询结果包装器
 */
public abstract interface IResultSetExtractor<T> {
	
	public abstract T extractData(ResultSet paramResultSet) throws SQLException;
}
