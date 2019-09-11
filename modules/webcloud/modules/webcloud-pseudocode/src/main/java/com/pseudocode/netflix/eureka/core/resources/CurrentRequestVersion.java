package com.pseudocode.netflix.eureka.core.resources;

import com.pseudocode.netflix.eureka.core.Version;

public final class CurrentRequestVersion {

    private static final ThreadLocal<Version> CURRENT_REQ_VERSION = new ThreadLocal<Version>();

    private CurrentRequestVersion() {
    }

    public static Version get() {
        return CURRENT_REQ_VERSION.get();
    }

    public static void set(Version version) {
        CURRENT_REQ_VERSION.set(version);
    }

}
