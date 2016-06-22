package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.google.common.base.Optional;
import com.ocean.parser.SqlCondition;

/**
 * MySQL的INSERT语句访问器
 */
public class MysqlInsertVisitor extends AbstractMysqlVisitor {

    @Override
    public boolean visit(final MySqlInsertStatement x) {
        getParseContext().setCurrentTable(x.getTableName().toString(), Optional.fromNullable(x.getAlias()));
        for (int i = 0; i < x.getColumns().size(); i++) {
            getParseContext().addCondition(x.getColumns().get(i).toString(), x.getTableName().toString(), SqlCondition.BinaryOperator.EQUAL, x.getValues().getValues().get(i), getDatabaseType(), getParameters());
        }
        return super.visit(x);
    }
}