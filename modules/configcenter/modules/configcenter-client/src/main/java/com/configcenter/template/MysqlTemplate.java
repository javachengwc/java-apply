package com.configcenter.template;

import java.util.ArrayList;
import java.util.List;

/**
 * mysql模板
 */
public class MysqlTemplate extends BaseTemplate{

    private List<MysqlDb> dbs = new ArrayList<MysqlDb>();

    public List<MysqlDb> getDbs() {
        return dbs;
    }

    public void setDbs(List<MysqlDb> dbs) {
        this.dbs = dbs;
    }
}
