package com.druid.service.impl;

import com.druid.dao.mapper.EntityExtMapper;
import com.druid.model.pojo.Entity;
import com.druid.model.pojo.EntityExt;
import com.druid.service.EntityExtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class EntityExtServiceImpl implements EntityExtService {

    @Autowired
    private EntityExtMapper entityExtMapper;

    @Override
    @Transactional(propagation= Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public EntityExt addEntityExt(Entity entity, String ext) {
        Date now = new Date();
        EntityExt entityExt = new EntityExt();
        entityExt.setEntityId(entity.getId());
        entityExt.setExt(ext);
        entityExt.setCreteTime(now);
        entityExt.setUpdateTime(now);
        entityExtMapper.insertSelective(entityExt);
        return entityExt;
    }
}
