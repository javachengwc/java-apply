package com.mountain.manage.service.serve;

import com.mountain.constant.Constant;
import com.mountain.manage.model.vo.QueryVo;
import com.mountain.manage.model.vo.ServiceVo;
import com.mountain.manage.util.BizFilterTransUtil;
import com.mountain.model.SpecUrl;
import com.util.enh.BeanCopyUtil;
import com.util.page.CollectionPage;
import com.util.page.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * 消费服务的服务类
 */
@Service
public class ConsumerService {

    private static Logger logger = LoggerFactory.getLogger(ConsumerService.class);

    @Autowired
    private ZookeeperService zookeeperService;

    public Page<ServiceVo> queryList(QueryVo queryVo)
    {
        logger.info("ConsumerService queryList doing ..................");
        queryVo.setCategory(Constant.CONSUMERS_CATEGORY);
        Map<String, ConcurrentMap<String, Map<Long, SpecUrl>>> cacheMap =zookeeperService.getRegistryCache();
        List<ServiceVo> list = new ArrayList<ServiceVo>();
        Page<ServiceVo> emptyPage=new CollectionPage<ServiceVo>(list,queryVo.getPage(),queryVo.getRows());
        if(cacheMap==null || cacheMap.size()<=0) {
            return emptyPage;
        }
        String category= queryVo.getCategory();
        //根据category进行过滤
        Map<String, Map<Long, SpecUrl>> data =cacheMap.get(category);
        if(data==null || data.size()<=0)
        {
            return emptyPage;
        }
        Map<Long, SpecUrl> resultMap =new HashMap<Long, SpecUrl>();
        Integer state = (queryVo.getState()==null)?0:queryVo.getState();
        String keyword =queryVo.getQueryValue();
        String application="";
        String machine="";
        String assignService="";
        String demenType=queryVo.getDimenType();
        if(!StringUtils.isBlank(demenType))
        {
            if("application".equals(demenType) && !StringUtils.isBlank(queryVo.getAssignValue()))
            {
                application=queryVo.getAssignValue();
            }
            if("machine".equals(demenType) && !StringUtils.isBlank(queryVo.getAssignValue()))
            {
                machine=queryVo.getAssignValue();
            }
            if("service".equals(demenType) && !StringUtils.isBlank(queryVo.getAssignValue()))
            {
                assignService=queryVo.getAssignValue();
            }
        }

        for (Map.Entry<String, Map<Long, SpecUrl>> per : data.entrySet()) {
            Map<Long, SpecUrl> perMap = per.getValue();
            if(perMap==null)
            {
                continue;
            }
            for (Map.Entry<Long, SpecUrl> entry : perMap.entrySet()) {
                SpecUrl url = entry.getValue();
                //启用禁用过滤
                boolean useable = url.getParameter(Constant.USEABLE_KEY, true);
                if ((state == 1 && !useable) || (state == 2 && useable)) {
                    continue;
                }
                //关键字过滤
                if (!StringUtils.isBlank(keyword) && !url.getUrlStr().contains(keyword)) {
                    continue;
                }
                String service=url.getService();
                //服务过滤
                if(!StringUtils.isBlank(assignService) && !assignService.equals(service))
                {
                    continue;
                }
                //机器过滤
                if (!StringUtils.isBlank(machine)) {
                    //查找提供服务的机器
                    Set<String> servProviders=zookeeperService.queryProviders(service, 1);
                    if(!servProviders.contains(machine)) {
                        //提供机器不包含提交服条件机器
                        continue;
                    }
                }
                //应用过滤
                if (!StringUtils.isBlank(application)) {
                    //查找提供服务的应用
                    Set<String> appProviders=zookeeperService.queryProviders(service,2);
                    if(!appProviders.contains(application)) {
                        //提供服务的应用不包含条件应用
                        continue;
                    }
                }
                resultMap.put(entry.getKey(), url);
            }
        }

        //组装成serviceVo
        if(resultMap!=null && resultMap.size()>0)
        {
            for(Long id:resultMap.keySet())
            {
                SpecUrl specUrl = resultMap.get(id);
                ServiceVo vo = BizFilterTransUtil.trans(specUrl);
                if(vo!=null)
                {
                    vo.setId(id.intValue());
                    list.add(vo);
                }
            }
        }
        Collections.sort(list);
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
