package com.shop.book.service.impl;

import com.shop.book.annotation.DataCache;
import com.shop.book.dao.mapper.DictMapper;
import com.shop.book.model.pojo.Dict;
import com.shop.book.model.pojo.DictExample;
import com.shop.book.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictMapper dictMapper;

    @DataCache
    public List<Dict> queryAll() {
        DictExample example = new DictExample();
        List<Dict> list =dictMapper.selectByExample(example);
        if(list==null) {
            list = Collections.emptyList();
        }
        return list;
    }

    public Dict getByKey(String dictKey) {
        DictExample example = new DictExample();
        DictExample.Criteria criteria = example.createCriteria();
        criteria.andDictKeyEqualTo(dictKey);
        List<Dict> list =dictMapper.selectByExample(example);
        if(list!=null && list.size()>0) {
            return  list.get(0);
        }
        return null;
    }

    @DataCache
    public List<Dict> queryByType(Integer type,Integer isUse) {
        DictExample example = new DictExample();
        DictExample.Criteria criteria = example.createCriteria();
        criteria.andDictTypeEqualTo(type);
        if(isUse!=null) {
            criteria.andIsUseEqualTo(isUse);
        }
        List<Dict> list =dictMapper.selectByExample(example);
        if(list==null) {
            list = Collections.emptyList();
        }
        return list;
    }
}
