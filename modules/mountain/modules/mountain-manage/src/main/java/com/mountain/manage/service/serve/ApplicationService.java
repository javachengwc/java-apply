package com.mountain.manage.service.serve;

import com.mountain.manage.model.vo.DimenVo;
import com.util.BeanCopyUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务维度服务类
 */
@Service
public class ApplicationService {

    public List<DimenVo> queryList()
    {
       return testData();
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
