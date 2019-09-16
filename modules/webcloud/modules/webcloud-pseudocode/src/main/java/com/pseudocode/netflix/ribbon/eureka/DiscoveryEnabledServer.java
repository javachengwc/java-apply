package com.pseudocode.netflix.ribbon.eureka;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.PortType;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;

@SuppressWarnings({"EQ_DOESNT_OVERRIDE_EQUALS"})
public class DiscoveryEnabledServer extends Server {

    private final InstanceInfo instanceInfo;
    private final MetaInfo serviceInfo;

    public DiscoveryEnabledServer(final InstanceInfo instanceInfo, boolean useSecurePort) {
        this(instanceInfo, useSecurePort, false);
    }

    public DiscoveryEnabledServer(final InstanceInfo instanceInfo, boolean useSecurePort, boolean useIpAddr) {
        super(useIpAddr ? instanceInfo.getIPAddr() : instanceInfo.getHostName(), instanceInfo.getPort());
        if(useSecurePort && instanceInfo.isPortEnabled(PortType.SECURE))
            super.setPort(instanceInfo.getSecurePort());
        this.instanceInfo = instanceInfo;
        this.serviceInfo = new MetaInfo() {
            @Override
            public String getAppName() {
                return instanceInfo.getAppName();
            }

            @Override
            public String getServerGroup() {
                return instanceInfo.getASGName();
            }

            @Override
            public String getServiceIdForDiscovery() {
                return instanceInfo.getVIPAddress();
            }

            @Override
            public String getInstanceId() {
                return instanceInfo.getId();
            }
        };
    }

    public InstanceInfo getInstanceInfo() {
        return instanceInfo;
    }

    @Override
    public MetaInfo getMetaInfo() {
        return serviceInfo;
    }
}
