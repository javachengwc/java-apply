package com.mountain.manage.service.serve;

import com.mountain.manage.model.vo.DimenVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 机器维度服务类
 */
@Service
public class MachineService {

    public List<DimenVo> queryList()
    {
        return testData();
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
