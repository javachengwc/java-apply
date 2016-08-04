package com.djy.manage.service.serve;

import com.djy.manage.model.vo.QueryVo;
import com.djy.manage.model.vo.ServiceVo;
import com.util.BeanCopyUtil;
import com.util.page.CollectionPage;
import com.util.page.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 消费服务的服务类
 */
@Service
public class ConsumerService {

    public Page<ServiceVo> queryList(QueryVo queryVo)
    {
        List<ServiceVo> list = testData();
        Page<ServiceVo> page = new CollectionPage<ServiceVo>(list,queryVo.getPage(),queryVo.getRows());
        return page;
    }

    public List<ServiceVo> testData()
    {
        List<ServiceVo> list = new ArrayList<ServiceVo>();
        ServiceVo vo = new ServiceVo();
        vo.setId(1);
        vo.setPid(6677);
        vo.setService("tt-cc-ww-server:1.0");
        vo.setApplication("consumer-cc-ww-app");
        vo.setUrl("consumer://172.0.0.1/tt-cc-ww-server?application=consumer-cc-ww-app&category=consumers&pid=6677&timeout=60000&timestamp=1469679206966&version=1.0");
        vo.setUseable(true);
        vo.setAddress("172.0.0.1");
        vo.setParameters("application=consumer-cc-ww-app&category=consumers&timeout=60000&pid=6677&timestamp=1469679206966&version=1.0");

        ServiceVo vo2 = new ServiceVo();
        vo2.setId(1);
        vo2.setPid(7788);
        vo2.setService("tt-aa-bb-server:1.1");
        vo2.setApplication("consumer-aa-bb-app");
        vo2.setUrl("consumer://172.0.0.2/tt-aa-bb-server?application=consumer-aa-bb-app&category=consumers&pid=7788&timeout=30000&timestamp=1459679206966&version=1.1");
        vo2.setUseable(false);
        vo2.setAddress("172.0.0.2");
        vo2.setParameters("application=consumer-aa-bb-app&category=consumers&timeout=30000&pid=7788&timestamp=1459679206966&version=1.1");
        vo2.setInvokeFailCnt(0l);
        vo2.setInvokeFailTotalCnt(0l);
        vo2.setInvokeSuccessCnt(0l);
        vo2.setInvokeSucTotalCnt(0l);

        list.add(vo);
        list.add(vo2);

        for(int i=0;i<20;i++) {
            ServiceVo vo3 = new ServiceVo();
            BeanCopyUtil.copyProperties(vo3, vo2);
            vo3.setId(i+3);
            list.add(vo3);
        }
        return list;
    }
}
