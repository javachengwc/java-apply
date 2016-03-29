package com.configcenter.client;

import com.alibaba.fastjson.JSON;
import com.configcenter.template.*;
import com.configcenter.zookeeper.BaseConnectStateListener;
import com.configcenter.zookeeper.DataWatcher;
import com.configcenter.zookeeper.PathWatcher;
import com.configcenter.zookeeper.ZookeeperClient;
import org.apache.commons.lang3.StringUtils;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 配置客户端类
 */
public class ConfigClient extends ZookeeperClient {

    public final static String APP_ROOT="/configcenter/app";

    public static String DEF_VALUE="";

    //本地缓存
    public static ConcurrentHashMap<String, String> localCache = new ConcurrentHashMap<String, String>();

    //监听的app
    public ConcurrentHashMap<String, String> appListener = new ConcurrentHashMap<String, String>();

    //监听的配置项
    public ConcurrentHashMap<String,String> configListener = new ConcurrentHashMap<String, String>();

    private static ConfigClient configClient;

    public static  ConfigClient getInstance(){
        return configClient;
    }

    /**
     * spring加载该类
     * <bean id="configClient" class="com.configcenter.client.ConfigClient" />
     */
    public ConfigClient() {
        this(null);
    }

    /**
     * spring加载该类
     * <bean id="configClient" class="com.configcenter.client.ConfigClient">
     * <constructor-arg name="zkHosts" value="127.0.0.1:2181" />
     * </bean>
     */
    public ConfigClient(String zkHosts) {

        this(zkHosts,null);
    }

    /**
     * spring加载该类
     * <bean id="configClient" class="com.configcenter.client.ConfigClient">
     * <constructor-arg name="zkHosts" value="127.0.0.1:2181" />
     * <constructor-arg name="connectTimeout" value="3000" />
     * </bean>
     */
    public ConfigClient(String zkHosts,Long connectTimeout)
    {
        this.zkHosts=StringUtils.isBlank(zkHosts)?DEF_ZK_HOSTS:zkHosts;
        this.connectTimeout= connectTimeout==null?DEF_CONNECT_TIMEOUT:connectTimeout;

        //监听器，监听到连接上，或者丢失会话后再连接上 就重新加载数据并注入节点watch
        BaseConnectStateListener listener =new BaseConnectStateListener(this){

            public void handleNewSession()
            {
                initHandle();
            }

            public void handleConnected()
            {
                initHandle();
            }
        };

        super.init(listener);
        configClient = this;
    }

    public synchronized  void initHandle() {

        watchRoot();
        List<String> apps = getChildren(APP_ROOT);
        initApps(apps);
    }

    //连接超时后从本地文件加载配置
    public synchronized  void initFromLocalFile()
    {

    }

    public void handleConnectTimeout()
    {
        initFromLocalFile();
    }

    //监听根节点
    public void watchRoot()
    {
        final String rootPath =APP_ROOT;
        try {
            getClient().getChildren().usingWatcher(new Watcher() {
                public void process(WatchedEvent watchedEvent) {

                    if (Event.EventType.NodeChildrenChanged == watchedEvent.getType()) {

                        logger.info("ConfigClient watchRoot app changed,rootPath=" + rootPath);

                        //重置监听
                        try {
                            ConfigClient.this.getClient().getChildren().usingWatcher(this).forPath(rootPath);
                        }catch(Exception e)
                        {
                            logger.error("ConfigClient watchRoot rewatch rootPath error,",e);
                        }

                        List<String> children = ConfigClient.this.getChildren(rootPath);
                        if(children==null || children.size()<=0)
                        {
                            //根节点所有应用节点为空的话，不做配置关联更新
                            logger.info("ConfigClient watchRoot rootPath children is null ");
                            return;
                        }

                        List<String> addApps = findAddCacheConfig(appListener, rootPath, children);
                        List<String> delApps = findDelCacheConfig(appListener, rootPath, children);

                        if (addApps != null) {
                            initApps(addApps);
                            addApps.clear();
                        }
                        if (delApps != null) {
                            for (String app : delApps) {
                                String appPath = rootPath.concat("/").concat(app);
                                cleanListenAndCacheByAppPath(appPath);
                            }
                        }
                        if (children != null) {
                            children.clear();
                        }

                    }
                    if (Event.EventType.NodeDeleted == watchedEvent.getType()) {
                        //根节点被删了，可能被误删，可能招到恶意攻击
                        logger.error("ConfigClient watchRoot rootPath deleted,rootPath=" + rootPath);
                    }
                }

            }).forPath(rootPath);
        }catch(Exception e)
        {
            logger.error("ConfigClient watchRoot checkExists error,rootPath="+rootPath,e);
        }
    }

    public void initApps(List<String> apps)
    {
        if(apps!=null)
        {
            for (String app : apps) {
                initApp(app);
            }
        }
    }

    public void initApp(String app)
    {
        String appPath =APP_ROOT.concat("/").concat(app);
        appListener.put(appPath,appPath);
        //监听应用
        pathWatch(appPath,new PathWatcher(appPath,getZooKeeper(),true){

            public void handleChildrenChange(String path,List<String> children)
            {

                logger.info("ConfigClient PathWatcher watch zookeeper path children change,path="+path
                        +",thread="+Thread.currentThread().getName()+Thread.currentThread().getId());
                updateAppChange(path,children);
            }

            public void handlePathDelete(String path)
            {
                logger.info("ConfigClient PathWatcher watch zookeeper path delete,path="+path
                        +",thread="+Thread.currentThread().getName()+Thread.currentThread().getId());
                cleanListenAndCacheByAppPath(path);
            }
        });

        this.loadAndListenApp(app);
    }

    //根据app获取并监听数据生成本地缓存
    public void loadAndListenApp(String app) {

        String appPath =APP_ROOT.concat("/").concat(app);
        //获取应用下所有配置项
        List<String> configKeyList = getChildren(appPath);

        loadAndListenAppConfig(app,configKeyList);
    }

    //根据app,配置项列表获取并监听数据生成本地缓存
    public void loadAndListenAppConfig(String app,List<String> children)
    {
        String appPath =APP_ROOT.concat("/").concat(app);
        if (children != null) {
            for (String configKey : children) {
                String configPath = appPath.concat("/").concat(configKey);
                configListener.put(configPath,configPath);
                String configValue =null;
                try{
                    configValue= readDataWithWatch(configPath,new DataWatcher(configPath,getZooKeeper(),true)
                    {
                        public void handleDataChange(String path,String newValue)
                        {
                            logger.info("ConfigClient DataWatcher watch zookeeper path value changed,path="+path
                                        +",newValue="+newValue+",thread="+Thread.currentThread().getName()+Thread.currentThread().getId());
                            localCache.put(path, newValue);
                        }

                        public void handlePathDelete(String path)
                        {
                            //能监听到配置项被删除,配置项被删除的处理交给应用的监听回调处理，此处不做处理
                            logger.info("ConfigClient DataWatcher watch zookeeper path delete,path=" + path
                                    +",thread="+Thread.currentThread().getName()+Thread.currentThread().getId());
                        }

                    });
                }catch(Exception e)
                {
                    logger.error("ConfigClient loadAndListenAppConfig error,configPath="+configPath,e);
                }
                localCache.put(configPath, configValue);
                logger.info("app-->"+app+",configKey-->" + configKey + ",value-->" + configValue + " local cache success");
            }
        }
    }

    public void printData()
    {
        for (ConcurrentMap.Entry<String,String> entry : appListener.entrySet()){
            String key = entry.getKey();
            String vakue= entry.getValue();

            logger.info("appListener key-->"+key+",value-->" + vakue );
        }
        logger.info("---------------------------------");
        for (ConcurrentMap.Entry<String,String> entry : configListener.entrySet()){
            String key = entry.getKey();
            String vakue= entry.getValue();
            logger.info("configListener key-->"+key+",value-->" + vakue );
        }
        logger.info("---------------------------------");
        printLocalCache();
    }

    public void printLocalCache()
    {
        if(localCache!=null) {
            for (ConcurrentMap.Entry<String,String> entry : localCache.entrySet()){
                String key = entry.getKey();
                String vakue= entry.getValue();

                logger.info("key-->"+key+",value-->" + vakue );
            }
        }
    }

    //根据appPath更新监听以及缓存
    public void updateAppChange(String appPath,List<String> children)
    {
        if(StringUtils.isBlank(appPath))
        {
            return;
        }
        //监听的配置项
        List<String> listenConfigPaths=getConfigListenerByApp(appPath);

        String appKey = appPath.replace(APP_ROOT.concat("/"),"");
        //缓存的配置项
        Map<String,String> appConfigCache= getConfigByApp(appKey);

        //去掉删除的配置监听
        List<String> delListenConfig =findDelListenConfig(listenConfigPaths,appPath,children);
        if(delListenConfig!=null)
        {
            for(String configKey:delListenConfig)
            {
                configListener.remove(appPath.concat("/").concat(configKey));
            }
            delListenConfig.clear();
        }
        if(listenConfigPaths!=null)
        {
            listenConfigPaths.clear();
        }

        //去掉删除的配置缓存
        List<String> delCacheConfig= findDelCacheConfig(appConfigCache,appPath,children);
        if(delCacheConfig!=null)
        {
            for (String configKey : delCacheConfig){
                localCache.remove(appPath.concat("/").concat(configKey));
            }
            delCacheConfig.clear();
        }
        //增加新的配置项
        List<String> addCacheConfig=findAddCacheConfig(appConfigCache,appPath,children);
        if(addCacheConfig!=null)
        {
            loadAndListenAppConfig(appKey,addCacheConfig);
        }
        if(appConfigCache!=null)
        {
            appConfigCache.clear();
        }
    }

    private List<String> findAddListenConfig(List<String> oldListenConfig,String appPath,List<String> children)
    {
        if(oldListenConfig==null || children==null )
        {
            return null;
        }
        List<String> addList = new ArrayList<String>();
        for(String config:children)
        {
            boolean find =false;
            for(String oldConfigPath:oldListenConfig)
            {
                String oldConfigKey = oldConfigPath.replace(appPath.concat("/"),"");
                if(config.equals(oldConfigKey))
                {
                    find=true;
                    break;
                }
            }
            if(!find)
            {
                addList.add(config);
            }
        }
        return addList;
    }

    private List<String> findDelListenConfig(List<String> oldListenConfig,String appPath,List<String> children)
    {
        if(oldListenConfig==null || children==null )
        {
            return null;
        }
        List<String> delList = new ArrayList<String>();
        for(String oldConfigPath:oldListenConfig)
        {
            boolean find =false;
            String oldConfigKey = oldConfigPath.replace(appPath.concat("/"),"");
            for(String config:children)
            {
                if(config.equals(oldConfigKey))
                {
                    find=true;
                    break;
                }
            }
            if(!find)
            {
                delList.add(oldConfigKey);
            }
        }
        return delList;
    }

    private List<String> findAddCacheConfig( Map<String,String> oldCacheConfig,String appPath,List<String> children)
    {
        if(oldCacheConfig==null || children==null )
        {
            return null;
        }
        List<String> addList = new ArrayList<String>();
        for(String config:children)
        {
            boolean find =false;
            for(Map.Entry<String,String> entry:oldCacheConfig.entrySet())
            {
                String oldConfigPath =entry.getKey();
                String oldConfigKey = oldConfigPath.replace(appPath.concat("/"),"");
                if(config.equals(oldConfigKey))
                {
                    find=true;
                    break;
                }
            }
            if(!find)
            {
                addList.add(config);
            }
        }
        return addList;
    }

    private List<String> findDelCacheConfig(Map<String,String> oldCacheConfig,String appPath,List<String> children)
    {
        if(oldCacheConfig==null || children==null )
        {
            return null;
        }
        List<String> delList = new ArrayList<String>();
        for(Map.Entry<String,String> entry:oldCacheConfig.entrySet())
        {
            String oldConfigPath =entry.getKey();
            boolean find =false;
            String oldConfigKey = oldConfigPath.replace(appPath.concat("/"),"");
            for(String config:children)
            {
                if(config.equals(oldConfigKey))
                {
                    find=true;
                    break;
                }
            }
            if(!find)
            {
                delList.add(oldConfigKey);
            }
        }
        return delList;
    }

    //根据appPath删除监听以及缓存
    public void cleanListenAndCacheByAppPath(String appPath)
    {
        if(StringUtils.isBlank(appPath))
        {
            return;
        }
        appListener.remove(appPath);
        List<String> configPaths=getConfigListenerByApp(appPath);
        if( configPaths!=null)
        {
            for(String configKey:configPaths)
            {
                configListener.remove(configKey);
            }
            configPaths.clear();
        }
        String appKey = appPath.replace(APP_ROOT.concat("/"),"");
        Map<String,String> appConfigCache= getConfigByApp(appKey);
        if(appConfigCache!=null)
        {
            for (String key : appConfigCache.keySet()){
                localCache.remove(key);
            }
            appConfigCache.clear();
        }
    }

    //通过应用path获取监听的配置项
    public List<String> getConfigListenerByApp(String appPath)
    {
         List<String> configPaths= new ArrayList<String>();
         if(configListener!=null && !StringUtils.isBlank(appPath))
         {
             for (ConcurrentMap.Entry<String,String> entry : configListener.entrySet()){
                 String key = entry.getKey();
                 if(key.indexOf(appPath)==0 && key.replace(appPath,"").startsWith("/")){
                     configPaths.add(key);
                 }
             }
         }
        return configPaths;
    }

    //通过应用名获取该应用下所有配置项
    public Map<String,String> getConfigByApp(String appKey){

        Map<String,String> configMap = new HashMap<String,String>();
        if(localCache!=null && !StringUtils.isBlank(appKey)) {
            String appPath =APP_ROOT.concat("/").concat(appKey);
            for (ConcurrentMap.Entry<String,String> entry : localCache.entrySet()){
                String key = entry.getKey();
                if(key.indexOf(appPath)==0 && key.replace(appPath,"").startsWith("/"))
                {
                    configMap.put(key, entry.getValue());
                }
            }
        }
        return configMap;
    }

    //根据appKey, configKey获取配置值
    public String getConfigValue(String appKey, String configKey) {

        if(StringUtils.isBlank(appKey) || StringUtils.isBlank(appKey) || localCache==null)
        {
            return DEF_VALUE;
        }
        String appPath = APP_ROOT.concat("/").concat(appKey);
        String configPath = appPath.concat("/").concat(configKey);

        String configValue = localCache.get(configPath);
        if (configValue == null) {

            logger.error("ConfigClient getConfigValue result is null,appKey= "+appKey+",configKey="+configKey);
            configValue= DEF_VALUE;
        } else {
            configValue = configValue.trim();
        }
        return configValue;
    }

    // 根据appKey, configKey获取配置值, 如果没有, 则返回默认值
    public String getConfigValue(String appKey, String configKey, String defaultValue) {
        String configValue = this.getConfigValue(appKey, configKey);
        if (StringUtils.isBlank(configValue)) {
            configValue = defaultValue;
        }
        return configValue;
    }

    //获取配置
    public BaseTemplate getBaseConfig(String appKey,String configKey)
    {
        String jsonStr = getConfigValue(appKey, configKey, "");
        BaseTemplate baseTemplate = null;
        try {
            baseTemplate = JSON.parseObject(jsonStr, BaseTemplate.class);
        } catch (Exception e) {
            String configPath = APP_ROOT.concat("/").concat(appKey).concat("/").concat(configKey);
            logger.error("ConfigClient getBaseConfig error, path="+configPath, e);
        }
        return baseTemplate;
    }

    //获取mysql配置
    public MysqlTemplate getMysqlConfig(String appKey, String configKey) {
        String jsonStr = getConfigValue(appKey, configKey, "");
        MysqlTemplate mySqlTemplate = null;
        try {
            mySqlTemplate = JSON.parseObject(jsonStr, MysqlTemplate.class);

            if(mySqlTemplate!=null && mySqlTemplate.getDbs()!=null) {
                for (MysqlDb mysqlDb : mySqlTemplate.getDbs()) {
                    mysqlDb.setUrl("jdbc:mysql://".concat(mySqlTemplate.getIp()).concat(":")
                        .concat(StringUtils.isBlank(mySqlTemplate.getPort())?"3306":mySqlTemplate.getPort()).concat("/")
                        .concat(StringUtils.isBlank(mysqlDb.getDb())?"":mysqlDb.getDb())
                        .concat("?autoReconnect=true&useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull"));
                }
            }
        } catch (Exception e) {
            String configPath = APP_ROOT.concat("/").concat(appKey).concat("/").concat(configKey);
            logger.error("ConfigClient getMysqlConfig error, path="+configPath, e);
        }
        return mySqlTemplate;
    }

    public MysqlDb getMysqlConfig(String appKey, String configKey, String db) {

        MysqlTemplate mysqlTemplate = getMysqlConfig(appKey, configKey);
        if(mysqlTemplate!=null && mysqlTemplate.getDbs()!=null)
        {
            for (MysqlDb mysqlDb : mysqlTemplate.getDbs()) {
                if (db.equals(mysqlDb.getDb())) {
                    return mysqlDb;
                }
            }
        }
        String configPath = APP_ROOT.concat("/").concat(appKey).concat("/").concat(configKey);
        throw new RuntimeException("ConfigClient getMysqlConfig db[ "+db+"] is not found,path="+configPath);
    }

    //获取Kafka配置
    public KafkaTemplate getKafkaConfig(String appKey, String configKey) {
        String jsonStr = getConfigValue(appKey, configKey, "");
        KafkaTemplate kafkaTemplate = null;
        try {
            kafkaTemplate = JSON.parseObject(jsonStr, KafkaTemplate.class);
        } catch (Exception e) {
            String configPath = APP_ROOT.concat("/").concat(appKey).concat("/").concat(configKey);
            logger.error("ConfigClient getKafkaConfig error, path="+configPath, e);
        }
        return kafkaTemplate;
    }

    //获取redis配置
    public RedisTemplate getRedisConfig(String appKey, String configKey) {
        String jsonStr = getConfigValue(appKey, configKey, "");
        RedisTemplate redisTemplate = null;
        try {
            redisTemplate = JSON.parseObject(jsonStr, RedisTemplate.class);
        } catch (Exception e) {
            String configPath = APP_ROOT.concat("/").concat(appKey).concat("/").concat(configKey);
            logger.error("ConfigClient getRedisConfig error, path="+configPath, e);
        }
        return redisTemplate;
    }

}
