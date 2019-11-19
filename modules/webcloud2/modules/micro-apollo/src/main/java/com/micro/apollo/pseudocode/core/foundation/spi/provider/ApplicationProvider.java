package com.micro.apollo.pseudocode.core.foundation.spi.provider;

import java.io.InputStream;

public interface ApplicationProvider extends Provider {

    public String getAppId();

    public boolean isAppIdSet();

    public void initialize(InputStream in);
}
