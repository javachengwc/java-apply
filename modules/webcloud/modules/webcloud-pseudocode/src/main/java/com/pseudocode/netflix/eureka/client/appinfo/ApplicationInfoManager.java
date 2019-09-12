package com.pseudocode.netflix.eureka.client.appinfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//应用信息管理器
public class ApplicationInfoManager {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationInfoManager.class);

    private static final InstanceStatusMapper NO_OP_MAPPER = new InstanceStatusMapper() {
        @Override
        public InstanceStatus map(InstanceStatus prev) {
            return prev;
        }
    };

    private static ApplicationInfoManager instance = new ApplicationInfoManager(null, null, null);

    //状态变更监听器
    protected  Map<String, StatusChangeListener> listeners;

    //应用实例状态匹配
    private  InstanceStatusMapper instanceStatusMapper;

    //应用实例信息
    private InstanceInfo instanceInfo;

    //应用实例配置
    private EurekaInstanceConfig config;

    public static class OptionalArgs {
        private InstanceStatusMapper instanceStatusMapper;

        public void setInstanceStatusMapper(InstanceStatusMapper instanceStatusMapper) {
            this.instanceStatusMapper = instanceStatusMapper;
        }

        InstanceStatusMapper getInstanceStatusMapper() {
            return instanceStatusMapper == null ? NO_OP_MAPPER : instanceStatusMapper;
        }
    }

    public ApplicationInfoManager(EurekaInstanceConfig config, InstanceInfo instanceInfo, OptionalArgs optionalArgs) {
        this.config = config;
        this.instanceInfo = instanceInfo;
        this.listeners = new ConcurrentHashMap<String, StatusChangeListener>();
        if (optionalArgs != null) {
            this.instanceStatusMapper = optionalArgs.getInstanceStatusMapper();
        } else {
            this.instanceStatusMapper = NO_OP_MAPPER;
        }

        // Hack to allow for getInstance() to use the DI'd ApplicationInfoManager
        instance = this;
    }

    public ApplicationInfoManager(EurekaInstanceConfig config, /* nullable */ OptionalArgs optionalArgs) {
        //this(config, new EurekaConfigBasedInstanceInfoProvider(config).get(), optionalArgs);
    }

    public ApplicationInfoManager(EurekaInstanceConfig config, InstanceInfo instanceInfo) {
        this(config, instanceInfo, null);
    }

    @Deprecated
    public ApplicationInfoManager(EurekaInstanceConfig config) {
        this(config, (OptionalArgs) null);
    }

    @Deprecated
    public static ApplicationInfoManager getInstance() {
        return instance;
    }

    public void initComponent(EurekaInstanceConfig config) {
        try {
            this.config = config;
            //this.instanceInfo = new EurekaConfigBasedInstanceInfoProvider(config).get();
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize ApplicationInfoManager", e);
        }
    }

    public InstanceInfo getInfo() {
        return instanceInfo;
    }

    public EurekaInstanceConfig getEurekaInstanceConfig() {
        return config;
    }

    public void registerAppMetadata(Map<String, String> appMetadata) {
        instanceInfo.registerRuntimeMetadata(appMetadata);
    }

    //设置应用实例信息的状态
    public synchronized void setInstanceStatus(InstanceStatus status) {
        InstanceStatus next = instanceStatusMapper.map(status);
        if (next == null) {
            return;
        }

        InstanceStatus prev = instanceInfo.setStatus(next);
        if (prev != null) {
            for (StatusChangeListener listener : listeners.values()) {
                try {
                    listener.notify(new StatusChangeEvent(prev, next));
                } catch (Exception e) {
                    logger.warn("failed to notify listener: {}", listener.getId(), e);
                }
            }
        }
    }

    public void registerStatusChangeListener(StatusChangeListener listener) {
        listeners.put(listener.getId(), listener);
    }

    public void unregisterStatusChangeListener(String listenerId) {
        listeners.remove(listenerId);
    }

    public void refreshDataCenterInfoIfRequired() {
        String existingAddress = instanceInfo.getHostName();

        String newAddress=null;
//        if (config instanceof RefreshableInstanceConfig) {
//            // Refresh data center info, and return up to date address
//            newAddress = ((RefreshableInstanceConfig) config).resolveDefaultAddress(true);
//        } else {
//            newAddress = config.getHostName(true);
//        }
        String newIp = config.getIpAddress();

        if (newAddress != null && !newAddress.equals(existingAddress)) {
            logger.warn("The address changed from : {} => {}", existingAddress, newAddress);

            InstanceInfo.Builder builder = new InstanceInfo.Builder(instanceInfo);
            builder.setHostName(newAddress).setIPAddr(newIp).setDataCenterInfo(config.getDataCenterInfo());
            instanceInfo.setIsDirty();
        }
    }

    public void refreshLeaseInfoIfRequired() {
        LeaseInfo leaseInfo = instanceInfo.getLeaseInfo();
        if (leaseInfo == null) {
            return;
        }
        int currentLeaseDuration = config.getLeaseExpirationDurationInSeconds();
        int currentLeaseRenewal = config.getLeaseRenewalIntervalInSeconds();
        if (leaseInfo.getDurationInSecs() != currentLeaseDuration || leaseInfo.getRenewalIntervalInSecs() != currentLeaseRenewal) {
            LeaseInfo newLeaseInfo = LeaseInfo.Builder.newBuilder()
                    .setRenewalIntervalInSecs(currentLeaseRenewal)
                    .setDurationInSecs(currentLeaseDuration)
                    .build();
            instanceInfo.setLeaseInfo(newLeaseInfo);
            instanceInfo.setIsDirty();
        }
    }

    public static interface StatusChangeListener {

        String getId();

        void notify(StatusChangeEvent statusChangeEvent);
    }

    public static interface InstanceStatusMapper {

        InstanceStatus map(InstanceStatus prev);
    }

}
