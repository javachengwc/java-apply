package com.component.rest.jersey.balance;

import com.component.rest.jersey.filter.BalanceClientFilter;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.PredicateKey;
import com.netflix.loadbalancer.Server;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Profile;

@Profile("microservice")
public class VersionPredicate extends AbstractServerPredicate {

    public boolean apply(PredicateKey input) {
        Server server = input.getServer();
        if (server == null || !(server instanceof DiscoveryEnabledServer)) {
            return false;
        }

        DiscoveryEnabledServer discoveryEnabledServer = (DiscoveryEnabledServer) server;
        String appName =discoveryEnabledServer.getInstanceInfo().getAppName();
        String requestedVersion = BalanceClientFilter.getVersion(appName);
        if (StringUtils.isBlank(requestedVersion)) {
            return true;
        }

        String serverVersion = discoveryEnabledServer.getInstanceInfo().getMetadata().get("version");
        boolean versionSame =requestedVersion.equalsIgnoreCase(serverVersion);
        return versionSame;
    }

}