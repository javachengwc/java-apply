package com.manageplat.dao.tongyong.impl;

import com.manageplat.dao.tongyong.EntityDao;
import com.manageplat.model.mapper.TyEntityMapper;
import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.pojo.TyEntityExample;
import com.manageplat.model.vo.tongyong.TyEntityVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通用列表实体访问实现类
 */
@Repository
public class EntityDaoImpl implements EntityDao {

    private static Logger logger = LoggerFactory.getLogger(EntityDaoImpl.class);

    @Autowired
    private TyEntityMapper tyEntityMapper;


    public List<TyEntity> queryPage(TyEntityVo entity,int start,int pageSize)
    {
        TyEntityExample example = new TyEntityExample();
        if(entity!=null)
        {
            TyEntityExample.Criteria criteria = example.createCriteria();
            if(entity.getId()!=null)
            {
                criteria.andIdEqualTo(entity.getId());
            }
            if(!StringUtils.isBlank(entity.getName()))
            {
                criteria.andNameLike(entity.getName());
            }
            if(!StringUtils.isBlank(entity.getTableName()))
            {
                criteria.andTableNameLike(entity.getTableName());
            }
        }
        example.setOrderByClause(" id desc limit "+start+","+pageSize+" ");
        return tyEntityMapper.selectByExample(example);
    }

    public int count(TyEntityVo entity)
    {
        TyEntityExample example = new TyEntityExample();
        if(entity!=null)
        {
            TyEntityExample.Criteria criteria = example.createCriteria();
            if(entity.getId()!=null)
            {
                criteria.andIdEqualTo(entity.getId());
            }
            if(!StringUtils.isBlank(entity.getName()))
            {
                criteria.andNameLike(entity.getName());
            }
            if(!StringUtils.isBlank(entity.getTableName()))
            {
                criteria.andTableNameLike(entity.getTableName());
            }
        }
        return tyEntityMapper.countByExample(example);
    }
}
