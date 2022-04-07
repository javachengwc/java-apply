package com.manageplat.dao.tongyong.impl;

import com.manageplat.dao.tongyong.EntityItemDao;
import com.manageplat.model.mapper.TyEntityItemMapper;
import com.manageplat.model.pojo.TyEntityItem;
import com.manageplat.model.pojo.TyEntityItemExample;
import com.manageplat.model.vo.tongyong.TyEntityItemVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通用列表实体列项访问实现类
 */
@Repository
public class EntityItemDaoImpl implements EntityItemDao{

    private static Logger logger = LoggerFactory.getLogger(EntityItemDaoImpl.class);

    @Autowired
    private TyEntityItemMapper tyEntityItemMapper;

    public List<TyEntityItem> queryPage(TyEntityItemVo entityItem,int start,int pageSize)
    {
        TyEntityItemExample example = new TyEntityItemExample();
        if(entityItem!=null)
        {
            TyEntityItemExample.Criteria criteria = example.createCriteria();
            if(entityItem.getEntityId()!=null)
            {
                criteria.andEntityIdEqualTo(entityItem.getEntityId());
            }

            if(!StringUtils.isBlank(entityItem.getItemName()))
            {
                criteria.andItemNameLike(entityItem.getItemName());
            }
            if(!StringUtils.isBlank(entityItem.getItemCol()))
            {
                criteria.andItemColLike(entityItem.getItemCol());
            }
        }

        example.setOrderByClause(" id desc limit "+start+","+pageSize+" ");
        return tyEntityItemMapper.selectByExample(example);
    }

    public int count(TyEntityItemVo entityItem)
    {

        TyEntityItemExample example = new TyEntityItemExample();
        if(entityItem!=null)
        {
            TyEntityItemExample.Criteria criteria = example.createCriteria();
            if(entityItem.getEntityId()!=null)
            {
                criteria.andEntityIdEqualTo(entityItem.getEntityId());
            }

            if(!StringUtils.isBlank(entityItem.getItemName()))
            {
                criteria.andItemNameLike(entityItem.getItemName());
            }
            if(!StringUtils.isBlank(entityItem.getItemCol()))
            {
                criteria.andItemColLike(entityItem.getItemCol());
            }
        }

        return tyEntityItemMapper.countByExample(example);
    }
}