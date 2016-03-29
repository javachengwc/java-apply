package com.ground.core.callback;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface IStatementCallback<T> {

	  public T doInStatement(PreparedStatement preparedStatement) throws SQLException;
}
