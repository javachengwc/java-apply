package com.cloud.pseudocode.ribbon.core.client.config;


import java.util.Map;

public interface IClientConfig {

    String getClientName();

    String getNameSpace();

    void loadProperties(String var1);

    void loadDefaultValues();

    Map<String, Object> getProperties();

    /** @deprecated */
    @Deprecated
    void setProperty(IClientConfigKey var1, Object var2);

    /** @deprecated */
    @Deprecated
    Object getProperty(IClientConfigKey var1);

    /** @deprecated */
    @Deprecated
    Object getProperty(IClientConfigKey var1, Object var2);

    boolean containsProperty(IClientConfigKey var1);

    String resolveDeploymentContextbasedVipAddresses();

    int getPropertyAsInteger(IClientConfigKey var1, int var2);

    String getPropertyAsString(IClientConfigKey var1, String var2);

    boolean getPropertyAsBoolean(IClientConfigKey var1, boolean var2);

    <T> T get(IClientConfigKey<T> var1);

    <T> T get(IClientConfigKey<T> var1, T var2);

    <T> IClientConfig set(IClientConfigKey<T> var1, T var2);

    public static class Builder {
        private IClientConfig config;

        Builder() {
        }

        public static IClientConfig.Builder newBuilder() {
            IClientConfig.Builder builder = new IClientConfig.Builder();
            builder.config = new DefaultClientConfigImpl();
            return builder;
        }

        public static IClientConfig.Builder newBuilder(String clientName) {
            IClientConfig.Builder builder = new IClientConfig.Builder();
            builder.config = new DefaultClientConfigImpl();
            builder.config.loadProperties(clientName);
            return builder;
        }

        public static IClientConfig.Builder newBuilder(String clientName, String propertyNameSpace) {
            IClientConfig.Builder builder = new IClientConfig.Builder();
            builder.config = new DefaultClientConfigImpl(propertyNameSpace);
            builder.config.loadProperties(clientName);
            return builder;
        }

        public static IClientConfig.Builder newBuilder(Class<? extends IClientConfig> implClass, String clientName) {
            IClientConfig.Builder builder = new IClientConfig.Builder();

            try {
                builder.config = (IClientConfig)implClass.newInstance();
                builder.config.loadProperties(clientName);
                return builder;
            } catch (Exception var4) {
                throw new IllegalArgumentException(var4);
            }
        }

        public static IClientConfig.Builder newBuilder(Class<? extends IClientConfig> implClass) {
            IClientConfig.Builder builder = new IClientConfig.Builder();

            try {
                builder.config = (IClientConfig)implClass.newInstance();
                return builder;
            } catch (Exception var3) {
                throw new IllegalArgumentException(var3);
            }
        }

        public IClientConfig build() {
            return this.config;
        }

        public IClientConfig.Builder withDefaultValues() {
            this.config.loadDefaultValues();
            return this;
        }

        public IClientConfig.Builder withDeploymentContextBasedVipAddresses(String vipAddress) {
            this.config.set(CommonClientConfigKey.DeploymentContextBasedVipAddresses, vipAddress);
            return this;
        }

        public IClientConfig.Builder withForceClientPortConfiguration(boolean forceClientPortConfiguration) {
            this.config.set(CommonClientConfigKey.ForceClientPortConfiguration, forceClientPortConfiguration);
            return this;
        }

        public IClientConfig.Builder withMaxAutoRetries(int value) {
            this.config.set(CommonClientConfigKey.MaxAutoRetries, value);
            return this;
        }

        public IClientConfig.Builder withMaxAutoRetriesNextServer(int value) {
            this.config.set(CommonClientConfigKey.MaxAutoRetriesNextServer, value);
            return this;
        }

        public IClientConfig.Builder withRetryOnAllOperations(boolean value) {
            this.config.set(CommonClientConfigKey.OkToRetryOnAllOperations, value);
            return this;
        }

        public IClientConfig.Builder withRequestSpecificRetryOn(boolean value) {
            this.config.set(CommonClientConfigKey.RequestSpecificRetryOn, value);
            return this;
        }

        public IClientConfig.Builder withEnablePrimeConnections(boolean value) {
            this.config.set(CommonClientConfigKey.EnablePrimeConnections, value);
            return this;
        }

        public IClientConfig.Builder withMaxConnectionsPerHost(int value) {
            this.config.set(CommonClientConfigKey.MaxHttpConnectionsPerHost, value);
            this.config.set(CommonClientConfigKey.MaxConnectionsPerHost, value);
            return this;
        }

        public IClientConfig.Builder withMaxTotalConnections(int value) {
            this.config.set(CommonClientConfigKey.MaxTotalHttpConnections, value);
            this.config.set(CommonClientConfigKey.MaxTotalConnections, value);
            return this;
        }

        public IClientConfig.Builder withSecure(boolean secure) {
            this.config.set(CommonClientConfigKey.IsSecure, secure);
            return this;
        }

        public IClientConfig.Builder withConnectTimeout(int value) {
            this.config.set(CommonClientConfigKey.ConnectTimeout, value);
            return this;
        }

        public IClientConfig.Builder withReadTimeout(int value) {
            this.config.set(CommonClientConfigKey.ReadTimeout, value);
            return this;
        }

        public IClientConfig.Builder withConnectionManagerTimeout(int value) {
            this.config.set(CommonClientConfigKey.ConnectionManagerTimeout, value);
            return this;
        }

        public IClientConfig.Builder withFollowRedirects(boolean value) {
            this.config.set(CommonClientConfigKey.FollowRedirects, value);
            return this;
        }

        public IClientConfig.Builder withConnectionPoolCleanerTaskEnabled(boolean value) {
            this.config.set(CommonClientConfigKey.ConnectionPoolCleanerTaskEnabled, value);
            return this;
        }

        public IClientConfig.Builder withConnIdleEvictTimeMilliSeconds(int value) {
            this.config.set(CommonClientConfigKey.ConnIdleEvictTimeMilliSeconds, value);
            return this;
        }

        public IClientConfig.Builder withConnectionCleanerRepeatIntervalMills(int value) {
            this.config.set(CommonClientConfigKey.ConnectionCleanerRepeatInterval, value);
            return this;
        }

        public IClientConfig.Builder withGZIPContentEncodingFilterEnabled(boolean value) {
            this.config.set(CommonClientConfigKey.EnableGZIPContentEncodingFilter, value);
            return this;
        }

        public IClientConfig.Builder withProxyHost(String proxyHost) {
            this.config.set(CommonClientConfigKey.ProxyHost, proxyHost);
            return this;
        }

        public IClientConfig.Builder withProxyPort(int value) {
            this.config.set(CommonClientConfigKey.ProxyPort, value);
            return this;
        }

        public IClientConfig.Builder withKeyStore(String value) {
            this.config.set(CommonClientConfigKey.KeyStore, value);
            return this;
        }

        public IClientConfig.Builder withKeyStorePassword(String value) {
            this.config.set(CommonClientConfigKey.KeyStorePassword, value);
            return this;
        }

        public IClientConfig.Builder withTrustStore(String value) {
            this.config.set(CommonClientConfigKey.TrustStore, value);
            return this;
        }

        public IClientConfig.Builder withTrustStorePassword(String value) {
            this.config.set(CommonClientConfigKey.TrustStorePassword, value);
            return this;
        }

        public IClientConfig.Builder withClientAuthRequired(boolean value) {
            this.config.set(CommonClientConfigKey.IsClientAuthRequired, value);
            return this;
        }

        public IClientConfig.Builder withCustomSSLSocketFactoryClassName(String value) {
            this.config.set(CommonClientConfigKey.CustomSSLSocketFactoryClassName, value);
            return this;
        }

        public IClientConfig.Builder withHostnameValidationRequired(boolean value) {
            this.config.set(CommonClientConfigKey.IsHostnameValidationRequired, value);
            return this;
        }

        public IClientConfig.Builder ignoreUserTokenInConnectionPoolForSecureClient(boolean value) {
            this.config.set(CommonClientConfigKey.IgnoreUserTokenInConnectionPoolForSecureClient, value);
            return this;
        }

        public IClientConfig.Builder withLoadBalancerEnabled(boolean value) {
            this.config.set(CommonClientConfigKey.InitializeNFLoadBalancer, value);
            return this;
        }

        public IClientConfig.Builder withServerListRefreshIntervalMills(int value) {
            this.config.set(CommonClientConfigKey.ServerListRefreshInterval, value);
            return this;
        }

        public IClientConfig.Builder withZoneAffinityEnabled(boolean value) {
            this.config.set(CommonClientConfigKey.EnableZoneAffinity, value);
            return this;
        }

        public IClientConfig.Builder withZoneExclusivityEnabled(boolean value) {
            this.config.set(CommonClientConfigKey.EnableZoneExclusivity, value);
            return this;
        }

        public IClientConfig.Builder prioritizeVipAddressBasedServers(boolean value) {
            this.config.set(CommonClientConfigKey.PrioritizeVipAddressBasedServers, value);
            return this;
        }

        public IClientConfig.Builder withTargetRegion(String value) {
            this.config.set(CommonClientConfigKey.TargetRegion, value);
            return this;
        }
    }
}
