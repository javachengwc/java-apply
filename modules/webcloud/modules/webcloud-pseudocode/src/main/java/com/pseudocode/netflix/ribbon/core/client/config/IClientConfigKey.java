package com.pseudocode.netflix.ribbon.core.client.config;

public interface IClientConfigKey<T> {
    String key();

    Class<T> type();

    public static final class Keys extends CommonClientConfigKey {
        private Keys(String configKey) {
            super(configKey);
        }
    }
}
