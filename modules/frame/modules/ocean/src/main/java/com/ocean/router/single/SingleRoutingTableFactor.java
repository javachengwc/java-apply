package com.ocean.router.single;

import com.ocean.parser.SqlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 单表路由表单元
 */
public class SingleRoutingTableFactor {

    private static Logger logger = LoggerFactory.getLogger(SingleRoutingTableFactor.class);

    //逻辑表名
    private String logicTable;

    //实际表名
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
        logger.info("SingleRoutingTableFactor buildSQL, logicTable="+logicTable+",actualTable="+actualTable+".................." );
    }
}
