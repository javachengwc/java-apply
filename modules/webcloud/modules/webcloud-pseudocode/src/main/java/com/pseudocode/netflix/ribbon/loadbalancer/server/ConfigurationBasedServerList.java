package com.pseudocode.netflix.ribbon.loadbalancer.server;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.pseudocode.netflix.ribbon.core.client.config.CommonClientConfigKey;
import com.pseudocode.netflix.ribbon.core.client.config.IClientConfig;

//默认的ServerList
public class ConfigurationBasedServerList extends AbstractServerList<Server>  {

    private IClientConfig clientConfig;

    @Override
    public List<Server> getInitialListOfServers() {
        return getUpdatedListOfServers();
    }

    @Override
    public List<Server> getUpdatedListOfServers() {
        String listOfServers = clientConfig.get(CommonClientConfigKey.ListOfServers);
        return derive(listOfServers);
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    protected List<Server> derive(String value) {
        List<Server> list = Lists.newArrayList();
        if (!Strings.isNullOrEmpty(value)) {
            for (String s: value.split(",")) {
                list.add(new Server(s.trim()));
            }
        }
        return list;
    }
}
