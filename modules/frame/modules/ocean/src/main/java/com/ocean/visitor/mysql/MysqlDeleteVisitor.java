package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlDeleteStatement;
import com.google.common.base.Optional;

/**
 * MySQL的DELETE语句访问器
 */
public class MysqlDeleteVisitor  extends AbstractMysqlVisitor {

    @Override
    public boolean visit(final MySqlDeleteStatement x) {
        getParseContext().setCurrentTable(x.getTableName().toString(), Optional.fromNullable(x.getAlias()));
        return super.visit(x);
    }
}