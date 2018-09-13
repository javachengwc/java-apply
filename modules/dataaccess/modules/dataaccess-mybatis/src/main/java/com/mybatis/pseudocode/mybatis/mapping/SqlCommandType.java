package com.mybatis.pseudocode.mybatis.mapping;

//执行的sql类型
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
