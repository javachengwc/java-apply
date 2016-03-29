package com.ground.core.callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * db sql语句创建器
 */
public interface IPreparedStatementCreator {

	public String genericSql();
	
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException;
}
