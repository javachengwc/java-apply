package com.pseudocode.netflix.eureka.core.cluster;

import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PeerEurekaNode {

    private static final long RETRY_SLEEP_TIME_MS = 100;

    private static final long SERVER_UNAVAILABLE_SLEEP_TIME_MS = 1000;

    private static final long MAX_BATCHING_DELAY_MS = 500;

    private static final int BATCH_SIZE = 250;

    private static final Logger logger = LoggerFactory.getLogger(PeerEurekaNode.class);

    public static final String BATCH_URL_PATH = "peerreplication/batch/";

    public static final String HEADER_REPLICATION = "x-netflix-discovery-replication";

    private  String serviceUrl;

    private  EurekaServerConfig config;

    private  long maxProcessingDelayMs;

    private  PeerAwareInstanceRegistry registry;

    private  String targetHost;

}
