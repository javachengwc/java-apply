package com.micro.apollo.pseudocode.core.apollo.core.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ApolloConfig {

    private String appId;

    private String cluster;

    private String namespaceName;

    private Map<String, String> configurations;

    private String releaseKey;

    public ApolloConfig() {
    }

    public ApolloConfig(String appId,
                        String cluster,
                        String namespaceName,
                        String releaseKey) {
        this.appId = appId;
        this.cluster = cluster;
        this.namespaceName = namespaceName;
        this.releaseKey = releaseKey;
    }
}
