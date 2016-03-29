package com.ground.core.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * db ps参数设置器
 *
 */
public interface IPreparedStatementSetter {

	public void  setValues(PreparedStatement preparedStatement) throws SQLException;    

}
