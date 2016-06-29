package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.ast.SQLHint;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.ocean.shard.DatabaseType;
import com.ocean.parser.ParseContext;
import com.ocean.parser.SqlBuilder;
import com.ocean.parser.SqlCondition;
import com.ocean.parser.SqlTable;
import com.ocean.visitor.SqlVisitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * MySQL解析基础访问器
 */
public abstract class AbstractMysqlVisitor extends MySqlOutputVisitor implements SqlVisitor {

    protected static Logger logger = LoggerFactory.getLogger(MysqlSelectVisitor.class);

    private ParseContext parseContext = new ParseContext();

    public AbstractMysqlVisitor() {
        super(new SqlBuilder());
        setPrettyFormat(false);
    }

    public void setParseContext(ParseContext parseContext) {
        this.parseContext = parseContext;
    }

    @Override
    public DatabaseType getDatabaseType() {
        return DatabaseType.MySQL;
    }

    @Override
    public ParseContext getParseContext() {
        return parseContext;
    }

    @Override
    public final SqlBuilder getSqlBuilder() {
        return (SqlBuilder) appender;
    }

    @Override
    public final void printToken(final String token) {
        getSqlBuilder().appendToken(parseContext.getExactlyValue(token));
    }

    /**
     * 父类使用<tt>@@</tt>代替<tt>?</tt>,此处直接输出参数占位符<tt>?</tt>
     * @param x 变量表达式
     * @return false 终止遍历AST
     */
    @Override
    public final boolean visit(final SQLVariantRefExpr x) {
        print(x.getName());
        return false;
    }

    @Override
    public boolean visit(final SQLExprTableSource x) {
        return visit(x, parseContext.addTable(x));
    }

    private boolean visit(final SQLExprTableSource x, SqlTable table) {
        printToken(table.getName());
        if (table.getAlias().isPresent()) {
            print(' ');
            print(table.getAlias().get());
        }
        for (SQLHint each : x.getHints()) {
            print(' ');
            each.accept(this);
        }
        return false;
    }

    /**
     * 将表名替换成占位符
     * 1. 如果二元表达式使用别名, 如:
     * {@code FROM order o WHERE o.column_name = 't' }, 则Column中的tableName为o.
     * 2. 如果二元表达式使用表名, 如:
     * {@code FROM order WHERE order.column_name = 't' }, 则Column中的tableName为order.
     * @param x SQL属性表达式
     * @return true表示继续遍历AST, false表示终止遍历AST
     */
    @Override
    public final boolean visit(final SQLPropertyExpr x) {
        if (!(x.getParent() instanceof SQLBinaryOpExpr) && !(x.getParent() instanceof SQLSelectItem)) {
            return super.visit(x);
        }
        if (!(x.getOwner() instanceof SQLIdentifierExpr)) {
            return super.visit(x);
        }
        String tableOrAliasName = ((SQLIdentifierExpr) x.getOwner()).getLowerName();
        if (parseContext.isBinaryOperateWithAlias(x, tableOrAliasName)) {
            return super.visit(x);
        }
        printToken(tableOrAliasName);
        print(".");
        print(x.getName());
        return false;
    }

    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        switch (x.getOperator()) {
            case BooleanOr:
                parseContext.setHasOrCondition(true);
                break;
            case Equality:
                parseContext.addCondition(x.getLeft(), SqlCondition.BinaryOperator.EQUAL, Arrays.asList(x.getRight()), getDatabaseType(), getParameters());
                parseContext.addCondition(x.getRight(), SqlCondition.BinaryOperator.EQUAL, Arrays.asList(x.getLeft()), getDatabaseType(), getParameters());
                break;
            default:
                break;
        }
        return super.visit(x);
    }

    @Override
    public boolean visit(final SQLInListExpr x) {
        parseContext.addCondition(x.getExpr(), x.isNot() ? SqlCondition.BinaryOperator.NOT_IN : SqlCondition.BinaryOperator.IN, x.getTargetList(), getDatabaseType(), getParameters());
        return super.visit(x);
    }

    @Override
    public boolean visit(final SQLBetweenExpr x) {
        parseContext.addCondition(x.getTestExpr(), SqlCondition.BinaryOperator.BETWEEN, Arrays.asList(x.getBeginExpr(), x.getEndExpr()), getDatabaseType(), getParameters());
        return super.visit(x);
    }
}
