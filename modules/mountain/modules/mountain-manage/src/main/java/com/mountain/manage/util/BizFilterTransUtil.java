package com.mountain.manage.util;

import com.mountain.constant.Constant;
import com.mountain.manage.model.vo.QueryVo;
import com.mountain.manage.model.vo.ServiceVo;
import com.mountain.model.SpecUrl;
import com.util.base.NumberUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

public class BizFilterTransUtil {

    //category--service--id--url
    public static List<ServiceVo> filterMap(Map<String, ConcurrentMap<String, Map<Long, SpecUrl>>> dataMap,QueryVo queryVo)
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
                ServiceVo vo = BizFilterTransUtil.trans(specUrl);
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

    public static void filterFromUrls(Map<Long, SpecUrl> from, QueryVo queryVo,Map<Long, SpecUrl> resultMap) {
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

    public static ServiceVo trans(SpecUrl url)
    {
        if(url==null)
        {
            return null;
        }
        ServiceVo serviceVo = new ServiceVo();
        String pid = url.getParameter("pid");
        serviceVo.setPid(NumberUtil.isNumeric(pid)?Integer.parseInt(pid):0);
        String service =url.getService();
        serviceVo.setService(service);

        serviceVo.setApplication(url.getParameter(Constant.APPLICATION_KEY));
        serviceVo.setOwner(url.getParameter("owner"));
        serviceVo.setAddress(url.getAddress());
        boolean useable = url.getParameter(Constant.USEABLE_KEY,true);
        serviceVo.setUseable(useable);
        String weightStr=url.getParameter("weight");
        serviceVo.setWeight(NumberUtil.isNumeric(weightStr)?Integer.parseInt(weightStr):100);

        String urlStr =url.getUrlStr();
        String urlNoParamStr= urlStr;
        String params="";
        int p=urlStr.indexOf("?");
        if(p>0)
        {
            urlNoParamStr= urlStr.substring(0,p);
            params=urlStr.substring(p+1);
        }
        serviceVo.setUrl(urlNoParamStr);
        serviceVo.setParameters(params);
        serviceVo.setNote(url.getNote());
        return serviceVo;
    }
}
