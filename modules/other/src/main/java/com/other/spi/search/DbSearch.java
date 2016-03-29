package com.other.spi.search;

import java.util.List;
import java.util.Map;

/**
 * 数据库搜索
 */
public class DbSearch implements Search {

    @Override
    public List<Map<String,Object>> search(String keyword) {

        System.out.println("db search. keyword:" + keyword);
        return null;
    }
}
