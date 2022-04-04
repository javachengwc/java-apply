package com.datastore.mysql.service.impl;

import com.datastore.mysql.dao.mapper.ResourceMapper;
import com.datastore.mysql.model.entity.Resource;
import com.datastore.mysql.model.entity.ResourceExample;
import com.datastore.mysql.service.ResourceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static Logger logger = LoggerFactory.getLogger(ResourceServiceImpl.class);

    public static CountDownLatch latch = new CountDownLatch(1);

    @Autowired
    private ResourceMapper resourceMapper;

    @Override
    public Resource getByName(String name) {
        ResourceExample example= new ResourceExample();
        ResourceExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        List<Resource> list= resourceMapper.selectByExample(example);
        if(list==null || list.size()<=0) {
            return null;
        }
        return list.get(0);
    }

    @Override
    public Resource getById(Integer id) {
        return resourceMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer countAll() {
        ResourceExample example= new ResourceExample();
        return resourceMapper.countByExample(example);
    }

    @Override
    public List<Resource> queryAll() {
        ResourceExample example= new ResourceExample();
        return resourceMapper.selectByExample(example);
    }

    @Transactional
    public boolean uptByName(Resource paramResource) {
        String name = paramResource.getName();
        logger.info("ResourceServiceImpl uptByName start, name={}",name);
        Resource resource = this.getByName(name);
        if(resource==null) {
            logger.warn("ResourceServiceImpl uptByName 找不到名称对应的资源，name={}",name);
            return false;
        }
        Integer id = resource.getId();
        Resource resourceUpt = new Resource();
        resourceUpt.setId(id);
        if(paramResource.getIsShow()!=null) {
            resourceUpt.setIsShow(paramResource.getIsShow());
        }
        if(paramResource.getSort()!=null) {
            resourceUpt.setSort(paramResource.getSort());
        }
        resourceUpt.setModifyTime(new Date());
        logger.info("ResourceServiceImpl uptByName 准备好更新资源,name={},id={}",name,id);
        try {
            latch.await();
        }catch (InterruptedException e) {
            logger.error("ResourceServiceImpl uptByName latch.await() error,",e);
        }
        resourceMapper.updateByPrimaryKeySelective(resourceUpt);
        logger.info("ResourceServiceImpl uptByName end, name={},id={}",name,id);
        return true;
    }

    @Transactional
    public boolean uptShowByName(String name) {
        logger.info("ResourceServiceImpl uptShowByName start, name={}",name);
        Resource resource = this.getByName(name);
        if(resource==null) {
            logger.warn("ResourceServiceImpl uptShowByName 找不到名称对应的资源，name={}",name);
            return false;
        }
        Integer id = resource.getId();
        Resource resourceUpt = new Resource();
        resourceUpt.setId(id);
        resourceUpt.setIsShow(1);
        resourceUpt.setModifyTime(new Date());
        logger.info("ResourceServiceImpl uptShowByName 准备好更新资源,name={},id={}",name,id);
        try {
            latch.await();
        }catch (InterruptedException e) {
            logger.error("ResourceServiceImpl uptShowByName latch.await() error,",e);
        }
        resourceMapper.updateByPrimaryKeySelective(resourceUpt);
        logger.info("ResourceServiceImpl uptShowByName end, name={},id={}",name,id);
        return true;
    }
}
