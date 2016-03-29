package com.rule.data.model.vo;

import com.rule.data.engine.excel.NumberPool;

import java.sql.Connection;

public class RengineConnection {

    public Connection jdbcConn;

    public long lastCheckTime = NumberPool.LONG_0;

    public RengineConnection(Connection jdbcConn, long lastCheckTime) {

        this.jdbcConn = jdbcConn;
        this.lastCheckTime = lastCheckTime;
    }
}
