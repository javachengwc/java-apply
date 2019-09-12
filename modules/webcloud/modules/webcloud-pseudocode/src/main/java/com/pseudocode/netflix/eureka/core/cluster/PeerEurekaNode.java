package com.pseudocode.netflix.eureka.core.cluster;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo.InstanceStatus;
import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaHttpResponse;
import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import com.pseudocode.netflix.eureka.core.lease.Lease;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistryImpl.Action;
import com.pseudocode.netflix.eureka.core.util.batcher.TaskDispatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;

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

    private final HttpReplicationClient replicationClient;

    private final TaskDispatcher<String, ReplicationTask> batchingDispatcher;
    private final TaskDispatcher<String, ReplicationTask> nonBatchingDispatcher;

    public PeerEurekaNode(PeerAwareInstanceRegistry registry, String targetHost, String serviceUrl, HttpReplicationClient replicationClient, EurekaServerConfig config) {
        this(registry, targetHost, serviceUrl, replicationClient, config, BATCH_SIZE, MAX_BATCHING_DELAY_MS, RETRY_SLEEP_TIME_MS, SERVER_UNAVAILABLE_SLEEP_TIME_MS);
    }

    PeerEurekaNode(PeerAwareInstanceRegistry registry, String targetHost, String serviceUrl,
                                     HttpReplicationClient replicationClient, EurekaServerConfig config,
                                     int batchSize, long maxBatchingDelayMs,
                                     long retrySleepTimeMs, long serverUnavailableSleepTimeMs) {
        this.registry = registry;
        this.targetHost = targetHost;
        this.replicationClient = replicationClient;

        this.serviceUrl = serviceUrl;
        this.config = config;
        this.maxProcessingDelayMs = config.getMaxTimeForReplication();

        String batcherName = getBatcherName();
        ReplicationTaskProcessor taskProcessor = new ReplicationTaskProcessor(targetHost, replicationClient);
        this.batchingDispatcher = TaskDispatchers.createBatchingTaskDispatcher(
                batcherName,
                config.getMaxElementsInPeerReplicationPool(),
                batchSize,
                config.getMaxThreadsForPeerReplication(),
                maxBatchingDelayMs,
                serverUnavailableSleepTimeMs,
                retrySleepTimeMs,
                taskProcessor
        );
        this.nonBatchingDispatcher = TaskDispatchers.createNonBatchingTaskDispatcher(
                targetHost,
                config.getMaxElementsInStatusReplicationPool(),
                config.getMaxThreadsForStatusReplication(),
                maxBatchingDelayMs,
                serverUnavailableSleepTimeMs,
                retrySleepTimeMs,
                taskProcessor
        );
    }

    public void register(final InstanceInfo info) throws Exception {
        long expiryTime = System.currentTimeMillis() + getLeaseRenewalOf(info);
        batchingDispatcher.process(
                taskId("register", info),
                new InstanceReplicationTask(targetHost, Action.Register, info, null, true) {
                    public EurekaHttpResponse<Void> execute() {
                        return replicationClient.register(info);
                    }
                },
                expiryTime
        );
    }

    public void cancel(final String appName, final String id) throws Exception {
        long expiryTime = System.currentTimeMillis() + maxProcessingDelayMs;
        batchingDispatcher.process(
                taskId("cancel", appName, id),
                new InstanceReplicationTask(targetHost, Action.Cancel, appName, id) {
                    @Override
                    public EurekaHttpResponse<Void> execute() {
                        return replicationClient.cancel(appName, id);
                    }

                    @Override
                    public void handleFailure(int statusCode, Object responseEntity) throws Throwable {
                        super.handleFailure(statusCode, responseEntity);
                        if (statusCode == 404) {
                            logger.warn("{}: missing entry.", getTaskName());
                        }
                    }
                },
                expiryTime
        );
    }

    public void heartbeat(final String appName, final String id,
                          final InstanceInfo info, final InstanceInfo.InstanceStatus overriddenStatus,
                          boolean primeConnection) throws Throwable {
        if (primeConnection) {
            // We do not care about the result for priming request.
            replicationClient.sendHeartBeat(appName, id, info, overriddenStatus);
            return;
        }
        ReplicationTask replicationTask = new InstanceReplicationTask(targetHost, Action.Heartbeat, info, overriddenStatus, false) {
            @Override
            public EurekaHttpResponse<InstanceInfo> execute() throws Throwable {
                return replicationClient.sendHeartBeat(appName, id, info, overriddenStatus);
            }

            @Override
            public void handleFailure(int statusCode, Object responseEntity) throws Throwable {
                super.handleFailure(statusCode, responseEntity);
                if (statusCode == 404) {
                    logger.warn("{}: missing entry.", getTaskName());
                    if (info != null) {
                        logger.warn("{}: cannot find instance id {} and hence replicating the instance with status {}",
                                getTaskName(), info.getId(), info.getStatus());
                        register(info);
                    }
                } else if (config.shouldSyncWhenTimestampDiffers()) {
                    InstanceInfo peerInstanceInfo = (InstanceInfo) responseEntity;
                    if (peerInstanceInfo != null) {
                        syncInstancesIfTimestampDiffers(appName, id, info, peerInstanceInfo);
                    }
                }
            }
        };
        long expiryTime = System.currentTimeMillis() + getLeaseRenewalOf(info);
        batchingDispatcher.process(taskId("heartbeat", info), replicationTask, expiryTime);
    }

    public void statusUpdate(final String asgName, final ASGStatus newStatus) {
        long expiryTime = System.currentTimeMillis() + maxProcessingDelayMs;
        nonBatchingDispatcher.process(
                asgName,
                new AsgReplicationTask(targetHost, Action.StatusUpdate, asgName, newStatus) {
                    public EurekaHttpResponse<?> execute() {
                        return replicationClient.statusUpdate(asgName, newStatus);
                    }
                },
                expiryTime
        );
    }

    public void statusUpdate(final String appName, final String id,
                             final InstanceStatus newStatus, final InstanceInfo info) {
        long expiryTime = System.currentTimeMillis() + maxProcessingDelayMs;
        batchingDispatcher.process(
                taskId("statusUpdate", appName, id),
                new InstanceReplicationTask(targetHost, Action.StatusUpdate, info, null, false) {
                    @Override
                    public EurekaHttpResponse<Void> execute() {
                        return replicationClient.statusUpdate(appName, id, newStatus, info);
                    }
                },
                expiryTime
        );
    }

    public void deleteStatusOverride(final String appName, final String id, final InstanceInfo info) {
        long expiryTime = System.currentTimeMillis() + maxProcessingDelayMs;
        batchingDispatcher.process(
                taskId("deleteStatusOverride", appName, id),
                new InstanceReplicationTask(targetHost, Action.DeleteStatusOverride, info, null, false) {
                    @Override
                    public EurekaHttpResponse<Void> execute() {
                        return replicationClient.deleteStatusOverride(appName, id, info);
                    }
                },
                expiryTime);
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void shutDown() {
        batchingDispatcher.shutdown();
        nonBatchingDispatcher.shutdown();
    }

    private void syncInstancesIfTimestampDiffers(String appName, String id, InstanceInfo info, InstanceInfo infoFromPeer) {
        try {
            if (infoFromPeer != null) {
                logger.warn("Peer wants us to take the instance information from it, since the timestamp differs,"
                        + "Id : {} My Timestamp : {}, Peer's timestamp: {}", id, info.getLastDirtyTimestamp(), infoFromPeer.getLastDirtyTimestamp());

                if (infoFromPeer.getOverriddenStatus() != null && !InstanceStatus.UNKNOWN.equals(infoFromPeer.getOverriddenStatus())) {
                    logger.warn("Overridden Status info -id {}, mine {}, peer's {}", id, info.getOverriddenStatus(), infoFromPeer.getOverriddenStatus());
                    registry.storeOverriddenStatusIfRequired(appName, id, infoFromPeer.getOverriddenStatus());
                }
                registry.register(infoFromPeer, true);
            }
        } catch (Throwable e) {
            logger.warn("Exception when trying to set information from peer :", e);
        }
    }

    public String getBatcherName() {
        String batcherName;
        try {
            batcherName = new URL(serviceUrl).getHost();
        } catch (MalformedURLException e1) {
            batcherName = serviceUrl;
        }
        return "target_" + batcherName;
    }

    //生成同步操作任务编号
    private static String taskId(String requestType, String appName, String id) {
        return requestType + '#' + appName + '/' + id;
    }

    private static String taskId(String requestType, InstanceInfo info) {
        return taskId(requestType, info.getAppName(), info.getId());
    }

    private static int getLeaseRenewalOf(InstanceInfo info) {
        return (info.getLeaseInfo() == null ? Lease.DEFAULT_DURATION_IN_SECS : info.getLeaseInfo().getRenewalIntervalInSecs()) * 1000;
    }
}
