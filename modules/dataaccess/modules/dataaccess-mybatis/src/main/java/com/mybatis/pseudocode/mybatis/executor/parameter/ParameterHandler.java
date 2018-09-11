package com.mybatis.pseudocode.mybatis.executor.parameter;

import java.sql.PreparedStatement;
import java.sql.SQLException;

//负责对用户传递的参数转换成JDBC Statement 所需要的参数
public interface ParameterHandler {

    Object getParameterObject();

    //设置sql参数
    void setParameters(PreparedStatement var1) throws SQLException;
}
