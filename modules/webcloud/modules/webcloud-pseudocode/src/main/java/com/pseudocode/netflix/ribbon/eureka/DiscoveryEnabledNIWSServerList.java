package com.pseudocode.netflix.ribbon.eureka;

import com.netflix.config.ConfigurationManager;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import com.pseudocode.netflix.eureka.client.discovery.DiscoveryClient;
import com.pseudocode.netflix.eureka.client.discovery.EurekaClient;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.DefaultClientConfigImpl;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfigKey.Keys;
import com.pseudocode.netflix.ribbon.loadbalancer.server.AbstractServerList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.ws.Provider;
import java.util.ArrayList;
import java.util.List;

public class DiscoveryEnabledNIWSServerList extends AbstractServerList<DiscoveryEnabledServer> {

    private static final Logger logger = LoggerFactory.getLogger(DiscoveryEnabledNIWSServerList.class);

    String clientName;
    String vipAddresses;
    boolean isSecure = false;

    boolean prioritizeVipAddressBasedServers = true;

    String datacenter;
    String targetRegion;

    int overridePort = DefaultClientConfigImpl.DEFAULT_PORT;
    boolean shouldUseOverridePort = false;
    boolean shouldUseIpAddr = false;

    private final Provider<EurekaClient> eurekaClientProvider;

    /**
     * @deprecated use {@link #DiscoveryEnabledNIWSServerList(String)}
     * or {@link #DiscoveryEnabledNIWSServerList(IClientConfig)}
     */
    @Deprecated
    public DiscoveryEnabledNIWSServerList() {
        this.eurekaClientProvider = new LegacyEurekaClientProvider();
    }

    /**
     * @deprecated
     * use {@link #DiscoveryEnabledNIWSServerList(String, javax.inject.Provider)}
     * @param vipAddresses
     */
    @Deprecated
    public DiscoveryEnabledNIWSServerList(String vipAddresses) {
        this(vipAddresses, new LegacyEurekaClientProvider());
    }

    /**
     * @deprecated
     * use {@link #DiscoveryEnabledNIWSServerList(com.netflix.client.config.IClientConfig, javax.inject.Provider)}
     * @param clientConfig
     */
    @Deprecated
    public DiscoveryEnabledNIWSServerList(IClientConfig clientConfig) {
        this(clientConfig, new LegacyEurekaClientProvider());
    }

    public DiscoveryEnabledNIWSServerList(String vipAddresses, Provider<EurekaClient> eurekaClientProvider) {
        this(createClientConfig(vipAddresses), eurekaClientProvider);
    }

    public DiscoveryEnabledNIWSServerList(IClientConfig clientConfig, Provider<EurekaClient> eurekaClientProvider) {
        this.eurekaClientProvider = eurekaClientProvider;
        initWithNiwsConfig(clientConfig);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        clientName = clientConfig.getClientName();
        vipAddresses = clientConfig.resolveDeploymentContextbasedVipAddresses();
        if (vipAddresses == null &&
                ConfigurationManager.getConfigInstance().getBoolean("DiscoveryEnabledNIWSServerList.failFastOnNullVip", true)) {
            throw new NullPointerException("VIP address for client " + clientName + " is null");
        }
        isSecure = Boolean.parseBoolean(""+clientConfig.getProperty(CommonClientConfigKey.IsSecure, "false"));
        prioritizeVipAddressBasedServers = Boolean.parseBoolean(""+clientConfig.getProperty(CommonClientConfigKey.PrioritizeVipAddressBasedServers, prioritizeVipAddressBasedServers));
        datacenter = ConfigurationManager.getDeploymentContext().getDeploymentDatacenter();
        targetRegion = (String) clientConfig.getProperty(CommonClientConfigKey.TargetRegion);

        shouldUseIpAddr = clientConfig.getPropertyAsBoolean(CommonClientConfigKey.UseIPAddrForServer, DefaultClientConfigImpl.DEFAULT_USEIPADDRESS_FOR_SERVER);

        // override client configuration and use client-defined port
        if(clientConfig.getPropertyAsBoolean(CommonClientConfigKey.ForceClientPortConfiguration, false)){

            if(isSecure){

                if(clientConfig.containsProperty(CommonClientConfigKey.SecurePort)){

                    overridePort = clientConfig.getPropertyAsInteger(CommonClientConfigKey.SecurePort, DefaultClientConfigImpl.DEFAULT_PORT);
                    shouldUseOverridePort = true;

                }else{
                    logger.warn(clientName + " set to force client port but no secure port is set, so ignoring");
                }
            }else{

                if(clientConfig.containsProperty(CommonClientConfigKey.Port)){

                    overridePort = clientConfig.getPropertyAsInteger(CommonClientConfigKey.Port, DefaultClientConfigImpl.DEFAULT_PORT);
                    shouldUseOverridePort = true;

                }else{
                    logger.warn(clientName + " set to force client port but no port is set, so ignoring");
                }
            }
        }
    }

    @Override
    public List<DiscoveryEnabledServer> getInitialListOfServers(){
        return obtainServersViaDiscovery();
    }

    @Override
    public List<DiscoveryEnabledServer> getUpdatedListOfServers(){
        return obtainServersViaDiscovery();
    }

    //获取注册的服务列表
    private List<DiscoveryEnabledServer> obtainServersViaDiscovery() {
        List<DiscoveryEnabledServer> serverList = new ArrayList<DiscoveryEnabledServer>();

        if (eurekaClientProvider == null || eurekaClientProvider.get() == null) {
            logger.warn("EurekaClient has not been initialized yet, returning an empty list");
            return new ArrayList<DiscoveryEnabledServer>();
        }

        //eurekaClient客户端
        EurekaClient eurekaClient = eurekaClientProvider.get();
        if (vipAddresses!=null){
            for (String vipAddress : vipAddresses.split(",")) {
                // if targetRegion is null, it will be interpreted as the same region of client
                List<InstanceInfo> listOfInstanceInfo = eurekaClient.getInstancesByVipAddress(vipAddress, isSecure, targetRegion);
                for (InstanceInfo ii : listOfInstanceInfo) {
                    if (ii.getStatus().equals(InstanceStatus.UP)) {

                        if(shouldUseOverridePort){
                            if(logger.isDebugEnabled()){
                                logger.debug("Overriding port on client name: " + clientName + " to " + overridePort);
                            }

                            // copy is necessary since the InstanceInfo builder just uses the original reference,
                            // and we don't want to corrupt the global eureka copy of the object which may be
                            // used by other clients in our system
                            InstanceInfo copy = new InstanceInfo(ii);

                            if(isSecure){
                                ii = new InstanceInfo.Builder(copy).setSecurePort(overridePort).build();
                            }else{
                                ii = new InstanceInfo.Builder(copy).setPort(overridePort).build();
                            }
                        }

                        DiscoveryEnabledServer des = new DiscoveryEnabledServer(ii, isSecure, shouldUseIpAddr);
                        des.setZone(DiscoveryClient.getZone(ii));
                        serverList.add(des);
                    }
                }
                if (serverList.size()>0 && prioritizeVipAddressBasedServers){
                    break; // if the current vipAddress has servers, we dont use subsequent vipAddress based servers
                }
            }
        }
        return serverList;
    }

    public String getVipAddresses() {
        return vipAddresses;
    }

    public void setVipAddresses(String vipAddresses) {
        this.vipAddresses = vipAddresses;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder("DiscoveryEnabledNIWSServerList:");
        sb.append("; clientName:").append(clientName);
        sb.append("; Effective vipAddresses:").append(vipAddresses);
        sb.append("; isSecure:").append(isSecure);
        sb.append("; datacenter:").append(datacenter);
        return sb.toString();
    }

    private static IClientConfig createClientConfig(String vipAddresses) {
        IClientConfig clientConfig = DefaultClientConfigImpl.getClientConfigWithDefaultValues();
        clientConfig.set(Keys.DeploymentContextBasedVipAddresses, vipAddresses);
        return clientConfig;
    }
}
