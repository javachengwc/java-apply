package com.pseudocode.netflix.eureka.core.cluster;

import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaHttpClient;
import com.pseudocode.netflix.eureka.client.discovery.shared.transport.EurekaHttpResponse;

public interface HttpReplicationClient extends EurekaHttpClient {

    EurekaHttpResponse<Void> statusUpdate(String asgName, ASGStatus newStatus);

    EurekaHttpResponse<ReplicationListResponse> submitBatchUpdates(ReplicationList replicationList);

}

