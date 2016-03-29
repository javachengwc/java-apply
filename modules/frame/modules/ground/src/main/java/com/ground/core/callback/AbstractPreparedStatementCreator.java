package com.ground.core.callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractPreparedStatementCreator implements IPreparedStatementCreator{
	
	public PreparedStatement createPreparedStatement(Connection connection) throws SQLException
	{
		String sql = genericSql();
		return connection.prepareStatement(sql);
	}


}
