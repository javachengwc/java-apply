package com.mountain.manage.service.serve;

import com.mountain.constant.Constant;
import com.mountain.model.SpecUrl;
import com.mountain.registry.NotifyListener;
import com.mountain.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class AdminNotifyListener  implements NotifyListener{

    private static Logger logger = LoggerFactory.getLogger(AdminNotifyListener.class);

    private static AtomicLong ID = new AtomicLong();

    private String registryName;

    private Registry registry;

    //category--service--id--url
    private ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>> registryCache;

    private ZookeeperService zookeeperService;

    public  AdminNotifyListener( String registryName,Registry registry, ZookeeperService zookeeperService )
    {
        this.registryName=registryName;
        this.registry=registry;
        this.zookeeperService=zookeeperService;
        this.registryCache=zookeeperService.getMultRegistryCacheMap().get(registryName);
    }

    @Override
    public void notify(List<SpecUrl> urls) {
        logger.info("AdminNotifyListener notify exec...");
        int urlsCnt =urls==null?0:urls.size();
        if (urlsCnt<=0) {
            return;
        }
        //category--service--id--url
        Map<String, Map<String, Map<Long, SpecUrl>>> categoryMap = new HashMap<String, Map<String, Map<Long, SpecUrl>>>();
        Map<String,Set<String>> machineMap =new HashMap<String,Set<String>>();
        Map<String,Set<String>> applicationMap =new HashMap<String,Set<String>>();
        for (SpecUrl url : urls) {
            logger.info("AdminNotifyListener notify urls one url:"+url.toUrlStr());
            String category = url.getParameter(Constant.CATEGORY_KEY, Constant.PROVIDERS_CATEGORY);
            if (Constant.EMPTY_PROTOCOL.equalsIgnoreCase(url.getProtocol()))
            {
                ConcurrentMap<String, Map<Long, SpecUrl>> services = registryCache.get(category);
                if (services != null) {
                    String group = url.getParameter(Constant.GROUP_KEY);
                    String version = url.getParameter(Constant.VERSION_KEY);
                    if (!Constant.ANY_VALUE.equals(group) && !Constant.ANY_VALUE.equals(version)) {
                        services.remove(url.getService());
                        logger.info("AdminNotifyListener remove service "+url.getService());
                    } else {
                        for (Map.Entry<String, Map<Long, SpecUrl>> serviceEntry : services.entrySet()) {
                            String service = serviceEntry.getKey();
                            logger.info("AdminNotifyListener cache url service="+service+",serviceName="+getServiceName(service)+",notify url serviceName="+url.getServiceName());
                            if (getServiceName(service).equals(url.getServiceName())
                                    && (Constant.ANY_VALUE.equals(group) || getGroup(service).equals(group))
                                    && (Constant.ANY_VALUE.equals(version) || getVersion(service).equals(version))) {
                                services.remove(service);
                                logger.info("AdminNotifyListener remove service "+service);
                            }
                        }
                    }
                }
            } else {
                //找出更新的数据
                Map<String, Map<Long, SpecUrl>> map = categoryMap.get(category);
                if (map == null) {
                    map = new HashMap<String, Map<Long, SpecUrl>>();
                    categoryMap.put(category, map);
                }
                String service = url.getService();
                Map<Long, SpecUrl> ids = map.get(service);
                if (ids == null) {
                    ids = new HashMap<Long, SpecUrl>();
                    map.put(service, ids);
                }
                ids.put(ID.incrementAndGet(), url);
                String machine =url.getHost();
                String application =url.getParameter(Constant.APPLICATION_KEY);
                if(!StringUtils.isBlank(machine))
                {
                    Set<String> machineSet =machineMap.get(category);
                    if(machineSet==null)
                    {
                        machineSet=new HashSet<String>();
                        machineMap.put(category,machineSet);
                    }
                    machineSet.add(machine);
                }
                if(!StringUtils.isBlank(application))
                {
                    Set<String> applicationSet =applicationMap.get(category);
                    if(applicationSet==null)
                    {
                        applicationSet=new HashSet<String>();
                        applicationMap.put(category,applicationSet);
                    }
                    applicationSet.add(application);
                }
            }
        }
        //把更新的数据放入缓存中
        for (Map.Entry<String, Map<String, Map<Long, SpecUrl>>> categoryEntry : categoryMap.entrySet()) {
            String category = categoryEntry.getKey();
            Map<String, Map<Long, SpecUrl>> serviceMap = categoryEntry.getValue();
            ConcurrentMap<String, Map<Long, SpecUrl>> services = registryCache.get(category);
            if (services == null) {
                services = new ConcurrentHashMap<String, Map<Long, SpecUrl>>();
                registryCache.put(category, services);
            }
            services.putAll(serviceMap);
            //更新服务集
            Map<String,Set<String>> servsMap =zookeeperService.getServiceMap().get(registryName);
            if(servsMap==null)
            {
                servsMap=new HashMap<String,Set<String>>();
                zookeeperService.getServiceMap().put(registryName,servsMap);
            }
            Set<String> servs =servsMap.get(category);
            if(servs==null)
            {
                servs=new HashSet<String>();
                servsMap.put(category,servs);
            }
            servs.addAll(serviceMap.keySet());
        }

        //更新应用集合,机器集
        Map<String,Set<String>> applications =zookeeperService.getApplicationMap().get(registryName);
        if(applications==null)
        {
            applications=new HashMap<String,Set<String>>();
            zookeeperService.getApplicationMap().put(registryName,applications);
        }
        applications.putAll(applicationMap);

        Map<String,Set<String>> machines =zookeeperService.getMachineMap().get(registryName);
        if(machines==null)
        {
            machines=new HashMap<String,Set<String>>();
            zookeeperService.getMachineMap().put(registryName,machines);
        }
        machines.putAll(machineMap);
    }

    //service格式: group/service:version
    public static String getServiceName(String service) {
        String name=service;
        if (!StringUtils.isBlank(name)) {
            int i = name.indexOf('/');
            if (i >= 0) {
                name = name.substring(i + 1);
            }
            i = name.lastIndexOf(':');
            if (i >= 0) {
                name = name.substring(0, i);
            }
        }
        return name;
    }

    public static String getGroup(String service) {
        if (!StringUtils.isBlank(service)) {
            int i = service.indexOf('/');
            if (i >= 0) {
                return service.substring(0, i);
            }
        }
        return "";
    }

    public static String getVersion(String service) {
        if (!StringUtils.isBlank(service)) {
            int i = service.lastIndexOf(':');
            if (i >= 0) {
                return service.substring(i + 1);
            }
        }
        return "";
    }

}
