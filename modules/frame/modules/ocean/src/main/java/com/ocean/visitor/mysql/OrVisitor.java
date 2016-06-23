package com.ocean.visitor.mysql;

import com.alibaba.druid.sql.ast.SQLObject;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.visitor.SQLASTOutputVisitor;
import com.alibaba.druid.wall.spi.WallVisitorUtils;
import com.google.common.base.Optional;
import com.ocean.visitor.AbstractOrASTNode;
import com.ocean.visitor.CompositeOrASTNode;
import com.ocean.visitor.SimpleOrASTNode;
import com.ocean.visitor.SqlVisitor;

/**
 * 逻辑OR条件访问器
 */
public class OrVisitor extends AbstractMysqlVisitor {

    private AbstractOrASTNode orASTNode;

    public OrVisitor(final SQLASTOutputVisitor dependencyVisitor) {
        setParameters(dependencyVisitor.getParameters());
        SqlVisitor visitor = (SqlVisitor) dependencyVisitor;
        String currentTableName = null == visitor.getParseContext().getCurrentTable() ? "" : visitor.getParseContext().getCurrentTable().getName();
        getParseContext().setCurrentTable(currentTableName, Optional.<String>absent());
        getParseContext().setShardingColumns(visitor.getParseContext().getShardingColumns());
    }

    /**
     * 进行OR表达式的访问
     * @param sqlObject SQL对象
     * @return OR访问节点
     */
    public Optional<AbstractOrASTNode> visitHandle(final SQLObject sqlObject) {
        reset();
        sqlObject.accept(this);
        postVisitHandle();
        return Optional.fromNullable(orASTNode);
    }

    private void reset() {
        orASTNode = null;
        getParseContext().getCurrentConditionContext().clear();
        getParseContext().setHasOrCondition(false);
    }

    private void postVisitHandle() {
        if (null == orASTNode) {
            return;
        }
        if (!getParseContext().getCurrentConditionContext().isEmpty()) {
            CompositeOrASTNode existingOutConditionOrASTNode = new CompositeOrASTNode();
            existingOutConditionOrASTNode.addSubNode(orASTNode);
            existingOutConditionOrASTNode.addOutConditions(getParseContext().getCurrentConditionContext());
            orASTNode = existingOutConditionOrASTNode;
        }
        orASTNode.createOrASTAsRootNode();
    }

    /**
     * 逻辑OR访问器, 每次只解析一层OR条件
     * @param x 二元表达式
     * @return false 停止访问AST
     */
    @Override
    public boolean visit(final SQLBinaryOpExpr x) {
        if (!SQLBinaryOperator.BooleanOr.equals(x.getOperator())) {
            return super.visit(x);
        }
        if (Boolean.TRUE.equals(WallVisitorUtils.getValue(x))) {
            return false;
        }
        orASTNode = new SimpleOrASTNode(x, new OrVisitor(this));
        return false;
    }
}