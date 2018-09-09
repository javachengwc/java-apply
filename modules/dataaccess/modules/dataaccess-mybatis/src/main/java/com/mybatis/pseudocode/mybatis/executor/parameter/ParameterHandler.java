package com.mybatis.pseudocode.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ParameterHandler {

    Object getParameterObject();

    void setParameters(PreparedStatement var1) throws SQLException;
}
