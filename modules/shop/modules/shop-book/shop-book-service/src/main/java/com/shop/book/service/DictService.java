package com.shop.book.service;


import com.shop.book.model.pojo.Dict;

import java.util.List;

public interface DictService {

    public List<Dict> queryAll();

    public Dict getByKey(String dictKey);

    public List<Dict> queryByType(Integer type,Integer isUse);
}
