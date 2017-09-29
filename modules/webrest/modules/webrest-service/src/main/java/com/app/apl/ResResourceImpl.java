package com.app.apl;

import com.app.annotation.RestService;
import com.app.api.rest.ResResource;
import com.app.entity.pojo.SysResource;
import com.app.model.Resource;
import com.app.service.ResourceService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
@RestService
public class ResResourceImpl implements ResResource{

    private static Logger logger = LoggerFactory.getLogger(ResResourceImpl.class);

    @Autowired
    private ResourceService resourceService;

    public Resource getResource(Integer id) {
        logger.info("ResResourceImpl getResource start ,id={}",id);
        if(id==null) { return null ;}
        SysResource resource =resourceService.getById(id);
        if(resource==null) { return null;}
        Resource rt = new Resource();
        BeanUtils.copyProperties(resource,rt);
        return rt;
    }

    public Resource queryByName(String name) {
        logger.info("ResResourceImpl queryByName start ,name={}",name);
        if(StringUtils.isBlank(name)) { return null ;}
        SysResource resource =resourceService.queryByName(name);
        if(resource==null) { return null;}
        Resource rt = new Resource();
        BeanUtils.copyProperties(resource,rt);
        return rt;
    }
}
