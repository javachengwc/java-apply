package com.manageplat.service.tongyong;

import com.manageplat.dao.tongyong.EntityDao;
import com.manageplat.model.mapper.TyEntityMapper;
import com.manageplat.model.pojo.TyEntity;
import com.manageplat.model.vo.tongyong.TyEntityVo;
import org.apache.xmlbeans.impl.piccolo.xml.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 通用列表实体服务类
 */
@Service
public class EntityService {

    private static Logger logger = LoggerFactory.getLogger(EntityService.class);

    @Autowired
    private EntityDao entityDao;

    @Autowired
    private TyEntityMapper tyEntityMapper;

    public List<TyEntity> queryPage(TyEntityVo entity,int start,int pageSize)
    {
        return entityDao.queryPage(entity,start,pageSize);
    }

    public int count(TyEntityVo entity)
    {
        return entityDao.count(entity);
    }

    public TyEntity getById(Integer id)
    {
        if(id==null)
        {
            return null;
        }
        return  tyEntityMapper.selectByPrimaryKey(id);
    }
}
