package com.druid.service.impl;

import com.druid.dao.mapper.EntityMapper;
import com.druid.model.pojo.Entity;
import com.druid.model.pojo.EntityExample;
import com.druid.model.pojo.Log;
import com.druid.service.EntityExtService;
import com.druid.service.EntityService;
import com.druid.service.LogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Date;
import java.util.List;

@Service
public class EntityServiceImpl implements EntityService  {

    private static Logger logger = LoggerFactory.getLogger(EntityServiceImpl.class);

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private EntityExtService entityExtService;

    @Autowired
    private LogService logService;

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
        Date now = new Date();
        Entity entity = new Entity();
        entity.setName(name);
        entity.setCreteTime(now);
        entity.setUpdateTime(now);
        entityMapper.insertSelective(entity);

        entityExtService.addEntityExt(entity,name);


        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            public void suspend() { }
            public void resume() { }
            public void flush() { }
            public void beforeCommit(boolean b) { }
            public void beforeCompletion() { }
            public void afterCompletion(int i) {
                try {
                    logger.info("EntityServiceImpl transactionSynchronization afterCompletion done");
                    Log log = EntityServiceImpl.this.tipLog(entity);
                    logService.recordLog(log);
                } catch (Exception ee) {
                    logger.error("EntityServiceImpl transactionSynchronization afterCompletion error,",ee);
                }
            }
            public void afterCommit() { }
        });

        logger.info("EntityServiceImpl addEntity end,name="+name);
        return  entity;
    }

    private Log tipLog(Entity entity) {
        Date now = new Date();
        Log log = new Log();
        log.setBiz("entity");
        log.setRelaKey(""+entity.getId());
        log.setContent("添加entity->"+entity.getName());
        log.setCreteTime(now);
        log.setUpdateTime(now);
        return log;
    }
}
