package com.ocean.parser;

import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.ocean.shard.DatabaseType;
import com.ocean.exception.DatabaseTypeUnsupportedException;
import com.ocean.visitor.mysql.MysqlDeleteVisitor;
import com.ocean.visitor.mysql.MysqlInsertVisitor;
import com.ocean.visitor.mysql.MysqlSelectVisitor;
import com.ocean.visitor.mysql.MysqlUpdateVisitor;

import java.util.HashMap;
import java.util.Map;

/**
 * sql访问器注册
 */
public class SqlVisitorRegistry {

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> SELECT_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(DatabaseType.values().length);

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> INSERT_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(DatabaseType.values().length);

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> UPDATE_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(DatabaseType.values().length);

    private static final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> DELETE_REGISTRY = new HashMap<DatabaseType, Class<? extends SQLASTOutputVisitor>>(DatabaseType.values().length);

    static {
        registerSelectVistor();
        registerInsertVistor();
        registerUpdateVistor();
        registerDeleteVistor();
    }

    private static void registerSelectVistor() {
        SELECT_REGISTRY.put(DatabaseType.H2, MysqlSelectVisitor.class);
        SELECT_REGISTRY.put(DatabaseType.MySQL, MysqlSelectVisitor.class);
    }

    private static void registerInsertVistor() {
        INSERT_REGISTRY.put(DatabaseType.H2, MysqlInsertVisitor.class);
        INSERT_REGISTRY.put(DatabaseType.MySQL, MysqlInsertVisitor.class);
    }

    private static void registerUpdateVistor() {
        UPDATE_REGISTRY.put(DatabaseType.H2, MysqlUpdateVisitor.class);
        UPDATE_REGISTRY.put(DatabaseType.MySQL, MysqlUpdateVisitor.class);
    }

    private static void registerDeleteVistor() {
        DELETE_REGISTRY.put(DatabaseType.H2, MysqlDeleteVisitor.class);
        DELETE_REGISTRY.put(DatabaseType.MySQL, MysqlDeleteVisitor.class);
    }

    /**
     * 获取SELECT访问器.
     *
     * @param databaseType 数据库类型
     * @return SELECT访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getSelectVistor(final DatabaseType databaseType) {
        return getVistor(databaseType, SELECT_REGISTRY);
    }

    /**
     * 获取INSERT访问器
     * @param databaseType 数据库类型
     * @return INSERT访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getInsertVistor(final DatabaseType databaseType) {
        return getVistor(databaseType, INSERT_REGISTRY);
    }

    /**
     * 获取UPDATE访问器.
     *
     * @param databaseType 数据库类型
     * @return UPDATE访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getUpdateVistor(final DatabaseType databaseType) {
        return getVistor(databaseType, UPDATE_REGISTRY);
    }

    /**
     * 获取DELETE访问器
     * @param databaseType 数据库类型
     * @return DELETE访问器
     */
    public static Class<? extends SQLASTOutputVisitor> getDeleteVistor(final DatabaseType databaseType) {
        return getVistor(databaseType, DELETE_REGISTRY);
    }

    private static Class<? extends SQLASTOutputVisitor> getVistor(final DatabaseType databaseType, final Map<DatabaseType, Class<? extends SQLASTOutputVisitor>> registry) {
        if (!registry.containsKey(databaseType)) {
            throw new DatabaseTypeUnsupportedException(databaseType.name());
        }
        return registry.get(databaseType);
    }
}

