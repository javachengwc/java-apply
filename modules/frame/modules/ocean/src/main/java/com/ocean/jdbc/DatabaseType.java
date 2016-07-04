package com.ocean.jdbc;

import com.ocean.exception.DatabaseTypeUnsupportedException;

/**
 * 数据库类型
 */
public enum DatabaseType {

    H2, MySQL, Oracle, SQLServer, DB2;

    /**
     * 获取数据库类型枚举
     */
    public static DatabaseType valueFrom(String databaseProductName) {
        try {
            return DatabaseType.valueOf(databaseProductName);
        } catch (final IllegalArgumentException ex) {
            throw new DatabaseTypeUnsupportedException(databaseProductName);
        }
    }
}