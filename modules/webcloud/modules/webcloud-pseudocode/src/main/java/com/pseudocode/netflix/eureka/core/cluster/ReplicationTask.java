package com.pseudocode.netflix.eureka.core.cluster;


import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaHttpResponse;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistryImpl.Action;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Eureka server同步任务
abstract class ReplicationTask {

    private static final Logger logger = LoggerFactory.getLogger(ReplicationTask.class);

    protected final String peerNodeName;
    protected final Action action;

    ReplicationTask(String peerNodeName, Action action) {
        this.peerNodeName = peerNodeName;
        this.action = action;
    }

    public abstract String getTaskName();

    public Action getAction() {
        return action;
    }

    public abstract EurekaHttpResponse<?> execute() throws Throwable;

    public void handleSuccess() {
    }

    public void handleFailure(int statusCode, Object responseEntity) throws Throwable {
        logger.warn("The replication of task {} failed with response code {}", getTaskName(), statusCode);
    }
}

