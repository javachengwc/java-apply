package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.google.common.base.Optional;

/**
 * MySQL的UPDATE语句访问器
 */
public class MysqlUpdateVisitor extends AbstractMysqlVisitor {

    @Override
    public boolean visit(final MySqlUpdateStatement x) {
        getParseContext().setCurrentTable(x.getTableName().toString(), Optional.<String>absent());
        return super.visit(x);
    }
}