package com.pseudocode.netflix.eureka.client.appinfo.providers;

public interface VipAddressResolver {

    String resolveDeploymentContextBasedVipAddresses(String vipAddressMacro);
}
