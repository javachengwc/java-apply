package com.ground.core.callback;

import java.sql.Connection;
import java.sql.SQLException;
/**
 * 数据访问回调接口
 */
public interface IJdbcCallback<T> {
   
	  public T doInJdbc(Connection paramConnection) throws SQLException;
}
