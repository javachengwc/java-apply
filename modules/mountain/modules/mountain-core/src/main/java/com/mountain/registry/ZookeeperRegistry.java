package com.mountain.registry;

import com.mountain.constant.Constant;
import com.mountain.exception.RegistryException;
import com.mountain.model.SpecUrl;
import com.mountain.util.NamedThreadFactory;
import com.mountain.util.ServiceUrlUtil;
import com.mountain.zookeeper.StateListener;
import com.mountain.zookeeper.ZookeeperIoiClient;
import com.util.NumberUtil;
import com.util.encrypt.EncodeUtil;
import org.I0Itec.zkclient.IZkChildListener;
import org.apache.zookeeper.Watcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * zookeeper注册中心
 */
public class ZookeeperRegistry extends CacheRegistry {

    private  static Logger logger = LoggerFactory.getLogger(ZookeeperRegistry.class);

    private static int ZK_PORT = 2181;

    public static String ROOT_PATH = "mountain";

    private String  root;

    private  Set<String> anyServices =Collections.newSetFromMap(new ConcurrentHashMap<String,Boolean>());

    private  ConcurrentMap<SpecUrl, ConcurrentMap<NotifyListener, IZkChildListener>> listenerMap = new ConcurrentHashMap<SpecUrl, ConcurrentMap<NotifyListener, IZkChildListener>>();

    private ZookeeperIoiClient zkClient;

    private  Set<SpecUrl> failRegMap = Collections.newSetFromMap(new ConcurrentHashMap<SpecUrl,Boolean>());

    private  Set<SpecUrl> failUnregMap = Collections.newSetFromMap(new ConcurrentHashMap<SpecUrl,Boolean>());

    private  ConcurrentMap<SpecUrl, Set<NotifyListener>> failSubMap = new ConcurrentHashMap<SpecUrl, Set<NotifyListener>>();

    private  ConcurrentMap<SpecUrl, Set<NotifyListener>> failUnsubMap = new ConcurrentHashMap<SpecUrl, Set<NotifyListener>>();

    private  ConcurrentMap<SpecUrl, Map<NotifyListener, List<SpecUrl>>> failedNtfMap = new ConcurrentHashMap<SpecUrl, Map<NotifyListener, List<SpecUrl>>>();

    private ScheduledExecutorService retryExecutor = Executors.newScheduledThreadPool(1, new NamedThreadFactory("RetryTimer", true));

    private ScheduledFuture<?> retryFuture;

    public String getRoot() {
        return root;
    }

    public void setRoot(String root) {
        this.root = root;
    }

    public ZookeeperRegistry(SpecUrl url)
    {
        super(url);
        if (url.isAnyHost()) {
            logger.error("ZookeeperRegistry registry url is anyHost,url= "+url);
            throw new IllegalStateException("ZookeeperRegistry registry url is anyHost");
        }
        String group = url.getParameter(Constant.GROUP_KEY, ROOT_PATH);
        if (! group.startsWith(Constant.PATH_SEPARATOR)) {
            group = Constant.PATH_SEPARATOR + group;
        }
        this.root = group;
        zkClient = connectZookeeper(url);
        zkClient.addStateListener(new StateListener() {
            public void stateChanged(Watcher.Event.KeeperState state) {
            }
            public void reconnected()
            {
                try {
                    recover();
                } catch (Exception e) {
                    logger.error("ZookeeperRegistry stateListener reconnected do recover error,",e);
                }
            }
        });

        //重试机制
        int retryPeriod =Constant.RETRY_PERIOD_VALUE;
        String retryPeriodStr = url.getParameter(Constant.RETRY_PERIOD_KEY);
        if(NumberUtil.isNumeric(retryPeriodStr))
        {
            retryPeriod= Integer.parseInt(retryPeriodStr);
        }
        this.retryFuture = retryExecutor.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                try {
                    retry();
                } catch (Exception e) {
                    logger.error("ZookeeperRegistry retry run  error",e);
                }
            }
        }, retryPeriod, retryPeriod, TimeUnit.MILLISECONDS);
    }

    public ZookeeperIoiClient connectZookeeper(SpecUrl url)
    {
        String timeoutStr=url.getParameter("timeout","20000");
        int timeout =Integer.parseInt(timeoutStr);
        ZookeeperIoiClient client = new ZookeeperIoiClient(url.getBackupAddress(),timeout);
        return client;
    }

    //注册服务
    public void register(SpecUrl url)
    {
        super.register(url);
        failRegMap.remove(url);
        failUnregMap.remove(url);
        try {
            doRegister(url);
        } catch (Exception e) {
            boolean check = (getUrl().getParameter(Constant.CHECK_KEY, true) && url.getParameter(Constant.CHECK_KEY, true)
                             && !Constant.CONSUMER_PROTOCOL.equals(url.getProtocol()));
            if (check) {
                throw new RuntimeException("ZookeeperRegistry register fail url="+url+",registryUrl="+getUrl(),e);
            } else {
                logger.error("ZookeeperRegistry register fail url="+url+",registryUrl="+getUrl(),e);
            }
            failRegMap.add(url);
        }
    }

    //取消注册
    public void unregister(SpecUrl url)
    {
        super.unregister(url);
        failRegMap.remove(url);
        failUnregMap.remove(url);
        try {
            doUnregister(url);
        } catch (Exception e) {
            boolean check = (getUrl().getParameter(Constant.CHECK_KEY, true) && url.getParameter(Constant.CHECK_KEY, true)
                    && !Constant.CONSUMER_PROTOCOL.equals(url.getProtocol()));
            if (check) {
                throw new RuntimeException("ZookeeperRegistry unregister fail url="+url+",registryUrl="+getUrl(),e);
            } else {
                logger.error("ZookeeperRegistry unregister fail url="+url+",registryUrl="+getUrl(),e);
            }
            failUnregMap.add(url);
        }
    }

    //订阅服务
    public void subscribe(final SpecUrl url, final NotifyListener listener)
    {
        super.subscribe(url, listener);
        delFailSubscribed(url, listener);
        try {
            doSubscribe(url, listener);
        } catch (Exception e) {
            List<SpecUrl> urls = getCacheUrls(url);
            if (urls != null && urls.size() > 0) {
                notify(url, listener, urls);
                String cacheFile= System.getProperty("user.home") + "/.mountain/registry-" + url.getParameter("application","application") + url.getHost() + ".cache";
                cacheFile=getUrl().getParameter(Constant.FILE_KEY,cacheFile);
                logger.error("ZookeeperRegistry subscribe fail, using cached list: " + urls + " from cache file: " +cacheFile, e);
            } else {
                boolean check = (getUrl().getParameter(Constant.CHECK_KEY, true) && url.getParameter(Constant.CHECK_KEY, true));
                if (check) {
                    throw new RuntimeException("ZookeeperRegistry subscribe fail url="+url+",registryUrl="+getUrl(),e);
                } else {
                    logger.error("ZookeeperRegistry subscribe fail url="+url+",registryUrl="+getUrl(),e);
                }
            }
            addFailSubscribed(url, listener);
        }
        logger.info("ZookeeperRegistry subscribe over...");
    }

    //取消订阅
    public void unsubscribe(SpecUrl url, NotifyListener listener)
    {
        super.unsubscribe(url, listener);
        delFailSubscribed(url, listener);
        try {
            doUnsubscribe(url, listener);
        } catch (Exception e) {
            boolean check = (getUrl().getParameter(Constant.CHECK_KEY, true) && url.getParameter(Constant.CHECK_KEY, true));
            if (check ) {
                throw new RuntimeException("ZookeeperRegistry unsubscribe fail url="+url+",registryUrl="+getUrl(),e);
            } else {
                logger.error("ZookeeperRegistry unsubscribe fail url="+url+",registryUrl="+getUrl(),e);
            }
            addFailSubscribed(url, listener);
        }
    }

    @Override
    protected void notify(SpecUrl url, NotifyListener listener, List<SpecUrl> urls) {
        if (url == null) {
            throw new IllegalArgumentException("ZookeeperRegistry notify url == null");
        }
        if (listener == null) {
            throw new IllegalArgumentException("ZookeeperRegistry notify listener == null");
        }
        try {
            doNotify(url, listener, urls);
        } catch (Exception e) {
            //失败的通知定时重试
            Map<NotifyListener, List<SpecUrl>> listeners = failedNtfMap.get(url);
            if (listeners == null) {
                failedNtfMap.putIfAbsent(url, new ConcurrentHashMap<NotifyListener, List<SpecUrl>>());
                listeners = failedNtfMap.get(url);
            }
            listeners.put(listener, urls);
            logger.error("ZookeeperRegistry notify error, url= "+url,e);
        }
    }

    //查询
    public List<SpecUrl> query(SpecUrl url)
    {
        if (url == null) {
            throw new IllegalArgumentException("ZookeeperRegistry query url == null");
        }
        try {
            List<String> providers = new ArrayList<String>();
            for (String path : toCategoriesPath(url)) {
                List<String> children = zkClient.getChildren(path);
                if (children != null) {
                    providers.addAll(children);
                }
            }
            return toUrlsWithoutEmpty(url, providers);
        } catch (Exception e) {
            throw new RegistryException("ZookeeperRegistry query url fail,url=" + url + " from zookeeper " + getUrl(), e);
        }
    }

    public ZookeeperIoiClient getZookeeperClient()
    {
        return zkClient;
    }

    public void destroy() {
        super.destroy();
        try {
            retryFuture.cancel(true);
        } catch (Exception e) {
            logger.error("ZookeeperRegistry destroy retry future cancel error",e);
        }
        try {
            zkClient.close();
        } catch (Exception e) {
            logger.error("ZookeeperRegistry destroy  close zookeeper client error, url= " + getUrl(), e);
        }
    }

    private String getRootDir() {
        return root + Constant.PATH_SEPARATOR;
    }
    private String toServicePath(SpecUrl url) {
        String name = url.getInterface();
        if (Constant.ANY_VALUE.equals(name)) {
            return root;
        }
        return getRootDir() + EncodeUtil.urlEncode(name);
    }

    private String[] toCategoriesPath(SpecUrl url) {
        String[] categroies;
        if (Constant.ANY_VALUE.equals(url.getParameter(Constant.CATEGORY_KEY))) {
            categroies = new String[] {Constant.PROVIDERS_CATEGORY, Constant.CONSUMERS_CATEGORY,
                    Constant.ROUTERS_CATEGORY, Constant.CONFIGURATORS_CATEGORY};
        } else {
            categroies = url.getParameter(Constant.CATEGORY_KEY, new String[] {Constant.DEFAULT_CATEGORY});
        }
        String[] paths = new String[categroies.length];
        for (int i = 0; i < categroies.length; i ++) {
            paths[i] = toServicePath(url) + Constant.PATH_SEPARATOR + categroies[i];
        }
        return paths;
    }

    private String toCategoryPath(SpecUrl url) {
        return toServicePath(url) + Constant.PATH_SEPARATOR + url.getParameter(Constant.CATEGORY_KEY, Constant.DEFAULT_CATEGORY);
    }

    private String toUrlPath(SpecUrl url) {
        return toCategoryPath(url) + Constant.PATH_SEPARATOR + EncodeUtil.urlEncode(url.toUrlStr());
    }

    private List<SpecUrl> toUrlsWithoutEmpty(SpecUrl consumer, List<String> providers) {
        List<SpecUrl> urls = new ArrayList<SpecUrl>();
        if (providers != null && providers.size() > 0) {
            for (String provider : providers) {
                provider = EncodeUtil.urlDecode(provider);
                logger.info("ZookeeperRegistry gen without empty url,provider="+provider);
                if (provider.contains("://")) {
                    SpecUrl url = SpecUrl.valueOf(provider);
                    if (ServiceUrlUtil.isMatch(consumer, url)) {
                        urls.add(url);
                    }
                }
            }
        }
        return urls;
    }

    private List<SpecUrl> toUrlsWithEmpty(SpecUrl consumer, String path, List<String> providers) {
        List<SpecUrl> urls = toUrlsWithoutEmpty(consumer, providers);
        if(urls==null)
        {
           urls = new ArrayList<SpecUrl>();
        }
        if (urls.isEmpty()) {
            int i = path.lastIndexOf('/');
            String category = (i < 0) ? path : path.substring(i + 1);
            SpecUrl empty=consumer.genUrlWithProtocol(Constant.EMPTY_PROTOCOL).genUrlWithParamAdd(Constant.CATEGORY_KEY,category);
            logger.info("ZookeeperRegistry gen empty url is:\r\n"+empty+",category="+category);
            urls.add(empty);
        }
        return urls;
    }

    // 重试失败的动作
    protected void retry() {
        retryRegOrUn();
        retrySubOrUn();
        retryNotify();
    }

    public void retryRegOrUn()
    {
        if (! failRegMap.isEmpty()) {
            Set<SpecUrl> failed = new HashSet<SpecUrl>(failRegMap);
            int failCount= (failed==null)?0:failed.size();
            if (failCount > 0) {
                logger.info("ZookeeperRegistry retry register start,failCount=" + failCount);
                for (SpecUrl url : failed) {
                    try {
                        doRegister(url);
                        failRegMap.remove(url);
                    } catch (Exception e) {
                        logger.error("ZookeeperRegistry retry register url fail,url=" + url, e);
                    }
                }
            }
        }
        if(! failUnregMap.isEmpty()) {
            Set<SpecUrl> failed = new HashSet<SpecUrl>(failUnregMap);
            int failCount= (failed==null)?0:failed.size();
            if (failCount > 0) {
                logger.info("ZookeeperRegistry retry unregister start,failCount=" + failCount);
                for (SpecUrl url : failed) {
                    try {
                        doUnregister(url);
                        failUnregMap.remove(url);
                    } catch (Exception e) {
                        logger.error("ZookeeperRegistry retry unregister url fail,url=" + url, e);
                    }
                }
            }
        }
    }

    public void retrySubOrUn()
    {
        if (! failSubMap.isEmpty()) {
            Map<SpecUrl, Set<NotifyListener>> failed = new HashMap<SpecUrl, Set<NotifyListener>>(failSubMap);
            for (Map.Entry<SpecUrl, Set<NotifyListener>> entry :failed.entrySet()) {
                if (entry.getValue() == null || entry.getValue().size() == 0) {
                    failed.remove(entry.getKey());
                }
            }
            int failCount= (failed==null)?0:failed.size();
            if (failCount> 0) {
                logger.info("ZookeeperRegistry retry subscribe start,failCount=" + failCount);
                for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : failed.entrySet()) {
                    SpecUrl url = entry.getKey();
                    Set<NotifyListener> listeners = entry.getValue();
                    for (NotifyListener listener : listeners) {
                        try {
                            doSubscribe(url, listener);
                            listeners.remove(listener);
                        } catch (Exception e) {
                            logger.error("ZookeeperRegistry retry subscribe fail,url=" + url+",listener="+listener, e);
                        }
                    }
                }
            }
        }
        if (!failUnsubMap.isEmpty()) {
            Map<SpecUrl, Set<NotifyListener>> failed = new HashMap<SpecUrl, Set<NotifyListener>>(failUnsubMap);
            for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : failed.entrySet()) {
                if (entry.getValue() == null || entry.getValue().size() == 0) {
                    failed.remove(entry.getKey());
                }
            }
            int failCount= (failed==null)?0:failed.size();
            if (failCount > 0) {
                logger.info("ZookeeperRegistry retry unsubscribe start,failCount=" + failCount);
                for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : failed.entrySet()) {
                    SpecUrl url = entry.getKey();
                    Set<NotifyListener> listeners = entry.getValue();
                    for (NotifyListener listener : listeners) {
                        try {
                            doUnsubscribe(url, listener);
                            listeners.remove(listener);
                        } catch (Exception e) {
                            logger.error("ZookeeperRegistry retry unsubscribe fail,url=" + url+",listener="+listener, e);
                        }
                    }
                }
            }
        }
    }

    public void retryNotify()
    {
        if (! failedNtfMap.isEmpty()) {
            Map<SpecUrl, Map<NotifyListener, List<SpecUrl>>> failed = new HashMap<SpecUrl, Map<NotifyListener, List<SpecUrl>>>(failedNtfMap);
            for (Map.Entry<SpecUrl, Map<NotifyListener, List<SpecUrl>>> entry : failed.entrySet()) {
                if (entry.getValue() == null || entry.getValue().size() == 0) {
                    failed.remove(entry.getKey());
                }
            }
            int failCount= (failed==null)?0:failed.size();
            if (failCount > 0) {
                logger.info("ZookeeperRegistry retry notify start,failCount=" + failCount);
                for (Map<NotifyListener, List<SpecUrl>> values : failed.values()) {
                    for (Map.Entry<NotifyListener, List<SpecUrl>> entry : values.entrySet()) {
                        NotifyListener listener = entry.getKey();
                        List<SpecUrl> urls = entry.getValue();
                        try {
                            listener.notify(urls);
                            values.remove(listener);
                        } catch (Exception e) {
                            logger.error("ZookeeperRegistry retry notify fail,urls=" + urls+",listener="+listener, e);
                        }
                    }
                }
            }
        }
    }

    public void doRegister(SpecUrl url)
    {
        try {
            String urlPath =toUrlPath(url);
            logger.info("ZookeeperRegistry doRegister urlPath="+urlPath);
            zkClient.create(urlPath, url.getParameter(Constant.DYNAMIC_KEY, true));
        } catch (Exception e) {
            logger.error("ZookeeperRegistry doRegister failed ,url= "+url,e);
            throw new RegistryException("ZookeeperRegistry doRegister failed ,url="+url, e);
        }
    }

    public void doUnregister(SpecUrl url)
    {
        try {
            zkClient.delete(toUrlPath(url));
        } catch (Exception e) {
            logger.error("ZookeeperRegistry doUnregister failed ,url= "+url,e);
            throw new RegistryException("ZookeeperRegistry doUnregister failed,url=" + url,e);
        }
    }

    public void doSubscribe(final SpecUrl url, final NotifyListener listener)
    {
        try {
            String iface = url.getInterface();
            logger.info("ZookeeperRegistry doSubscribe url interface="+iface);
            if (Constant.ANY_VALUE.equals(iface)) {
                String root = this.root;
                ConcurrentMap<NotifyListener, IZkChildListener> listeners = listenerMap.get(url);
                if (listeners == null) {
                    listenerMap.putIfAbsent(url, new ConcurrentHashMap<NotifyListener, IZkChildListener>());
                    listeners = listenerMap.get(url);
                }
                IZkChildListener zkListener = listeners.get(listener);
                if (zkListener == null) {
                    listeners.putIfAbsent(listener, new IZkChildListener() {
                        public void handleChildChange(String parentPath, List<String> currentChilds) {
                            for (String child : currentChilds) {
                                child = EncodeUtil.urlDecode(child);
                                if (! anyServices.contains(child)) {
                                    anyServices.add(child);

                                    Map<String,String> addParams = new HashMap<String, String>();
                                    addParams.put(Constant.INTERFACE_KEY, child);
                                    addParams.put(Constant.CHECK_KEY, String.valueOf(false));
                                    SpecUrl otherUrl=url.genUrlWithPath(child).genUrlWithParamAdd(addParams);
                                    subscribe(otherUrl, listener);
                                }
                            }
                        }
                    });
                    zkListener = listeners.get(listener);
                }
                zkClient.create(root, false);
                List<String> services = zkClient.addChildListener(root, zkListener);
                if (services != null && services.size() > 0) {
                    for (String service : services) {
                        service = EncodeUtil.urlDecode(service);
                        anyServices.add(service);
                        Map<String,String> addParams = new HashMap<String, String>();
                        addParams.put(Constant.INTERFACE_KEY, service);
                        addParams.put(Constant.CHECK_KEY, String.valueOf(false));
                        SpecUrl otherUrl=url.genUrlWithPath(service).genUrlWithParamAdd(addParams);
                        subscribe(otherUrl, listener);
                    }
                }
            } else {
                List<SpecUrl> urls = new ArrayList<SpecUrl>();
                String[] pathArray = toCategoriesPath(url);
                int pathArrayLen= pathArray==null?0:pathArray.length;
                logger.info("ZookeeperRegistry doSubscribe pathArray length= "+pathArrayLen);
                for (String path :pathArray ) {
                    logger.info("ZookeeperRegistry doSubscribe per path,path="+path);
                    ConcurrentMap<NotifyListener, IZkChildListener> listeners = listenerMap.get(url);
                    if (listeners == null) {
                        listenerMap.putIfAbsent(url, new ConcurrentHashMap<NotifyListener, IZkChildListener>());
                        listeners = listenerMap.get(url);
                    }
                    IZkChildListener zkListener = listeners.get(listener);
                    if (zkListener == null) {
                        listeners.putIfAbsent(listener, new IZkChildListener() {
                            public void handleChildChange(String parentPath, List<String> currentChilds) {
                                ZookeeperRegistry.this.notify(url, listener, toUrlsWithEmpty(url, parentPath, currentChilds));
                            }
                        });
                        zkListener = listeners.get(listener);
                    }
                    zkClient.create(path, false);
                    List<String> children = zkClient.addChildListener(path, zkListener);
                    if (children != null) {
                        List<SpecUrl> specList =toUrlsWithEmpty(url, path, children);
                        urls.addAll(specList);
                    }
                }
                logger.info("ZookeeperRegistry doSubscribe invoke notify begin...");
                notify(url, listener, urls);
            }
        } catch (Exception e) {
            logger.error("ZookeeperRegistry doSubscribe failed ,url= "+url,e);
            throw new RegistryException("ZookeeperRegistry doSubscribe failed,url=" + url,e);
        }
    }

    public void doUnsubscribe(SpecUrl url, NotifyListener listener)
    {
        ConcurrentMap<NotifyListener, IZkChildListener> listeners = listenerMap.get(url);
        if (listeners != null) {
            IZkChildListener zkListener = listeners.get(listener);
            if (zkListener != null) {
                zkClient.removeChildListener(toUrlPath(url), zkListener);
            }
        }
    }

    public void doNotify(SpecUrl url, NotifyListener listener, List<SpecUrl> urls) {
        super.notify(url, listener, urls);
    }

    @Override
    protected void recover() throws Exception {
        // register
        Set<SpecUrl> recoverRegistered = new HashSet<SpecUrl>(getRegistered());
        if (recoverRegistered!=null && recoverRegistered.size()>0) {
            logger.info("ZookeeperRegistry recover register url " + recoverRegistered);
            for (SpecUrl url : recoverRegistered) {
                failRegMap.add(url);
            }
        }
        // subscribe
        Map<SpecUrl, Set<NotifyListener>> recoverSubscribed = new HashMap<SpecUrl, Set<NotifyListener>>(getSubscribed());
        if ( recoverSubscribed!=null && recoverSubscribed.size()>0) {
            logger.info("ZookeeperRegistry recover subscribe url " + recoverSubscribed.keySet());
            for (Map.Entry<SpecUrl, Set<NotifyListener>> entry : recoverSubscribed.entrySet()) {
                SpecUrl url = entry.getKey();
                for (NotifyListener listener : entry.getValue()) {
                    addFailSubscribed(url, listener);
                }
            }
        }
    }

    private void addFailSubscribed(SpecUrl url, NotifyListener listener) {
        Set<NotifyListener> listeners = failSubMap.get(url);
        if (listeners == null) {
            failSubMap.putIfAbsent(url,  Collections.newSetFromMap(new ConcurrentHashMap<NotifyListener,Boolean>()));
            listeners = failSubMap.get(url);
        }
        listeners.add(listener);
    }

    private void delFailSubscribed(SpecUrl url, NotifyListener listener) {
        Set<NotifyListener> listeners = failSubMap.get(url);
        if (listeners != null) {
            listeners.remove(listener);
        }
        listeners = failUnsubMap.get(url);
        if (listeners != null) {
            listeners.remove(listener);
        }
        Map<NotifyListener, List<SpecUrl>> notified = failedNtfMap.get(url);
        if (notified != null) {
            notified.remove(listener);
        }
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("root-->").append(root).append("\r\n");
        buf.append("registryUrl-->").append(registryUrl).append("\r\n");
        buf.append("properties-->").append(properties).append("\r\n");
        return buf.toString();
    }
}
