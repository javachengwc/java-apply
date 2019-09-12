package com.pseudocode.netflix.eureka.core;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;

import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;
import com.pseudocode.netflix.eureka.client.appinfo.ApplicationInfoManager;
import com.pseudocode.netflix.eureka.client.appinfo.DataCenterInfo;
import com.pseudocode.netflix.eureka.client.appinfo.EurekaInstanceConfig;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.discovery.DiscoveryClient;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClient;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClientConfig;
import com.pseudocode.netflix.eureka.core.cluster.PeerEurekaNodes;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;
import com.pseudocode.netflix.eureka.core.resources.ServerCodecs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Eureka-Server 启动类
public class EurekaBootStrap implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(EurekaBootStrap.class);

    //部署环境 -- 测试
    private static final String TEST = "test";

    private static final String ARCHAIUS_DEPLOYMENT_ENVIRONMENT = "archaius.deployment.environment";

    private static final String EUREKA_ENVIRONMENT = "eureka.environment";

    //部署数据中心 -- CLOUD
    private static final String CLOUD = "cloud";
    //部署数据中心 -- 默认
    private static final String DEFAULT = "default";

    private static final String ARCHAIUS_DEPLOYMENT_DATACENTER = "archaius.deployment.datacenter";

    private static final String EUREKA_DATACENTER = "eureka.datacenter";

    protected volatile EurekaServerContext serverContext;

    //protected volatile AwsBinder awsBinder;

    private EurekaClient eurekaClient;

    public EurekaBootStrap() {
        this(null);
    }

    public EurekaBootStrap(EurekaClient eurekaClient) {
        this.eurekaClient = eurekaClient;
    }

    //Servlet容器( Tomcat、Jetty )启动时，调用contextInitialized() 方法，初始化 Eureka-Server
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            //初始化 Eureka-Server 配置环境
            initEurekaEnvironment();
            //初始化 Eureka-Server 上下文
            initEurekaServerContext();

            ServletContext sc = event.getServletContext();
            sc.setAttribute(EurekaServerContext.class.getName(), serverContext);
        } catch (Throwable e) {
            logger.error("Cannot bootstrap eureka server :", e);
            throw new RuntimeException("Cannot bootstrap eureka server :", e);
        }
    }

    protected void initEurekaEnvironment() throws Exception {
        logger.info("Setting the eureka configuration..");

        //设置配置文件的数据中心
        String dataCenter = ConfigurationManager.getConfigInstance().getString(EUREKA_DATACENTER);
        if (dataCenter == null) {
            logger.info("Eureka data center value eureka.datacenter is not set, defaulting to default");
            ConfigurationManager.getConfigInstance().setProperty(ARCHAIUS_DEPLOYMENT_DATACENTER, DEFAULT);
        } else {
            ConfigurationManager.getConfigInstance().setProperty(ARCHAIUS_DEPLOYMENT_DATACENTER, dataCenter);
        }

        //设置配置文件的环境
        String environment = ConfigurationManager.getConfigInstance().getString(EUREKA_ENVIRONMENT);
        if (environment == null) {
            ConfigurationManager.getConfigInstance().setProperty(ARCHAIUS_DEPLOYMENT_ENVIRONMENT, TEST);
            logger.info("Eureka environment value eureka.environment is not set, defaulting to test");
        }
    }

    protected void initEurekaServerContext() throws Exception {
        //创建 Eureka-Server 配置
        EurekaServerConfig eurekaServerConfig = new DefaultEurekaServerConfig();

        // For backward compatibility
        //Eureka-Server 请求和响应的数据兼容
        JsonXStream.getInstance().registerConverter(new V1AwareInstanceInfoConverter(), XStream.PRIORITY_VERY_HIGH);
        XmlXStream.getInstance().registerConverter(new V1AwareInstanceInfoConverter(), XStream.PRIORITY_VERY_HIGH);

        logger.info("Initializing the eureka client...");
        logger.info(eurekaServerConfig.getJsonCodecName());
        //创建 Eureka-Server 请求和响应编解码器
        ServerCodecs serverCodecs = new DefaultServerCodecs(eurekaServerConfig);

        //创建 Eureka-Client
        //Eureka-Server 内嵌 Eureka-Client，用于和 Eureka-Server 集群里其他节点通信交互
        ApplicationInfoManager applicationInfoManager = null;
        if (eurekaClient == null) {
            EurekaInstanceConfig instanceConfig = isCloud(ConfigurationManager.getDeploymentContext())
                    ? new CloudInstanceConfig()
                    : new MyDataCenterInstanceConfig();

            applicationInfoManager = new ApplicationInfoManager(instanceConfig, new EurekaConfigBasedInstanceInfoProvider(instanceConfig).get());

            EurekaClientConfig eurekaClientConfig = new DefaultEurekaClientConfig();
            eurekaClient = new DiscoveryClient(applicationInfoManager, eurekaClientConfig);
        } else {
            applicationInfoManager = eurekaClient.getApplicationInfoManager();
        }

        PeerAwareInstanceRegistry registry;
//        if (isAws(applicationInfoManager.getInfo())) {
//            registry = new AwsInstanceRegistry(
//                    eurekaServerConfig,
//                    eurekaClient.getEurekaClientConfig(),
//                    serverCodecs,
//                    eurekaClient
//            );
//            awsBinder = new AwsBinderDelegate(eurekaServerConfig, eurekaClient.getEurekaClientConfig(), registry, applicationInfoManager);
//            awsBinder.start();
//        } else {
            //创建应用实例信息的注册表
            registry = new PeerAwareInstanceRegistryImpl(
                    eurekaServerConfig,
                    eurekaClient.getEurekaClientConfig(),
                    serverCodecs,
                    eurekaClient
            );
//        }

        //创建 Eureka-Server 集群节点集合
        PeerEurekaNodes peerEurekaNodes = getPeerEurekaNodes(
                registry,
                eurekaServerConfig,
                eurekaClient.getEurekaClientConfig(),
                serverCodecs,
                applicationInfoManager
        );

        //创建 Eureka-Server 上下文
        serverContext = new DefaultEurekaServerContext(
                eurekaServerConfig,
                serverCodecs,
                registry,
                peerEurekaNodes,
                applicationInfoManager
        );

        //初始化 EurekaServerContextHolder
        EurekaServerContextHolder.initialize(serverContext);

        //初始化 Eureka-Server 上下文
        serverContext.initialize();
        logger.info("Initialized server context");

        // Copy registry from neighboring eureka node
        //从其他 Eureka-Server 拉取注册信息
        int registryCount = registry.syncUp();
        registry.openForTraffic(applicationInfoManager, registryCount);

        // Register all monitoring statistics.
        //注册监控
        //EurekaMonitors.registerAllStats();
    }

    protected PeerEurekaNodes getPeerEurekaNodes(PeerAwareInstanceRegistry registry, EurekaServerConfig eurekaServerConfig, EurekaClientConfig eurekaClientConfig, ServerCodecs serverCodecs, ApplicationInfoManager applicationInfoManager) {
        PeerEurekaNodes peerEurekaNodes = new PeerEurekaNodes(
                registry,
                eurekaServerConfig,
                eurekaClientConfig,
                serverCodecs,
                applicationInfoManager
        );

        return peerEurekaNodes;
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        try {
            logger.info("{} Shutting down Eureka Server..", new Date());
            ServletContext sc = event.getServletContext();
            sc.removeAttribute(EurekaServerContext.class.getName());

            destroyEurekaServerContext();
            destroyEurekaEnvironment();

        } catch (Throwable e) {
            logger.error("Error shutting down eureka", e);
        }
        logger.info("{} Eureka Service is now shutdown...", new Date());
    }

    protected void destroyEurekaServerContext() throws Exception {
//        EurekaMonitors.shutdown();
//        if (awsBinder != null) {
//            awsBinder.shutdown();
//        }
        if (serverContext != null) {
            serverContext.shutdown();
        }
    }

    protected void destroyEurekaEnvironment() throws Exception {

    }

    protected boolean isAws(InstanceInfo selfInstanceInfo) {
        boolean result = DataCenterInfo.Name.Amazon == selfInstanceInfo.getDataCenterInfo().getName();
        logger.info("isAws returned {}", result);
        return result;
    }

    protected boolean isCloud(DeploymentContext deploymentContext) {
        logger.info("Deployment datacenter is {}", deploymentContext.getDeploymentDatacenter());
        return CLOUD.equals(deploymentContext.getDeploymentDatacenter());
    }
}
