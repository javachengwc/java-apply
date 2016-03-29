package com.manageplat.service.tongyong;

import com.manageplat.dao.tongyong.EntityItemDao;
import com.manageplat.model.mapper.TyEntityItemMapper;
import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.pojo.TyEntityCdnExample;
import com.manageplat.model.pojo.TyEntityItem;
import com.manageplat.model.pojo.TyEntityItemExample;
import com.manageplat.model.vo.tongyong.TyEntityItemVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 通用列表实体列项服务类
 */
@Service
public class EntityItemService {

    private static Logger logger = LoggerFactory.getLogger(EntityItemService.class);

    @Autowired
    private EntityItemDao entityItemDao;

    @Autowired
    private TyEntityItemMapper tyEntityItemMapper;

    public List<TyEntityItem> queryPage(TyEntityItemVo entityItem,int start,int pageSize)
    {
        return entityItemDao.queryPage(entityItem,start,pageSize);
    }

    public int count(TyEntityItemVo entityItem)
    {
        return entityItemDao.count(entityItem);
    }

    public List<TyEntityItem> queryEntityItem(Integer entityId)
    {

        if(entityId==null)
        {
            return Collections.EMPTY_LIST;
        }
        TyEntityItemExample example = new TyEntityItemExample();
        TyEntityItemExample.Criteria criteria= example.createCriteria();
        criteria.andEntityIdEqualTo(entityId);
        example.setOrderByClause(" sort asc");
        return tyEntityItemMapper.selectByExample(example);
    }
}
