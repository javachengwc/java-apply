package com.solr7.search.service;

import com.solr7.search.model.SearchVo;
import com.util.page.Page;

import java.util.Map;

public interface SearchService {

    public Page<Map<String,Object>> queryPage(SearchVo searchVo);

    public int count(SearchVo searchVo);
}
