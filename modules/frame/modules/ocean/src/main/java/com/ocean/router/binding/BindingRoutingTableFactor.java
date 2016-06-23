package com.ocean.router.binding;

import com.ocean.parser.SqlBuilder;
import com.ocean.router.single.SingleRoutingTableFactor;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Binding表路由表单元
 */
public class BindingRoutingTableFactor extends SingleRoutingTableFactor {

    private Collection<BindingRoutingTableFactor> bindingRoutingTableFactors = new ArrayList<BindingRoutingTableFactor>();

    public BindingRoutingTableFactor(final String logicTable, final String actualTable) {
        super(logicTable, actualTable);
    }

    public Collection<BindingRoutingTableFactor> getBindingRoutingTableFactors() {
        return bindingRoutingTableFactors;
    }

    public void setBindingRoutingTableFactors(Collection<BindingRoutingTableFactor> bindingRoutingTableFactors) {
        this.bindingRoutingTableFactors = bindingRoutingTableFactors;
    }

    @Override
    public void buildSQL(SqlBuilder builder) {
        super.buildSQL(builder);
        for (BindingRoutingTableFactor each : bindingRoutingTableFactors) {
            each.buildSQL(builder);
        }
    }
}
