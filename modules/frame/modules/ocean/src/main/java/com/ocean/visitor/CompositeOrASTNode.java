package com.ocean.visitor;

import com.ocean.parser.ConditionContext;
import com.ocean.parser.SqlCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * 存在外层条件的节点
 */
public class CompositeOrASTNode extends AbstractOrASTNode {

    private List<SqlCondition> outConditions = new ArrayList<SqlCondition>();

    public void addOutConditions(ConditionContext outConditions) {
        this.outConditions.addAll(outConditions.getAllConditions());
    }

    @Override
    public void createOrASTAsRootNode() {
        for (AbstractOrASTNode each : getSubNodes()) {
            each.createOrASTAsRootNode();
        }
        mergeSubConditions();
        for (List<SqlCondition> each : getNestedConditions()) {
            each.addAll(outConditions);
        }
    }
}