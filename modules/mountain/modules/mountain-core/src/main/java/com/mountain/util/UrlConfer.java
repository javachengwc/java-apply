package com.mountain.util;

import com.mountain.constant.Constant;
import com.mountain.model.SpecUrl;
import com.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UrlConfer implements Comparable<UrlConfer>{

    private SpecUrl confUrl;

    public SpecUrl getConfUrl() {
        return confUrl;
    }

    public void setConfUrl(SpecUrl confUrl) {
        this.confUrl = confUrl;
    }

    public UrlConfer(SpecUrl url) {
        this.confUrl = url;
    }

    public SpecUrl getUrl()
    {
        return confUrl;
    }

    public SpecUrl conf(SpecUrl url) {
        if (confUrl == null || confUrl.getHost() == null || url == null || url.getHost() == null)
        {
            return url;
        }

        if (Constant.ANYHOST_VALUE.equals(confUrl.getHost()) || url.getHost().equals(confUrl.getHost())) {
            String confApp = confUrl.getParameter(Constant.APPLICATION_KEY);
            String urlApp = url.getParameter(Constant.APPLICATION_KEY);
            if (confApp == null || Constant.ANY_VALUE.equals(confApp) || confApp.equals(urlApp)) {
                if (confUrl.getPort() == 0 || url.getPort() == confUrl.getPort()) {
                    Set<String> cdnKeys = new HashSet<String>();
                    cdnKeys.add(Constant.CATEGORY_KEY);
                    cdnKeys.add("check");
                    cdnKeys.add("dynamic");
                    cdnKeys.add("enabled");
                    for (Map.Entry<String, String> entry : confUrl.getParameters().entrySet()) {
                        String key = entry.getKey();
                        String value = entry.getValue();
                        String value2= url.getParameter(key.startsWith("~") ? key.substring(1) : key);
                        if (key.startsWith("~") || Constant.APPLICATION_KEY.equals(key)  || Constant.SIDE_KEY.equals(key)) {
                            cdnKeys.add(key);
                            if (!StringUtils.isBlank(value) && !Constant.ANY_VALUE.equals(value) && !value.equals(value2)) {
                                return url;
                            }
                        }
                    }
                    SpecUrl newUrl =confUrl.genUrlWithParamDel(cdnKeys.toArray(new String [cdnKeys.size()]));
                    return doConf(url, newUrl);
                }
            }
        }
        return url;
    }

    public int compareTo(UrlConfer o) {
        if (o == null) {
            return -1;
        }
        return getUrl().getHost().compareTo(o.getUrl().getHost());
    }


    public SpecUrl doConf(SpecUrl curUrl, SpecUrl configUrl) {

        Map<String,String> confParam =configUrl.getParameters();
        if (confParam == null || confParam.size() <= 0) {
            return curUrl;
        }
        int curParamCnt=(curUrl.getParameters()==null)?0:curUrl.getParameters().size();
        //是否有变动
        boolean hasChange = false;
        if(curParamCnt<=0)
        {
            hasChange = true;
        }else {
            for (Map.Entry<String, String> entry : confParam.entrySet()) {
                String curValue = curUrl.getParameters().get(entry.getKey());
                if (!StringUtil.strEquals(curValue, entry.getValue())) {
                    hasChange = true;
                    break;
                }
            }
        }
        //没变动，返回
        if(!hasChange)
        {
            return curUrl;
        }
        return curUrl.genUrlWithParamAdd(confParam);
    }
}