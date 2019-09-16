package com.pseudocode.cloud.ribbon;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext.ContextKey;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.pseudocode.netflix.ribbon.loadbalancer.zone.ZoneAffinityServerListFilter;

//默认过滤器
public class ZonePreferenceServerListFilter extends ZoneAffinityServerListFilter<Server> {

    private String zone;

    @Override
    public void initWithNiwsConfig(IClientConfig niwsClientConfig) {
        super.initWithNiwsConfig(niwsClientConfig);
        if (ConfigurationManager.getDeploymentContext() != null) {
            this.zone = ConfigurationManager.getDeploymentContext().getValue(ContextKey.zone);
        }
    }

    @Override
    public List<Server> getFilteredListOfServers(List<Server> servers) {
        List<Server> output = super.getFilteredListOfServers(servers);
        if (this.zone != null && output.size() == servers.size()) {
            List<Server> local = new ArrayList<>();
            for (Server server : output) {
                if (this.zone.equalsIgnoreCase(server.getZone())) {
                    local.add(server);
                }
            }
            if (!local.isEmpty()) {
                return local;
            }
        }
        return output;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

}

