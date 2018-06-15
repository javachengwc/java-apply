package com.esearch6.service;

import com.esearch6.model.SearchVo;
import com.util.page.Page;

import java.util.Map;

public interface SearchService {

    public Page<Map<String,Object>> queryPage(SearchVo searchVo);

    public int count(SearchVo searchVo);
}
