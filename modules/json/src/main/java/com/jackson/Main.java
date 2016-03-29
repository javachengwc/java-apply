package com.jackson;

import com.model.SiteAccessInfo;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.codehaus.jackson.type.TypeReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * jackson包的json相关操作
 */
public class Main {

    public static void main(String args []) throws Exception {
        JsonUtil jsonUtil =JsonUtil.buildNormalBinder();
        jsonUtil.setDateFormat("yyyy-MM-dd HH:mm:ss");
        List<SiteAccessInfo> list = generalTestData();
        SiteAccessInfo siteAccessInfo = generalSiglData();

        String listJsonStr =jsonUtil.toJson(list);
        String entityJsonStr = jsonUtil.toJson(siteAccessInfo);
        String nullStr = jsonUtil.toJson(null);
        String newListStr = jsonUtil.toJson(new ArrayList<SiteAccessInfo>());
        System.out.println("----list json str:"+listJsonStr);
        System.out.println("----entity json str:"+entityJsonStr);
        System.out.println("----null json str:"+nullStr);
        System.out.println("----new list json str:"+newListStr);

        //json-->list 方法1
        ObjectMapper mapper = jsonUtil.getMapper();
        List<SiteAccessInfo> list1 = mapper.readValue(listJsonStr, new TypeReference<List<SiteAccessInfo>>() {});

        //json-->list 方法2
        JavaType javaType = mapper.getTypeFactory().constructParametricType(List.class, SiteAccessInfo.class);
        //如果是Map类型  mapper.getTypeFactory().constructParametricType(HashMap.class,String.class, Bean.class);
        List<SiteAccessInfo> list2 =  (List<SiteAccessInfo>)mapper.readValue(listJsonStr, javaType);


        SiteAccessInfo info2 = jsonUtil.fromJson(entityJsonStr,SiteAccessInfo.class);

        if(list1!=null) {
            for(SiteAccessInfo site :list1) {
                System.out.println("----re list1 per entity =" + site);
            }
        }

        if(list2!=null) {
            for(SiteAccessInfo site :list2) {
                System.out.println("----re list2 per entity =" + site);
            }
        }

        System.out.println("----re object="+info2);

        JsonUtil jsonUtil2 =JsonUtil.buildNonNullBinder();
        SiteAccessInfo site  = new SiteAccessInfo();
        site.setName("163");
        String siteJsonStr = jsonUtil2.toJson(site);
        System.out.println("----entity non null json str:"+siteJsonStr);

    }

    public static List<SiteAccessInfo> generalTestData()
    {
        List<SiteAccessInfo> list = new ArrayList<SiteAccessInfo>();
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.baidu.com");
        info.setName("百度");
        info.setUv(10000 * 10000l);
        info.setOut(10000 * 1000l);
        info.setPcExp(10000 * 9000l);
        info.setWxExp(10000 * 2000l);
        info.setAddTime(new Date());
        list.add(info);

        SiteAccessInfo info2 = new SiteAccessInfo();
        info2.setSite("http://www.yy.com");
        info2.setName("YY");
        info2.setUv(1000*5000l);
        info2.setOut(1000*3000l);
        info2.setPcExp(1000*700l);
        info2.setWxExp(1000*2000l);
        list.add(info2);

        return list;
    }

    public static SiteAccessInfo generalSiglData()
    {
        SiteAccessInfo info = new SiteAccessInfo();
        info.setSite("http://www.xxx.com");
        info.setName("xxx");
        info.setUv(3000 * 10000l);
        info.setOut(3000 * 1000l);
        info.setPcExp(3000 * 9000l);
        info.setWxExp(3000 * 2000l);
        info.setAddTime(new Date());
        return info;
    }


}
