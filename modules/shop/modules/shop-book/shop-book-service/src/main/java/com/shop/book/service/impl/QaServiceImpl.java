package com.shop.book.service.impl;

import com.shop.book.annotation.DataCache;
import com.shop.book.dao.mapper.QaMapper;
import com.shop.book.model.pojo.Qa;
import com.shop.book.model.pojo.QaExample;
import com.shop.book.service.QaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class QaServiceImpl implements QaService {

    private static Logger logger= LoggerFactory.getLogger(QaServiceImpl.class);

    @Autowired
    private QaMapper qaMapper;

    @DataCache
    public List<Qa> queryAll() {
        QaExample example= new QaExample();
        example.setOrderByClause(" sort asc ");
        List<Qa> list = qaMapper.selectByExample(example);
        int cnt = list==null?0:list.size();
        logger.info("QaServiceImpl queryAll cnt={}",cnt);
        if(list==null) {
            return Collections.EMPTY_LIST;
        }
        return list;
    }

    public Integer addQa(Qa qa) {
        //insertSelective只录入有值的字段，没值得字段会用表sql定义的默认值填充
        int rt  = qaMapper.insertSelective(qa);
        return rt;
    }
}
