package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLOrderBy;
import com.alibaba.druid.sql.ast.expr.*;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectOrderByItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.expr.MySqlSelectGroupByExpr;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlOutputVisitor;
import com.google.common.base.Optional;
import com.ocean.merger.aggregation.AggregationColumn;
import com.ocean.merger.groupby.GroupByColumn;
import com.ocean.parser.Limit;
import com.ocean.merger.orderby.OrderByColumn;
import org.apache.log4j.spi.LoggerFactory;

import java.util.List;

/**
 * MySQL的SELECT语句访问器
 */
public class MysqlSelectVisitor  extends AbstractMysqlVisitor {

    private static final String AUTO_GEN_TOKE_KEY = "ocean_auto_gen";

    private int itemIndex;

    @Override
    protected void printSelectList(final List<SQLSelectItem> selectList) {
        super.printSelectList(selectList);
        getSqlBuilder().appendToken(AUTO_GEN_TOKE_KEY, false);
    }

    @Override
    public boolean visit(MySqlSelectQueryBlock x) {
        logger.info("MysqlSelectVisitor visit start");
        if (x.getFrom() instanceof SQLExprTableSource) {
            SQLExprTableSource tableExpr = (SQLExprTableSource) x.getFrom();
            getParseContext().setCurrentTable(tableExpr.getExpr().toString(), Optional.fromNullable(tableExpr.getAlias()));
            logger.info("MysqlSelectVisitor visit tableExpr expr="+tableExpr.getExpr()+",alias="+tableExpr.getAlias());
        }
        return super.visit(x);
    }

    /**
     * 解析 {@code SELECT item1,item2 FROM }中的item
     * @param x SELECT item 表达式
     * @return true表示继续遍历AST, false表示终止遍历AST
     */
    //ELECT * 导致index不准，不支持SELECT *
    public boolean visit(final SQLSelectItem x) {
        itemIndex++;
        return super.visit(x);
    }

    @Override
    public boolean visit(final SQLAggregateExpr x) {
        logger.info("MysqlSelectVisitor visit  aggregate expr start");
        if (!(x.getParent() instanceof SQLSelectItem)) {
            return super.visit(x);
        }
        AggregationColumn.AggregationType aggregationType;
        try {
            aggregationType = AggregationColumn.AggregationType.valueOf(x.getMethodName().toUpperCase());
        } catch (final IllegalArgumentException ex) {
            return super.visit(x);
        }
        StringBuilder expression = new StringBuilder();
        x.accept(new MySqlOutputVisitor(expression));
        //index获取不准，考虑使用别名替换
        AggregationColumn column = new AggregationColumn(expression.toString(), aggregationType, Optional.fromNullable(((SQLSelectItem) x.getParent()).getAlias()),
                (null == x.getOption()) ? Optional.<String>absent() : Optional.of(x.getOption().toString()), itemIndex);
        getParseContext().getParsedResult().getMergeContext().getAggregationColumns().add(column);
        if (AggregationColumn.AggregationType.AVG.equals(aggregationType)) {
            getParseContext().addDerivedColumnsForAvgColumn(column);
        }
        return super.visit(x);
    }

    public boolean visit(final SQLOrderBy x) {
        logger.info("MysqlSelectVisitor visit  order by  start");
        for (SQLSelectOrderByItem each : x.getItems()) {
            SQLExpr expr = each.getExpr();
            OrderByColumn.OrderByType orderByType = null == each.getType() ? OrderByColumn.OrderByType.ASC : OrderByColumn.OrderByType.valueOf(each.getType());
            if (expr instanceof SQLIntegerExpr) {
                getParseContext().addOrderByColumn(((SQLIntegerExpr) expr).getNumber().intValue(), orderByType);
            } else if (expr instanceof SQLIdentifierExpr) {
                getParseContext().addOrderByColumn(((SQLIdentifierExpr) expr).getName(), orderByType);
            } else if (expr instanceof SQLPropertyExpr) {
                getParseContext().addOrderByColumn(((SQLPropertyExpr) expr).getName(), orderByType);
            }
        }
        return super.visit(x);
    }

    /**
     * 将GROUP BY列放入parseResult
     * 直接返回false,防止重复解析GROUP BY表达式
     * @param x GROUP BY 表达式
     * @return false 停止遍历AST
     */
    @Override
    public boolean visit(final MySqlSelectGroupByExpr x) {
        logger.info("MysqlSelectVisitor visit  group by  start");
        String alias = getParseContext().generateDerivedColumnAlias();
        OrderByColumn.OrderByType orderByType = null == x.getType() ? OrderByColumn.OrderByType.ASC : OrderByColumn.OrderByType.valueOf(x.getType());
        if (x.getExpr() instanceof SQLPropertyExpr) {
            SQLPropertyExpr expr = (SQLPropertyExpr) x.getExpr();
            getParseContext().addGroupByColumns(expr.toString(), alias, orderByType);
        } else if (x.getExpr() instanceof SQLIdentifierExpr) {
            SQLIdentifierExpr expr = (SQLIdentifierExpr) x.getExpr();
            getParseContext().addGroupByColumns(expr.getName(), alias, orderByType);
        } else {
            return super.visit(x);
        }
        return super.visit(x);
    }

    /**
     * LIMIT 解析
     * @param x LIMIT表达式
     * @return false 停止遍历AST
     */
    @Override
    public boolean visit(final MySqlSelectQueryBlock.Limit x) {
        logger.info("MysqlSelectVisitor visit limit start..");
        print("LIMIT ");
        int offset = 0;
        if (null != x.getOffset()) {
            if (x.getOffset() instanceof SQLNumericLiteralExpr) {
                offset = ((SQLNumericLiteralExpr) x.getOffset()).getNumber().intValue();
                print("0, ");
            } else {
                offset = ((Number) getParameters().get(((SQLVariantRefExpr) x.getOffset()).getIndex())).intValue();
                getParameters().set(((SQLVariantRefExpr) x.getOffset()).getIndex(), 0);
                print("?, ");
            }
        }
        int rowCount;
        if (x.getRowCount() instanceof SQLNumericLiteralExpr) {
            rowCount = ((SQLNumericLiteralExpr) x.getRowCount()).getNumber().intValue();
            print(rowCount + offset);
        } else {
            rowCount = ((Number) getParameters().get(((SQLVariantRefExpr) x.getRowCount()).getIndex())).intValue();
            getParameters().set(((SQLVariantRefExpr) x.getRowCount()).getIndex(), rowCount + offset);
            print("?");
        }
        getParseContext().getParsedResult().getMergeContext().setLimit(new Limit(offset, rowCount));
        return false;
    }

    @Override
    public void endVisit(SQLSelectStatement x) {
        logger.info("MysqlSelectVisitor endVisit start..");

        StringBuilder derivedSelectItems = new StringBuilder();
        for (AggregationColumn aggregationColumn : getParseContext().getParsedResult().getMergeContext().getAggregationColumns()) {
            for (AggregationColumn derivedColumn : aggregationColumn.getDerivedColumns()) {
                derivedSelectItems.append(", ").append(derivedColumn.getExpression()).append(" AS ").append(derivedColumn.getAlias().get());
            }
        }
        for (GroupByColumn each : getParseContext().getParsedResult().getMergeContext().getGroupByColumns()) {
            derivedSelectItems.append(", ").append(each.getName()).append(" AS ").append(each.getAlias());
        }
        if (0 != derivedSelectItems.length()) {
            getSqlBuilder().buildSQL(AUTO_GEN_TOKE_KEY, derivedSelectItems.toString());
        }
        logger.info("MysqlSelectVisitor parseContext="+getParseContext().toString());
        logger.info("MysqlSelectVisitor sqlBuilder="+getSqlBuilder().toString());
        super.endVisit(x);
    }
}