package com.ocean.router.single;

import com.ocean.parser.SqlBuilder;

/**
 * 单表路由表单元
 */
public class SingleRoutingTableFactor {

    private String logicTable;

    private String actualTable;

    public SingleRoutingTableFactor()
    {

    }

    public SingleRoutingTableFactor(String logicTable,String actualTable)
    {
        this.logicTable =logicTable;
        this.actualTable=actualTable;
    }

    public String getLogicTable() {
        return logicTable;
    }

    public void setLogicTable(String logicTable) {
        this.logicTable = logicTable;
    }

    public String getActualTable() {
        return actualTable;
    }

    public void setActualTable(String actualTable) {
        this.actualTable = actualTable;
    }

    /**
     * 构建SQL
     */
    public void buildSQL(SqlBuilder builder) {
        builder.buildSQL(logicTable, actualTable);
    }
}
