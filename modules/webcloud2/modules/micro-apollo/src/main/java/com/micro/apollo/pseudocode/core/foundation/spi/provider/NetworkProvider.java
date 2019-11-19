package com.micro.apollo.pseudocode.core.foundation.spi.provider;

public interface NetworkProvider extends Provider {

    public String getHostAddress();

    public String getHostName();
}
