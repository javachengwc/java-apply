package com.mountain.manage.service.serve;

import com.mountain.manage.model.vo.DimenVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 服务维度服务类
 */
@Service
public class ServeService {

    private static Logger logger = LoggerFactory.getLogger(ServeService.class);

    @Autowired
    private ZookeeperService zookeeperService;


    public List<DimenVo> queryList()
    {
        Set<String> services = zookeeperService.getServices();
        int count = services==null?0:services.size();
        logger.info("MachineService queryList service count="+count);
        if(count==0)
        {
            return Collections.emptyList();
        }
        List<DimenVo> list = new ArrayList<DimenVo>();
        for(String service:services)
        {
            DimenVo vo = new DimenVo();
            vo.setName(service);
            vo.setType(2);
            list.add(vo);
        }
        return list;
    }

    public List<DimenVo> testData()
    {
        List<DimenVo> list = new ArrayList<DimenVo>();
        DimenVo vo = new DimenVo();
        vo.setName("TestService");
        vo.setType(2);

        DimenVo vo2 = new DimenVo();
        vo2.setName("TestService2");
        vo2.setType(2);

        list.add(vo);
        list.add(vo2);


        return list;
    }
}
