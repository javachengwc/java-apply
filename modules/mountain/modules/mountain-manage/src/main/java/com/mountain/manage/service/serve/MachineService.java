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
 * 机器维度服务类
 */
@Service
public class MachineService {

    private static Logger logger = LoggerFactory.getLogger(MachineService.class);

    @Autowired
    private ZookeeperService zookeeperService;

    public List<DimenVo> queryList()
    {
        Set<String> machines = zookeeperService.getMachines();
        int count = machines==null?0:machines.size();
        logger.info("MachineService queryList machine count="+count);
        if(count==0)
        {
            return Collections.emptyList();
        }
        List<DimenVo> list = new ArrayList<DimenVo>();
        for(String machine:machines)
        {
            DimenVo vo = new DimenVo();
            vo.setName(machine);
            vo.setType(3);
            list.add(vo);
        }
        return list;
    }

    public List<DimenVo> testData()
    {
        List<DimenVo> list = new ArrayList<DimenVo>();
        DimenVo vo = new DimenVo();
        vo.setName("127.0.0.1");
        vo.setType(3);

        DimenVo vo2 = new DimenVo();
        vo2.setName("127.0.0.2");
        vo2.setType(3);

        list.add(vo);
        list.add(vo2);
        return list;
    }
}
