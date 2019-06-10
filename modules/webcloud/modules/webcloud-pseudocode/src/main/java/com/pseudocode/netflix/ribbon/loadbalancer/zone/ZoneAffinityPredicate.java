package com.pseudocode.netflix.ribbon.loadbalancer.zone;

import com.pseudocode.netflix.ribbon.loadbalancer.rule.AbstractServerPredicate;
import com.pseudocode.netflix.ribbon.loadbalancer.rule.PredicateKey;
import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.netflix.config.ConfigurationManager;
import com.netflix.config.DeploymentContext;

public class ZoneAffinityPredicate extends AbstractServerPredicate {

    private final String zone = ConfigurationManager.getDeploymentContext().getValue(DeploymentContext.ContextKey.zone);

    public ZoneAffinityPredicate() {
    }

    @Override
    public boolean apply(PredicateKey input) {
        Server s = input.getServer();
        String az = s.getZone();
        //检查server的zone属性
        if (az != null && zone != null && az.toLowerCase().equals(zone.toLowerCase())) {
            return true;
        } else {
            return false;
        }
    }
}
