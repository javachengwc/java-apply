package com.pseudocode.netflix.eureka.client.appinfo;


import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import com.pseudocode.netflix.commons.util.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractInstanceConfig implements EurekaInstanceConfig {

    private static final Logger logger = LoggerFactory.getLogger(AbstractInstanceConfig.class);

    //契约过期时间，单位：秒
    private static final int LEASE_EXPIRATION_DURATION_SECONDS = 90;

    //租约续约频率，单位：秒
    private static final int LEASE_RENEWAL_INTERVAL_SECONDS = 30;

    //应用 https 端口关闭
    private static final boolean SECURE_PORT_ENABLED = false;

    //应用 http 端口开启
    private static final boolean NON_SECURE_PORT_ENABLED = true;

    //应用 http 端口
    private static final int NON_SECURE_PORT = 80;

    //应用 https 端口
    private static final int SECURE_PORT = 443;

    private static final boolean INSTANCE_ENABLED_ON_INIT = false;

    //主机信息
    private static final Pair<String, String> hostInfo = getHostInfo();

    //数据中心信息
    private DataCenterInfo info = new DataCenterInfo() {
        @Override
        public Name getName() {
            return Name.MyOwn;
        }
    };

    protected AbstractInstanceConfig() {

    }

    protected AbstractInstanceConfig(DataCenterInfo info) {
        this.info = info;
    }

    @Override
    public boolean isInstanceEnabledOnit() {
        return INSTANCE_ENABLED_ON_INIT;
    }

    @Override
    public int getNonSecurePort() {
        return NON_SECURE_PORT;
    }

    @Override
    public int getSecurePort() {
        return SECURE_PORT;
    }

    @Override
    public boolean isNonSecurePortEnabled() {
        return NON_SECURE_PORT_ENABLED;
    }

    @Override
    public boolean getSecurePortEnabled() {
        // TODO Auto-generated method stub
        return SECURE_PORT_ENABLED;
    }

    @Override
    public int getLeaseRenewalIntervalInSeconds() {
        return LEASE_RENEWAL_INTERVAL_SECONDS;
    }

    @Override
    public int getLeaseExpirationDurationInSeconds() {
        return LEASE_EXPIRATION_DURATION_SECONDS;
    }

    @Override
    public String getVirtualHostName() {
        return (getHostName(false) + ":" + getNonSecurePort());
    }

    @Override
    public String getSecureVirtualHostName() {
        return (getHostName(false) + ":" + getSecurePort());
    }

    @Override
    public String getASGName() {
        return null;
    }

    @Override
    public String getHostName(boolean refresh) {
        return hostInfo.second();
    }

    @Override
    public Map<String, String> getMetadataMap() {
        return null;
    }

    @Override
    public DataCenterInfo getDataCenterInfo() {
        return info;
    }

    @Override
    public String getIpAddress() {
        return hostInfo.first();
    }

    private static Pair<String, String> getHostInfo() {
        Pair<String, String> pair;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            pair = new Pair<String, String>(localHost.getHostAddress(), localHost.getHostName());
            //手动配置本机的 hostname + etc/hosts 文件，从而映射主机名和 IP 地址
        } catch (UnknownHostException e) {
            logger.error("Cannot get host info", e);
            pair = new Pair<String, String>("", "");
        }
        return pair;
    }

}

