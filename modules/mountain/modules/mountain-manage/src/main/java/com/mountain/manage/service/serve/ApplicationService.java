package com.mountain.manage.service.serve;

import com.mountain.manage.model.vo.DimenVo;
import com.util.BeanCopyUtil;
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
public class ApplicationService {

    private static Logger logger = LoggerFactory.getLogger(ApplicationService.class);

    @Autowired
    private ZookeeperService zookeeperService;

    public List<DimenVo> queryList()
    {
        Set<String> apps = zookeeperService.getApplications();
        int count = apps==null?0:apps.size();
        logger.info("MachineService queryList application count="+count);
        if(count==0)
        {
            return Collections.emptyList();
        }
        List<DimenVo> list = new ArrayList<DimenVo>();
        for(String app:apps)
        {
            DimenVo vo = new DimenVo();
            vo.setName(app);
            vo.setType(1);
            list.add(vo);
        }
        return list;
    }

    public List<DimenVo> testData()
    {
        List<DimenVo> list = new ArrayList<DimenVo>();
        DimenVo vo = new DimenVo();
        vo.setName("TestApp");
        vo.setType(1);

        DimenVo vo2 = new DimenVo();
        vo2.setName("TestApp2");
        vo2.setType(1);

        for(int i=0;i<100;i++) {
            DimenVo vo3 = new DimenVo();
            BeanCopyUtil.copyProperties(vo3, vo2);
            vo3.setName("TestApp"+(i+3));
            list.add(vo3);
        }

        list.add(vo);
        list.add(vo2);
        return list;
    }

}
