package com.mountain.manage.service.serve;

import com.mountain.config.RegistryBean;
import com.mountain.constant.Constant;
import com.mountain.model.SpecUrl;
import com.mountain.registry.Registry;
import com.util.encrypt.EncodeUtil;
import com.util.net.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class ZookeeperService {

    private static Logger logger = LoggerFactory.getLogger(ZookeeperService.class);

    private static SpecUrl adminUrl = new SpecUrl("admin", NetUtil.getLocalHost(), 0, "");

    //多注册中心，其实只有一个
    private Map<String, Registry> registryMap;

    private boolean inited=false;

    //多注册中心缓存的数据
    //registry-category--service--id--url
    private ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>>> multRegistryCacheMap =
            new ConcurrentHashMap<String, ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>>>();

    //注册中心-->category-->应用集
    private ConcurrentMap<String,Map<String,Set<String>>> applicationMap =new ConcurrentHashMap<String, Map<String,Set<String>>>();

    //注册中心-->category-->机器集
    private ConcurrentMap<String,Map<String,Set<String>>> machineMap =new ConcurrentHashMap<String, Map<String,Set<String>>>();

    //注册中心-->category-->服务集
    private  ConcurrentMap<String,Map<String,Set<String>>> serviceMap =new ConcurrentHashMap<String, Map<String,Set<String>>>();

    static
    {
        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put(Constant.INTERFACE_KEY, Constant.ANY_VALUE);
        paramMap.put(Constant.GROUP_KEY, Constant.ANY_VALUE);
        paramMap.put(Constant.CATEGORY_KEY, Constant.PROVIDERS_CATEGORY + "," + Constant.CONSUMERS_CATEGORY + ","+
                                            Constant.ROUTERS_CATEGORY + ","+Constant.CONFIGURATORS_CATEGORY);
        paramMap.put(Constant.VERSION_KEY, Constant.ANY_VALUE);
        paramMap.put(Constant.ENABLED_KEY, Constant.ANY_VALUE);
        paramMap.put(Constant.CHECK_KEY, String.valueOf(false));
        adminUrl =adminUrl.genUrlWithParamAdd(paramMap);
        logger.info("ZookeeperService static gen adminUrl="+adminUrl.toUrlStr());
    }

    public Map<String, Registry> getRegistryMap() {
        return registryMap;
    }

    public ConcurrentMap<String, ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>>> getMultRegistryCacheMap() {
        return multRegistryCacheMap;
    }

    public ConcurrentMap<String, Map<String, Set<String>>> getApplicationMap() {
        return applicationMap;
    }

    public ConcurrentMap<String, Map<String, Set<String>>> getMachineMap() {
        return machineMap;
    }

    public ConcurrentMap<String, Map<String, Set<String>>> getServiceMap() {
        return serviceMap;
    }

    @PostConstruct
    public synchronized void init() {

        registryMap = RegistryBean.getZookeeperRegistryMap();
        int registryCnt =registryMap.size();
        logger.info("ZookeeperService init registryCnt="+registryCnt);
        if(registryCnt<=0)
        {
            return;
        }
        if(!inited) {
            for (Map.Entry<String, Registry> entry : registryMap.entrySet()) {
                String registryName = entry.getKey();
                Registry registry = entry.getValue();
                ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>> registryCache = new ConcurrentHashMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>>();
                multRegistryCacheMap.put(registryName, registryCache);
                registry.subscribe(adminUrl, new AdminNotifyListener(registryName,registry,this));
                logger.info("ZookeeperService init registry:" + registryName);
            }
            inited=true;
        }
    }

    public int getRegistryCount()
    {
        return registryMap.size();
    }

    public Registry getRegistry()
    {
        return registryMap.values().iterator().next();
    }

    public void preDo()
    {
        if(!inited)
        {
            init();
        }
    }

    //获取缓存数据
    public  ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>> getRegistryCache()
    {
        preDo();
        String registryKey =registryMap.keySet().iterator().next();
        return getRegistryCache(registryKey);
    }

    public  ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>> getRegistryCache(String registryKey)
    {
        return multRegistryCacheMap.get(registryKey);
    }

    //获取机器集合
    public Set<String> getMachines()
    {
        preDo();
        String registryKey =registryMap.keySet().iterator().next();
        Map<String,Set<String>> map =  machineMap.get(registryKey);
        Set<String> rt= new HashSet<String>();
        if(map!=null)
        {
            for(Set<String> perSet:map.values())
            {
                if(perSet!=null)
                {
                    rt.addAll(perSet);
                }
            }
        }
        return rt;
    }

    //获取应用集
    public Set<String> getApplications()
    {
        preDo();
        String registryKey =registryMap.keySet().iterator().next();
        Map<String,Set<String>> map = applicationMap.get(registryKey);
        Set<String> rt= new HashSet<String>();
        if(map!=null)
        {
            for(Set<String> perSet:map.values())
            {
                if(perSet!=null)
                {
                    rt.addAll(perSet);
                }
            }
        }
        return rt;
    }

    //获取服务集
    public Set<String> getServices()
    {
        preDo();
        String registryKey =registryMap.keySet().iterator().next();
        Map<String,Set<String>> map = serviceMap.get(registryKey);
        Set<String> rt= new HashSet<String>();
        if(map!=null)
        {
            for(Set<String> perSet:map.values())
            {
                if(perSet!=null)
                {
                    rt.addAll(perSet);
                }
            }
        }
        return rt;
    }

    public String getData(String path) {
        try {
            String value=getRegistry().getZookeeperClient().getData(path);
            if(!StringUtils.isBlank(value))
            {
                value =EncodeUtil.urlDecode(value);
            }
            return  value;
        } catch (Exception e) {
            logger.error("ZookeeperService getData error,path="+path, e);
            return "";
        }
    }

}