package com.manageplat.service.tongyong;

import com.manageplat.dao.tongyong.EntityCdnDao;
import com.manageplat.model.mapper.TyEntityCdnMapper;
import com.manageplat.model.pojo.TyEntityCdn;
import com.manageplat.model.pojo.TyEntityCdnExample;
import com.manageplat.model.vo.tongyong.TyEntityCdnVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 通用列表实体条件服务类
 */
@Service
public class EntityCdnService {

    private static Logger logger = LoggerFactory.getLogger(EntityCdnService.class);

    @Autowired
    private EntityCdnDao entityCdnDao;

    @Autowired
    private TyEntityCdnMapper tyEntityCdnMapper;


    public List<TyEntityCdn> queryPage(TyEntityCdnVo entityCdn,int start,int pageSize)
    {
        return entityCdnDao.queryPage(entityCdn,start,pageSize);
    }

    public int count(TyEntityCdnVo entity)
    {
        return entityCdnDao.count(entity);
    }

    public List<TyEntityCdn> queryEntityCdn(Integer entityId)
    {

        if(entityId==null)
        {
            return Collections.EMPTY_LIST;
        }
        TyEntityCdnExample example = new TyEntityCdnExample();
        TyEntityCdnExample.Criteria criteria= example.createCriteria();
        criteria.andEntityIdEqualTo(entityId);
        example.setOrderByClause(" sort asc");
        return tyEntityCdnMapper.selectByExample(example);
    }
}
