package com.ocean.merger.groupby;

import java.util.ArrayList;
import java.util.List;

/**
 * 分组结果集数据存储索引
 */
public class GroupByKey {

    private List<String> unionKey = new ArrayList<String>();

    public List<String> getUnionKey() {
        return unionKey;
    }

    public void setUnionKey(List<String> unionKey) {
        this.unionKey = unionKey;
    }

    public void append(String key) {
        unionKey.add(key);
    }


}
