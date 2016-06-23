package com.ocean.visitor;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.ocean.parser.ConditionContext;
import com.ocean.parser.SqlCondition;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象的OR语法树节点
 */
public abstract class AbstractOrASTNode {

    private List<AbstractOrASTNode> subNodes = new ArrayList<AbstractOrASTNode>();

    private List<List<SqlCondition>> nestedConditions = new ArrayList<List<SqlCondition>>();

    public List<AbstractOrASTNode> getSubNodes() {
        return subNodes;
    }

    public void setSubNodes(List<AbstractOrASTNode> subNodes) {
        this.subNodes = subNodes;
    }

    public List<List<SqlCondition>> getNestedConditions() {
        return nestedConditions;
    }

    public void setNestedConditions(List<List<SqlCondition>> nestedConditions) {
        this.nestedConditions = nestedConditions;
    }

    public void addSubNode(final AbstractOrASTNode node) {
        subNodes.add(node);
    }

    protected final void addNestedConditions(final ConditionContext conditionContext) {
        nestedConditions.add(Lists.newArrayList(conditionContext.getAllConditions()));
    }

    /**
     * 使用该节点作为根节点生成抽象语法树.
     * 使用深度优先后续的方式生成语法树.
     * 其中后续遍历是由于DRUID进行SQL语法解析时产生的行为
     */
    public abstract void createOrASTAsRootNode();

    /**
     * 获取解析结果需要的条件
     */
    public final List<ConditionContext> getCondition() {
        return Lists.transform(nestedConditions, new Function<List<SqlCondition>, ConditionContext>() {

            @Override
            public ConditionContext apply(final List<SqlCondition> input) {
                ConditionContext result = new ConditionContext();
                for (SqlCondition each : input) {
                    result.add(each);
                }
                return result;
            }
        });
    }

    /**
     * 多个子节点之间做笛卡尔积.
     */
    protected final void mergeSubConditions() {
        if (subNodes.isEmpty()) {
            return;
        }
        List<List<SqlCondition>> result = new ArrayList<List<SqlCondition>>();
        result.addAll(subNodes.get(0).getNestedConditions());
        for (int i = 1; i < subNodes.size(); i++) {
            result = cartesianNestedConditions(result, subNodes.get(i).getNestedConditions());
        }
        nestedConditions.addAll(result);
    }

    private List<List<SqlCondition>> cartesianNestedConditions(final List<List<SqlCondition>> oneNestedConditions, final List<List<SqlCondition>> anotherNestedConditions) {
        List<List<SqlCondition>> result = new ArrayList<List<SqlCondition>>();
        for (List<SqlCondition> oneNestedCondition : oneNestedConditions) {
            for (List<SqlCondition> anotherNestedCondition : anotherNestedConditions) {
                List<SqlCondition> mergedConditions = new ArrayList<SqlCondition>();
                mergedConditions.addAll(oneNestedCondition);
                mergedConditions.addAll(anotherNestedCondition);
                result.add(mergedConditions);
            }
        }
        return result;
    }
}
