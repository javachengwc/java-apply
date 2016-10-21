package com.mountain.config;

import com.mountain.config.finagle.FinagleServerHandler;
import com.mountain.config.util.SpringContextUtils;
import com.mountain.model.Provider;
import com.mountain.model.SpecUrl;
import com.mountain.registry.Registry;
import com.util.AppUtil;
import com.util.NumberUtil;
import com.util.RunTimeUtil;
import com.util.StringUtil;
import com.util.encrypt.EncodeUtil;
import com.util.net.NetUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 服务节点
 */
public class ServiceBean implements InitializingBean {

    private static Logger logger = LoggerFactory.getLogger(ServiceBean.class);

    private static String PROTOCOL="thrift";

    private static int DEF_TIME_OUT=30000;

    //serviceName对应的注册中心,serviceName-->zk01,zk02
    public static ConcurrentMap<String,String> registryServiceMap = new ConcurrentHashMap<String,String>();

    //提供的service
    public static List<SpecUrl> providerUrlList = new ArrayList<SpecUrl>();

    static {
        RunTimeUtil.addShutdownHook(new Thread(new Runnable() {
            public void run() {
                logger.info("ServiceBean shutdown hook 回收service资源。");
                //回收Service资源
                ServiceBean.destroyAll();
            }
        }, "ServiceBeanShutdownHook"));
    }

    //服务id
    private String id;

    //服务的实现类的springBean名称
    private String ref;

    //对应的springBean对象
    private Object service;

    //thrift接口
    private String api;

    //服务版本号
    private String version = "1.0";

    //服务端口
    private Integer port = 0;

    //请求服务超时时间(毫秒)
    private Integer timeout=DEF_TIME_OUT;

    //服务负责人
    private String owner;

    //服务注册中心id
    private String registry;

    //服务备注
    private String note;

    private Integer threads = 10;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
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

    public Integer getPort() {
        return port;
    }

    public void setPort(String port) {
        if(NumberUtil.isNumeric(port))
        {
            this.port=Integer.parseInt(port);
        }
    }

    public Integer getTimeout() {
        return timeout;
    }

    public void setTimeout(String timeout) {
        if(NumberUtil.isNumeric(timeout))
        {
            this.timeout=Integer.parseInt(timeout);
        }
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRegistry() {
        return registry;
    }

    public void setRegistry(String registry) {
        this.registry = registry;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Integer getThreads() {
        return threads;
    }

    public void setThreads(Integer threads) {
        this.threads = threads;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }

    public void init() {
        this.init(true);
    }

    public void init(boolean needInitService) {
        String registryId = getRegistry();
        boolean hasRegistry = true;
        if (RegistryBean.zookeeperRegistryMap.isEmpty()) {
            hasRegistry = false;
            logger.error("ServiceBean init service [" + getId() + "] 获取注册中心为空");
        }
        if (needInitService) {
            //初始化服务
            if(!initService()){
                //如果没有初始化成功, 不应将服务注册到zookeeper上
                return;
            }
        }
        if(hasRegistry) {
            if (registryId == null)
            {
                registryId = StringUtil.joinString(new ArrayList<String>(RegistryBean.zookeeperRegistryMap.keySet()),",");
            }
            String[] registryIds = registryId.split(",");
            for (String rId : registryIds) {
                registryService(rId);
            }
            ServiceBean.registryServiceMap.put(getId(),registryId);
        }
    }

    //初始化服务
    public boolean initService() {
        int timeout = (getTimeout()==null)?DEF_TIME_OUT:getTimeout();
        boolean isSuccess = true;
        if(StringUtils.isBlank(ref))
        {
            isSuccess = false;
            logger.error("ServiceBean initService service["+getId()+"]  ref is null,服务实现为空");
            return isSuccess;
        }
        service = SpringContextUtils.getApplicationContext().getBean(ref);
        if(service==null)
        {
            isSuccess = false;
            logger.error("ServiceBean initService service["+getId()+"] springBean is null,从spring容器中获取beanName为[" + ref + "]的实例为null,服务初始化失败。");
            return isSuccess;
        }
        Provider provider = new Provider(id,api,port,version,timeout,threads);
        FinagleServerHandler finagleServerHandler = new FinagleServerHandler( provider,service);
        logger.info("ServiceBean initService service["+getId()+"] port:[" + port + "],thrift:[" + api + "],timeout:[" + timeout + "],threads:[" + threads + "]");
        try {
            finagleServerHandler.start();
            logger.info("ServiceBean initService service["+getId()+"] finagleServerHandler start,服务初始化成功");
        } catch (Exception e) {
            isSuccess = false;
            logger.error("ServiceBean initService service["+getId()+"] error,服务初始化失败.",e);
        }
        return isSuccess;
    }

    //注册服务
    private void registryService(String rId) {
        Registry registry = RegistryBean.zookeeperRegistryMap.get(rId);
        if (registry == null) {
            logger.error("ServiceBean registryService service["+getId()+"]获取注册中心registry:[" + rId + "]为空");
            return;
        }
        String urlStr=buildUrl();
        SpecUrl url = SpecUrl.valueOf(urlStr);
        registry.register(url);
        providerUrlList.add(url);
        logger.info("ServiceBean registryService service["+getId()+"]注册成功,url=" + urlStr );
        //服务备注
        try {
            registry.getZookeeperClient().writeData("/djy/" + getId(), EncodeUtil.urlEncode(getNote()));
        } catch (Exception e) {
            logger.error("ServiceBean registryService service["+getId()+"]备注失败.",e);
        }
    }

    public String buildUrl()
    {
        int timeout = (getTimeout()==null)?DEF_TIME_OUT:getTimeout();
        StringBuffer buf =new StringBuffer();
        buf.append("provider://");
        buf.append(NetUtil.getLocalHost()).append(":").append(getPort());
        buf.append("/").append(getId());
        buf.append("?category=providers");
        buf.append("&owner=").append(getOwner());
        buf.append("&timeout=").append(timeout);
        buf.append("&pid=").append( AppUtil.getPid());
        buf.append("&timestamp=").append( System.currentTimeMillis());
        buf.append("&application=").append(ApplicationBean.applicationName);
        buf.append("&version=").append(getVersion());
        buf.append("&protocol=").append(PROTOCOL);
        return buf.toString();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.init();
    }

    //删除服务节点
    public static void destroyAll() {
        for (SpecUrl providerUrl : providerUrlList) {
            String serviceName = providerUrl.getServiceName();
            String regZkIds = ServiceBean.registryServiceMap.get(serviceName);
            if (StringUtils.isBlank(regZkIds)) {
                continue;
            }
            String[] ridArray = regZkIds.split(",");
            for (String rId : ridArray) {
                try {
                    Registry registry = RegistryBean.zookeeperRegistryMap.get(rId);
                    registry.unregister(providerUrl);
                    logger.info("ServiceBean unregister "+serviceName+", 在注册中心[" + rId + "]删除服务节点[" + providerUrl + "]成功");
                } catch (Exception e) {
                    logger.error("ServiceBean unregister "+serviceName+" error, 在注册中心[" + rId + "]删除服务节点[" + providerUrl + "]失败", e);
                }
            }
        }
    }
}