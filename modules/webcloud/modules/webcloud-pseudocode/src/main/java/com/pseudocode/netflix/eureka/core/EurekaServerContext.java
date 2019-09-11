package com.pseudocode.netflix.eureka.core;

import com.pseudocode.netflix.eureka.core.cluster.PeerEurekaNodes;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;

public abstract interface EurekaServerContext
{
    public abstract void initialize() throws Exception;

    public abstract void shutdown() throws Exception;

    public abstract EurekaServerConfig getServerConfig();

    public abstract PeerEurekaNodes getPeerEurekaNodes();

//    public abstract ServerCodecs getServerCodecs();

    public abstract PeerAwareInstanceRegistry getRegistry();

//    public abstract ApplicationInfoManager getApplicationInfoManager();

}
