package com.ocean.visitor;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.wall.spi.WallVisitorUtils;
import com.google.common.base.Optional;
import com.ocean.visitor.mysql.OrVisitor;

/**
 * 只包含OR的节点
 */
public class SimpleOrASTNode extends AbstractOrASTNode {

    private SQLBinaryOpExpr canSplitExpr;

    private OrVisitor orVisitor;

    public SimpleOrASTNode()
    {

    }

    public SimpleOrASTNode(SQLBinaryOpExpr canSplitExpr,OrVisitor orVisitor)
    {
        this.canSplitExpr=canSplitExpr;
        this.orVisitor=orVisitor;
    }

    public SQLBinaryOpExpr getCanSplitExpr() {
        return canSplitExpr;
    }

    public void setCanSplitExpr(SQLBinaryOpExpr canSplitExpr) {
        this.canSplitExpr = canSplitExpr;
    }

    public OrVisitor getOrVisitor() {
        return orVisitor;
    }

    public void setOrVisitor(OrVisitor orVisitor) {
        this.orVisitor = orVisitor;
    }

    @Override
    public void createOrASTAsRootNode() {
        if (SQLBinaryOperator.BooleanOr == canSplitExpr.getOperator()) {
            parseExprIfNotFalse(canSplitExpr.getRight());
            if (canSplitExpr.getLeft() instanceof SQLBinaryOpExpr) {
                canSplitExpr = (SQLBinaryOpExpr) canSplitExpr.getLeft();
                createOrASTAsRootNode();
            } else {
                finishParseThisNode(canSplitExpr.getLeft());
            }
        } else {
            finishParseThisNode(canSplitExpr);
        }
    }

    private void finishParseThisNode(final SQLExpr expr) {
        parseExprIfNotFalse(expr);
        mergeSubConditions();
    }

    private void parseExprIfNotFalse(final SQLExpr expr) {
        if (Boolean.FALSE.equals(WallVisitorUtils.getValue(expr))) {
            return;
        }
        Optional<AbstractOrASTNode> subNode = orVisitor.visitHandle(expr);
        if (subNode.isPresent()) {
            addSubNode(subNode.get());
        } else {
            addNestedConditions(orVisitor.getParseContext().getCurrentConditionContext());
        }
    }
}