package com.mountain.manage.service.serve;

import com.mountain.constant.Constant;
import com.mountain.model.SpecUrl;
import com.mountain.registry.NotifyListener;
import com.mountain.registry.Registry;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

public class AdminNotifyListener  implements NotifyListener{

    private static Logger logger = LoggerFactory.getLogger(AdminNotifyListener.class);

    private static AtomicLong ID = new AtomicLong();

    private Registry registry;

    //category--service--id--url
    private ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>> registryCache;

    public  AdminNotifyListener(Registry registry,ConcurrentMap<String, ConcurrentMap<String, Map<Long, SpecUrl>>> registryCache )
    {
        this.registry=registry;
        this.registryCache=registryCache;
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
        for (SpecUrl url : urls) {
            String category = url.getParameter(Constant.CATEGORY_KEY, Constant.PROVIDERS_CATEGORY);
            if (Constant.EMPTY_PROTOCOL.equalsIgnoreCase(url.getProtocol()))
            {
                ConcurrentMap<String, Map<Long, SpecUrl>> services = registryCache.get(category);
                if (services != null) {
                    String group = url.getParameter(Constant.GROUP_KEY);
                    String version = url.getParameter(Constant.VERSION_KEY);
                    if (!Constant.ANY_VALUE.equals(group) && !Constant.ANY_VALUE.equals(version)) {
                        services.remove(url.getService());
                    } else {
                        for (Map.Entry<String, Map<Long, SpecUrl>> serviceEntry : services.entrySet()) {
                            String service = serviceEntry.getKey();
                            if (getServiceName(service).equals(url.getServiceName())
                                    && (Constant.ANY_VALUE.equals(group) || getGroup(service).equals(group))
                                    && (Constant.ANY_VALUE.equals(version) || getVersion(service).equals(version))) {
                                services.remove(service);
                            }
                        }
                    }
                }
            } else {
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
            }
        }
        for (Map.Entry<String, Map<String, Map<Long, SpecUrl>>> categoryEntry : categoryMap.entrySet()) {
            String category = categoryEntry.getKey();
            ConcurrentMap<String, Map<Long, SpecUrl>> services = registryCache.get(category);
            if (services == null) {
                services = new ConcurrentHashMap<String, Map<Long, SpecUrl>>();
                registryCache.put(category, services);
            }
            services.putAll(categoryEntry.getValue());
        }
    }

    //service格式: group/service:version
    public static String getServiceName(String service) {
        String name="";
        if (!StringUtils.isBlank(service)) {
            int i = service.indexOf('/');
            if (i >= 0) {
                name = service.substring(i + 1);
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
