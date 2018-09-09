package com.mybatis.pseudocode.mybatis.mapping;

public enum SqlCommandType {
    UNKNOWN,
    INSERT,
    UPDATE,
    DELETE,
    SELECT,
    FLUSH;

    private SqlCommandType() {
    }
}
