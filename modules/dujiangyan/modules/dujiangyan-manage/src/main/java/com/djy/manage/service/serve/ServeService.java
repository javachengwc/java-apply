package com.djy.manage.service.serve;

import com.djy.manage.model.vo.DimenVo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务维度服务类
 */
@Service
public class ServeService {

    public List<DimenVo> queryList()
    {
        return testData();
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
