package com.mountain.manage.util;

import com.mountain.constant.Constant;
import com.mountain.manage.model.vo.ServiceVo;
import com.mountain.model.SpecUrl;
import com.util.NumberUtil;

public class UrlTransUtil {

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
