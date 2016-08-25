package com.mountain.config;

import com.mountain.config.finagle.FinagleInvoker;
import com.mountain.core.Invoker;
import com.mountain.cluster.LoadBalance;
import com.mountain.cluster.WeightLoadBalance;
import com.mountain.config.finagle.FinagleAsynInvokeHandler;
import com.mountain.constant.Constant;
import com.mountain.core.UrlConfer;
import com.mountain.model.Consumer;
import com.mountain.model.SpecUrl;
import com.mountain.registry.NotifyListener;
import com.mountain.registry.Registry;
import com.twitter.finagle.Service;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.util.AppUtil;
import com.util.RunTimeUtil;
import com.util.net.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 消费服务节点
 */
public class ReferenceBean<T> implements FactoryBean,InitializingBean  {

    private static Logger logger = LoggerFactory.getLogger(ReferenceBean.class);

    //消费路由map sid:ip:application-->routeUrl
    private static ConcurrentMap<String, SpecUrl> routerMap = new ConcurrentHashMap<String, SpecUrl>();

    //服务提供者map serviceName-->提供者列表
    private static ConcurrentMap<String, List<Invoker>> serviceInvokerMap = new ConcurrentHashMap<String, List<Invoker>>();

    //服务的负载均衡map serviceName-->负载均衡器
    private static ConcurrentMap<String, LoadBalance> loadBalanceMap = new ConcurrentHashMap<String, LoadBalance>();

    //address-->服务提供调用
    public static ConcurrentMap<String, FinagleInvoker> finagleInvokerMap = new ConcurrentHashMap<String, FinagleInvoker>();

    private static String ip;

    private static Integer pid;

    //消费id
    private String id;

    //应用名
    private String application;

    //消费的服务id
    private String sid;

    //服务对应的thrift接口
    private String api;

    //消费的服务版本
    private String version = "1.0";

    //请求服务超时时间(毫秒)
    private Integer timeout;

    //注册中心id
    private String registry;

    //消费责任人
    private String owner;

    //直接服务地址, 如果有值,则不去注册中心获取服务地址, 而是直接请求该地址
    private String directurl;

    private T proxy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getDirecturl() {
        return directurl;
    }

    public void setDirecturl(String directurl) {
        this.directurl = directurl;
    }

    public T getProxy() {
        return proxy;
    }

    public void setProxy(T proxy) {
        this.proxy = proxy;
    }

    static {
        RunTimeUtil.addShutdownHook(new Thread(new Runnable() {
            public void run() {
                logger.info("ReferenceBean shutdown hook 回收reference资源。");
                //回收reference资源
                ReferenceBean.destroyAll();
            }
        }, "ReferenceBeanShutdownHook"));
    }

    public void init() {
        ip = NetUtil.getLocalHost();
        pid = AppUtil.getPid();
        this.application=ApplicationBean.applicationName;
        if(StringUtils.isBlank(owner))
        {
            this.owner=ApplicationBean.applicationOwner;
        }
        if(StringUtils.isBlank(application))
        {
            this.application=ApplicationBean.applicationName;
        }
        if(StringUtils.isBlank(directurl))
        {
            String registryId = getRegistry();
            Registry registryService = null;
            if (registryId == null) {
                if (RegistryBean.zookeeperRegistryMap.isEmpty())
                {
                    logger.error("ReferenceBean reference[" +getId()  + "]注册中心集为空");
                }else
               {
                    registryId = (String) RegistryBean.zookeeperRegistryMap.keySet().toArray()[0];
                    registryService = RegistryBean.zookeeperRegistryMap.get(registryId);
                }
            } else
            {
                registryService = RegistryBean.zookeeperRegistryMap.get(registryId);
            }
            if(registryService==null)
            {
                logger.error("ReferenceBean reference[" +getId()  + "]获取不到注册中心，registry:[" + registryId + "]");
                return;
            }
            try {
                //订阅服务
                subscribeService(registryService);
                //初始化消费者
                initConsumer();
                //注册消费者
                registryConsumer(registryService);
            } catch (Exception e) {
                logger.error("ReferenceBean 订阅服务或初始化并注册消费者异常,reference["+getId()+"],registry:["+registryId+"]",e);
            }
        }else{
            //直接根据directurl初始化消费者
            try {
                initConsumer();
            } catch (Exception e) {
                logger.error("ReferenceBean initConsumer with directurl error,reference["+getId()+"],directurl:["+getDirecturl()+"]",e);
            }
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    //订阅service
    private void subscribeService(final Registry registryService) {
        final String serviceName = getSid();
        final String referenceId=getId();
        final int timeout = (getTimeout()==null)?30000:getTimeout();

        if(StringUtils.isBlank(serviceName))
        {
            logger.error("ReferenceBean subscribeService fail,due sid is null.reference[" +getId()  + "]");
            return;
        }

        Map<String,String> paramMap = new HashMap<String,String>();
        paramMap.put( Constant.CATEGORY_KEY,"configurators");
        paramMap.put( Constant.VERSION_KEY,"*");
        final SpecUrl subscribeConfUrl = new SpecUrl("finagle", "0.0.0.0", 0, serviceName, paramMap);

        Map<String,String> providerParamMap = new HashMap<String,String>();
        providerParamMap.put( Constant.CATEGORY_KEY,"providers");
        providerParamMap.put( Constant.VERSION_KEY,"*");
        final SpecUrl subscribeProviderUrl = new SpecUrl("finagle", "0.0.0.0", 0, serviceName,providerParamMap);

        registryService.subscribe(subscribeProviderUrl, new NotifyListener() {
            public void notify(List<SpecUrl> urls) {
            int cnt = (urls==null)?0:urls.size();
            if(cnt<=0)
            {
                logger.info("NotifyListener notify subscribeProviderUrl urls count<=0,count="+cnt);
                return;
            }
            //获取所有的服务提供者对应的配置列表
            List<SpecUrl> configuratorUrls =  registryService.query(subscribeConfUrl);
            List<Invoker> invokers = mergeProviderAndConfUrls(urls, configuratorUrls, referenceId, version, timeout);
            serviceInvokerMap.put(serviceName, invokers);
            int invokerCnt =(invokers==null)?0:invokers.size();
            logger.info("NotifyListener notify reference[" + referenceId + "]缓存服务[" + serviceName + "]"+ invokerCnt +"] 个提供者。");
            loadBalanceMap.putIfAbsent(serviceName, new WeightLoadBalance());
            }
        });

        registryService.subscribe(subscribeConfUrl, new NotifyListener() {
            public void notify(List<SpecUrl> urls) {
            int cnt = (urls==null)?0:urls.size();
            if(cnt<=0)
            {
                logger.info("NotifyListener notify subscribeConfUrl urls count<=0,count="+cnt);
                return;
            }
            //获取所有服务提供者
            List<SpecUrl> invokerUrls = registryService.query(subscribeProviderUrl);
            List<Invoker> invokers = mergeProviderAndConfUrls(invokerUrls, urls, referenceId, version, timeout);
            serviceInvokerMap.put(serviceName, invokers);
            int invokerCnt =(invokers==null)?0:invokers.size();
            logger.info("NotifyListener notify reference[" + referenceId + "]缓存服务[" + serviceName + "]"+ invokerCnt +"] 个提供者。");
            }
        });
    }

    private List<Invoker> mergeProviderAndConfUrls(List<SpecUrl> providerUrls, List<SpecUrl> confUrls,String referenceId,String version,int timeout){
        Map<String, SpecUrl> urlMap = new HashMap<String, SpecUrl>();
        if (providerUrls != null) {
            for (SpecUrl invokerUrl : providerUrls) {
                urlMap.put(invokerUrl.getAddress()+":"+version, invokerUrl);
            }
            if(confUrls!=null){
                for (SpecUrl confUrl : confUrls) {
                    String key = confUrl.getAddress() + ":" + confUrl.getParameter(Constant.VERSION_KEY, "1.0");
                    if (urlMap.containsKey(key)) {
                        UrlConfer urlConfer = new UrlConfer(confUrl);
                        SpecUrl providerUrl =urlMap.get(key);
                        SpecUrl tempUrl = urlConfer.conf(providerUrl);
                        logger.info("ReferenceBean mergeProviderAndConfUrls 服务提供者url:[" + providerUrl.toUrlStr() + "]合并自定义配置url:[" + confUrl.toUrlStr() + "]后，\r\n"
                                +"生成的新的url:[" +tempUrl.toUrlStr() + "]");
                        urlMap.put(key, tempUrl);
                    }
                }
            }
        }

        List<Invoker> newInvokers = new ArrayList<Invoker>();
        for (Map.Entry<String, SpecUrl> entry : urlMap.entrySet()) {
            SpecUrl tempUrl = entry.getValue();
            tempUrl = tempUrl.genUrlWithParamAdd("timeout", timeout);
            //版本一致且可用
            String tempVersion =tempUrl.getParameter(Constant.VERSION_KEY, "1.0");
            String userable =tempUrl.getParameter(Constant.USEABLE_KEY, "true");
            if(!"false".equals(userable) && version.equals(tempVersion)){
                Invoker invoker = finagleInvokerMap.get(tempUrl.getAddress());
                if(invoker==null){
                    invoker = new FinagleInvoker(tempUrl);
                }
                newInvokers.add(invoker);
                logger.info("ReferenceBean mergeProviderAndConfUrls  reference[" + referenceId + "]合并服务提供者的配置后，url:[" + invoker.getUrl().toUrlStr() +
                        "]  useable:[" + invoker.getUrl().getParameter(Constant.USEABLE_KEY, "true")+ "]  version: [" +invoker.getUrl().getParameter(Constant.VERSION_KEY, "1.0")+ "]");
            }
        }
        return newInvokers;
    }

    //初始化消费者
    private void initConsumer() throws Exception{

        Consumer consumer = new Consumer(id,application,sid,api,version,timeout,registry,owner,directurl);
        FinagleAsynInvokeHandler handler = new FinagleAsynInvokeHandler(consumer);
        this.proxy = (T)handler.getProxy();
        logger.info("ReferenceBean initConsumer consumer["+getId()+"]消费者初始化成功,thrift:[" + api + "],服务提供者:["+sid+ "],version:[" + version +"],timeout:[" + timeout + "],owner:[" + owner +  "],directurl:[" + directurl+"]");
    }

    //注册消费者
    private void registryConsumer(Registry registryService) {
        String serviceName = getSid();
        if(StringUtils.isBlank(serviceName))
        {
            logger.error("ReferenceBean registryConsumer fail,due sid is null.reference[" +getId()  + "]");
            return;
        }
        final String routeKey = serviceName + ":" + ip + ":" + application;

        String urlStr =buildUrl();
        SpecUrl url = SpecUrl.valueOf(urlStr);
        registryService.register(url);
        logger.info("ReferenceBean registryConsumer reference[" +getId() + "]注册成功，url:[" + urlStr + "]");

        String routeUrlStr = buildRoute();
        SpecUrl routeUrl = SpecUrl.valueOf(routeUrlStr);
        routerMap.putIfAbsent(routeKey, routeUrl);
        logger.info("ReferenceBean registryConsumer reference[" +getId() + "]缓存route信息key:["+routeKey +"],routeUrl:[" + routeUrl + "]");

        //订阅路由信息
        Map<String,String> paramMap=new HashMap<String,String>();
        paramMap.put( Constant.CATEGORY_KEY,"routers");
        paramMap.put( Constant.VERSION_KEY,"*");
        SpecUrl subscribeRouterUrl = new SpecUrl("route", "0.0.0.0", 0, serviceName, paramMap);
        registryService.subscribe(subscribeRouterUrl, new NotifyListener() {

            public void notify(List<SpecUrl> urls) {
                int cnt = (urls==null)?0:urls.size();
                if(cnt<=0)
                {
                    logger.info("NotifyListener notify subscribeRouterUrl urls count<=0,count="+cnt);
                    return;
                }
                //如果消费者订阅了subscribeRouterUrl路由
                for (SpecUrl url : urls) {
                    if (url.getAddress().equals(ip) && application.equals(url.getParameter(Constant.APPLICATION_KEY))) {
                        routerMap.put(routeKey, url);
                        logger.info("NotifyListener reference[" +getId() + "]订阅route url:[" + url.toUrlStr() + "]并缓存,key:[" + routeKey + "]");
                    }
                }
            }
        });
    }

    public String buildUrl()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("consumer://");
        buf.append(NetUtil.getLocalHost());
        buf.append("/" + getSid());
        buf.append("?category=consumers");
        buf.append("&pid=" + AppUtil.getPid());
        buf.append("&timestamp=" + System.currentTimeMillis());
        buf.append("&version=" + getVersion());
        buf.append("&timeout=" + getTimeout());
        buf.append("&owner=" + getOwner());
        buf.append("&application=" + application);
        return buf.toString();
    }

    public String buildRoute()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("route://");
        buf.append(NetUtil.getLocalHost());
        buf.append("/" + getSid());
        buf.append("?category=routers");
        buf.append("&disabled=false");
        buf.append("&pid=" + AppUtil.getPid());
        buf.append("&application=" + application);
        return buf.toString();
    }

    //根据负载均衡算法获取提供者
    public static Invoker getProvider(String serviceName) {
        Invoker invoker = loadBalanceMap.get(serviceName).select( serviceInvokerMap.get(serviceName));
        return invoker;
    }

    public static SpecUrl getRouter(String serviceName){
        SpecUrl routerUrl = routerMap.get(serviceName + ":" + ip + ":" + ApplicationBean.applicationName);
        return routerUrl;
    }

    @Override
    public Object getObject() throws Exception {
        return getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        if (getProxy() == null) {
            return null;
        }
        return getProxy().getClass();
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    //回收资源
    public static void destroyAll(){
        for(Map.Entry<String,List<Invoker>> entry : serviceInvokerMap.entrySet()){
            List<Invoker> invokers = entry.getValue();
            destroyInvokerProvider(invokers);
        }
    }

    public static void destroyInvokerProvider(List<Invoker> invokers){
        if(invokers!=null){
            for(Invoker invoker : invokers){
                Service<ThriftClientRequest, byte[]> service = (Service<ThriftClientRequest, byte[]>) invoker.getProvider();
                service.close();
                logger.info("ReferenceBean destroyInvokerProvider释放服务提供者:[" + invoker.getUrl().toUrlStr() + "]资源成功。");
            }
        }
    }
}
