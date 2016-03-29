package com.other.spi.search;

import java.util.List;
import java.util.Map;

/**
 * 搜索接口
 */
public interface Search {

    List<Map<String,Object>> search(String keyword);
}
