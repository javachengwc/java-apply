package com.mountain.manage.service.serve;

import com.mountain.constant.Constant;
import com.mountain.manage.model.vo.QueryVo;
import com.mountain.manage.model.vo.ServiceVo;
import com.mountain.manage.util.UrlTransUtil;
import com.mountain.model.SpecUrl;
import com.util.BeanCopyUtil;
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
 * 提供服务的服务类
 */
@Service
public class ProviderService {

    private static Logger logger = LoggerFactory.getLogger(ProviderService.class);

    @Autowired
    private ZookeeperService zookeeperService;

    //列表查询
    public Page<ServiceVo> queryList(QueryVo queryVo)
    {
        queryVo.setCategory(Constant.PROVIDERS_CATEGORY);
        Map<String, ConcurrentMap<String, Map<Long, SpecUrl>>> cacheMap =zookeeperService.getRegistryCache();
        logger.info("ProviderService queryList doing ..................");
        List<ServiceVo> list=filterMap(cacheMap,queryVo);
        Page<ServiceVo> page = new CollectionPage<ServiceVo>(list,queryVo.getPage(),queryVo.getRows());
        return page;
    }

    //category--service--id--url
    public List<ServiceVo> filterMap(Map<String, ConcurrentMap<String, Map<Long, SpecUrl>>> dataMap,QueryVo queryVo)
    {
        List<ServiceVo> list = new ArrayList<ServiceVo>();
        if(dataMap==null || dataMap.size()<=0)
        {
            return list;
        }
        String category= queryVo.getCategory();
        //根据category进行过滤
        Map<String, Map<Long, SpecUrl>> data =dataMap.get(category);
        //对服务进行过滤
        Map<Long, SpecUrl> afterMap =filterFromService(data,queryVo);
        //组装成serviceVo
        if(afterMap!=null && afterMap.size()>0)
        {
            for(Long id:afterMap.keySet())
            {
                SpecUrl specUrl = afterMap.get(id);
                ServiceVo vo = UrlTransUtil.trans(specUrl);
                if(vo!=null)
                {
                    vo.setId(id.intValue());
                    list.add(vo);
                }
            }
        }
        Collections.sort(list);
        return list;
    }

    public static Map<Long, SpecUrl> filterFromService(Map<String, Map<Long, SpecUrl>> urls,QueryVo queryVo) {
        Map<Long, SpecUrl> resultMap = new HashMap<Long, SpecUrl>();
        if (urls == null)
        {
            return resultMap;
        }

        //是否进行服务过滤
        boolean serviceFilter=false;
        if(!StringUtils.isBlank(queryVo.getDimenType()) && "service".equals(queryVo.getDimenType()))
        {
            String service =queryVo.getAssignValue();
            if(!StringUtils.isBlank(service))
            {
                serviceFilter=true;
                Map<Long, SpecUrl> map = urls.get(service);
                filterFromUrls(map,queryVo,resultMap);
            }
        }
        if (!serviceFilter) {
            for (Map.Entry<String, Map<Long, SpecUrl>> entry : urls.entrySet()) {
                filterFromUrls(entry.getValue(), queryVo,resultMap);
            }
        }
        return resultMap;
    }

    static void filterFromUrls(Map<Long, SpecUrl> from, QueryVo queryVo,Map<Long, SpecUrl> resultMap) {
        if (from == null || from.isEmpty())
        {
            return;
        }
        Integer state = (queryVo.getState()==null)?0:queryVo.getState();
        String keyword =queryVo.getQueryValue();
        String application="";
        String machine="";
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
        }
        for (Map.Entry<Long, SpecUrl> entry : from.entrySet()) {
            SpecUrl url = entry.getValue();

            //启用禁用过滤
            boolean useable = url.getParameter(Constant.USEABLE_KEY,true);
            if( (state==1 && !useable) || (state==2 && useable))
            {
                continue;
            }
            //关键字过滤
            if(!StringUtils.isBlank(keyword) && !url.getUrlStr().contains(keyword))
            {
                continue;
            }
            //机器过滤
            if(!StringUtils.isBlank(machine) && !machine.equals(url.getHost()))
            {
                continue;
            }
            //应用过滤
            if(!StringUtils.isBlank(application) &&  !application.equals(url.getParameter(Constant.APPLICATION_KEY,"")))
            {
                continue;
            }
            resultMap.put(entry.getKey(), url);
        }
    }

    public List<ServiceVo> testData()
    {
        List<ServiceVo> list = new ArrayList<ServiceVo>();
        ServiceVo vo = new ServiceVo();
        vo.setId(1);
        vo.setPid(5112);
        vo.setService("tt-cc-ww-server:1.0");
        vo.setApplication("tt-cc-ww-app");
        vo.setUrl("provider://172.0.0.1:20000/tt-cc-ww-server");
        vo.setOwner("cc开发");
        vo.setNote("测试服务1");
        vo.setUseable(true);
        vo.setWeight(100);
        vo.setAddress("172.0.0.1:20000");
        vo.setParameters("application=tt-cc-ww-app&category=providers&owner=cc开发&pid=5112&timestamp=1469679206966&version=1.0");

        ServiceVo vo2 = new ServiceVo();
        vo2.setId(1);
        vo2.setPid(3322);
        vo2.setService("tt-aa-bb-server:1.1");
        vo2.setApplication("tt-aa-bb-app");
        vo2.setUrl("provider://172.0.0.1:20002/tt-aa-bb-server");
        vo2.setOwner("aa开发");
        vo2.setUseable(false);
        vo2.setWeight(100);
        vo2.setAddress("172.0.0.1:20002");
        vo2.setParameters("application=tt-aa-bb-app&category=providers&owner=aa开发&pid=3322&timestamp=1459679206966&version=1.1");
        vo2.setInvokeFailCnt(0l);
        vo2.setInvokeFailTotalCnt(2l);
        vo2.setInvokeSuccessCnt(120l);
        vo2.setInvokeSucTotalCnt(130000l);

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
