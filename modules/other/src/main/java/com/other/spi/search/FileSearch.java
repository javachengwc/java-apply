package com.other.spi.search;

import java.util.List;
import java.util.Map;

/**
 * 文件搜索
 */
public class FileSearch implements Search {

    @Override
    public List<Map<String,Object>> search(String keyword) {

        System.out.println("file search. keyword:" + keyword);
        return null;
    }
}
