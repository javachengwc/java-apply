package com.manageplat.dao.tongyong.impl;

import com.manageplat.dao.tongyong.EntityCdnDao;
import com.manageplat.model.mapper.TyEntityCdnMapper;
import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.pojo.TyEntityCdnExample;
import com.manageplat.model.vo.tongyong.TyEntityCdnVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 通用列表实体条件访问实现类
 */
@Repository
public class EntityCdnDaoImpl implements EntityCdnDao {

    private static Logger logger = LoggerFactory.getLogger(EntityCdnDaoImpl.class);

    @Autowired
    private TyEntityCdnMapper tyEntityCdnMapper;

    public List<TyEntityCdn> queryPage(TyEntityCdnVo entityCdn,int start,int pageSize)
    {
        TyEntityCdnExample example = new TyEntityCdnExample();
        if(entityCdn!=null)
        {
            TyEntityCdnExample.Criteria criteria = example.createCriteria();
            if(entityCdn.getEntityId()!=null)
            {
                criteria.andEntityIdEqualTo(entityCdn.getEntityId());
            }

            if(!StringUtils.isBlank(entityCdn.getCdnName()))
            {
                criteria.andCdnNameLike(entityCdn.getCdnName());
            }
            if(!StringUtils.isBlank(entityCdn.getCdnCol()))
            {
                criteria.andCdnColLike(entityCdn.getCdnCol());
            }
        }

        example.setOrderByClause(" id desc limit "+start+","+pageSize+" ");
        return tyEntityCdnMapper.selectByExample(example);
    }

    public int count(TyEntityCdnVo entityCdn)
    {

        TyEntityCdnExample example = new TyEntityCdnExample();
        if(entityCdn!=null)
        {
            TyEntityCdnExample.Criteria criteria = example.createCriteria();
            if(entityCdn.getEntityId()!=null)
            {
                criteria.andEntityIdEqualTo(entityCdn.getEntityId());
            }

            if(!StringUtils.isBlank(entityCdn.getCdnName()))
            {
                criteria.andCdnNameLike(entityCdn.getCdnName());
            }
            if(!StringUtils.isBlank(entityCdn.getCdnCol()))
            {
                criteria.andCdnColLike(entityCdn.getCdnCol());
            }
        }

        return tyEntityCdnMapper.countByExample(example);
    }
}
