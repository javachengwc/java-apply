package com.druid.service.impl;

import com.druid.dao.mapper.EntityMapper;
import com.druid.model.pojo.Entity;
import com.druid.model.pojo.EntityExample;
import com.druid.service.EntityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;

@Service
public class EntityServiceImpl implements EntityService  {

    private static Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<Entity> queryByName(String name) {
        EntityExample example = new EntityExample();
        example.createCriteria().andNameEqualTo(name);
        List<Entity> list = entityMapper.selectByExample(example);
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Entity addEntity(String name) {
        logger.info("EntityServiceImpl addEntity start,name="+name);
        Entity entity = new Entity();
        entity.setName(name);
        entityMapper.insertSelective(entity);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            public void suspend() { }
            public void resume() { }
            public void flush() { }
            public void beforeCommit(boolean b) { }
            public void beforeCompletion() { }
            public void afterCompletion(int i) {
                try {
                    logger.info("EntityServiceImpl transactionSynchronization afterCompletion done");
                } catch (Exception ee) {
                    logger.error("EntityServiceImpl transactionSynchronization afterCompletion error,",ee);
                }
            }
            public void afterCommit() { }
        });

        logger.info("EntityServiceImpl addEntity end,name="+name);
        return  entity;
    }
}
