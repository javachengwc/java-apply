package com.pseudocode.netflix.eureka.core.resources;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.pseudocode.netflix.eureka.client.appinfo.InstanceInfo;
import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import com.pseudocode.netflix.eureka.core.EurekaServerContext;
import com.pseudocode.netflix.eureka.core.cluster.protocol.ReplicationInstance;
import com.pseudocode.netflix.eureka.core.cluster.protocol.ReplicationInstanceResponse;
import com.pseudocode.netflix.eureka.core.cluster.protocol.ReplicationInstanceResponse.Builder;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//Eureka-Server 同步相关接口
@Path("/{version}/peerreplication")
@Produces({"application/xml", "application/json"})
public class PeerReplicationResource {

    private static final Logger logger = LoggerFactory.getLogger(PeerReplicationResource.class);

    private static final String REPLICATION = "true";

    private final EurekaServerConfig serverConfig;
    private final PeerAwareInstanceRegistry registry;

    PeerReplicationResource(EurekaServerContext server) {
        this.serverConfig = server.getServerConfig();
        this.registry = server.getRegistry();
    }

    public PeerReplicationResource() {
        this(EurekaServerContextHolder.getInstance().getServerContext());
    }

    @Path("batch")
    @POST
    public Response batchReplication(ReplicationList replicationList) {
        try {
            ReplicationListResponse batchResponse = new ReplicationListResponse();
            for (ReplicationInstance instanceInfo : replicationList.getReplicationList()) {
                try {
                    batchResponse.addResponse(dispatch(instanceInfo));
                } catch (Exception e) {
                    batchResponse.addResponse(new ReplicationInstanceResponse(Status.INTERNAL_SERVER_ERROR.getStatusCode(), null));
                    logger.error("{} request processing failed for batch item {}/{}",
                            instanceInfo.getAction(), instanceInfo.getAppName(), instanceInfo.getId(), e);
                }
            }
            return Response.ok(batchResponse).build();
        } catch (Throwable e) {
            logger.error("Cannot execute batch Request", e);
            return Response.status(Status.INTERNAL_SERVER_ERROR).build();
        }
    }

    private ReplicationInstanceResponse dispatch(ReplicationInstance instanceInfo) {
        ApplicationResource applicationResource = createApplicationResource(instanceInfo);
        InstanceResource resource = createInstanceResource(instanceInfo, applicationResource);

        String lastDirtyTimestamp = toString(instanceInfo.getLastDirtyTimestamp());
        String overriddenStatus = toString(instanceInfo.getOverriddenStatus());
        String instanceStatus = toString(instanceInfo.getStatus());

        Builder singleResponseBuilder = new Builder();
        switch (instanceInfo.getAction()) {
            case Register:
                singleResponseBuilder = handleRegister(instanceInfo, applicationResource);
                break;
            case Heartbeat:
                singleResponseBuilder = handleHeartbeat(serverConfig, resource, lastDirtyTimestamp, overriddenStatus, instanceStatus);
                break;
            case Cancel:
                singleResponseBuilder = handleCancel(resource);
                break;
            case StatusUpdate:
                singleResponseBuilder = handleStatusUpdate(instanceInfo, resource);
                break;
            case DeleteStatusOverride:
                singleResponseBuilder = handleDeleteStatusOverride(instanceInfo, resource);
                break;
        }
        return singleResponseBuilder.build();
    }

    ApplicationResource createApplicationResource(ReplicationInstance instanceInfo) {
        return new ApplicationResource(instanceInfo.getAppName(), serverConfig, registry);
    }

    InstanceResource createInstanceResource(ReplicationInstance instanceInfo,
                                                                      ApplicationResource applicationResource) {
        return new InstanceResource(applicationResource, instanceInfo.getId(), serverConfig, registry);
    }

    private static Builder handleRegister(ReplicationInstance instanceInfo, ApplicationResource applicationResource) {
        applicationResource.addInstance(instanceInfo.getInstanceInfo(), REPLICATION);
        return new Builder().setStatusCode(Status.OK.getStatusCode());
    }

    private static Builder handleCancel(InstanceResource resource) {
        Response response = resource.cancelLease(REPLICATION);
        return new Builder().setStatusCode(response.getStatus());
    }

    private static Builder handleHeartbeat(EurekaServerConfig config, InstanceResource resource, String lastDirtyTimestamp, String overriddenStatus, String instanceStatus) {
        Response response = resource.renewLease(REPLICATION, overriddenStatus, instanceStatus, lastDirtyTimestamp);
        int responseStatus = response.getStatus();
        Builder responseBuilder = new Builder().setStatusCode(responseStatus);

        if ("false".equals(config.getExperimental("bugfix.934"))) {
            if (responseStatus == Status.OK.getStatusCode() && response.getEntity() != null) {
                responseBuilder.setResponseEntity((InstanceInfo) response.getEntity());
            }
        } else {
            if ((responseStatus == Status.OK.getStatusCode() || responseStatus == Status.CONFLICT.getStatusCode())
                    && response.getEntity() != null) {
                responseBuilder.setResponseEntity((InstanceInfo) response.getEntity());
            }
        }
        return responseBuilder;
    }

    private static Builder handleStatusUpdate(ReplicationInstance instanceInfo, InstanceResource resource) {
        Response response = resource.statusUpdate(instanceInfo.getStatus(), REPLICATION, toString(instanceInfo.getLastDirtyTimestamp()));
        return new Builder().setStatusCode(response.getStatus());
    }

    private static Builder handleDeleteStatusOverride(ReplicationInstance instanceInfo, InstanceResource resource) {
        Response response = resource.deleteStatusUpdate(REPLICATION, instanceInfo.getStatus(),
                instanceInfo.getLastDirtyTimestamp().toString());
        return new Builder().setStatusCode(response.getStatus());
    }

    private static <T> String toString(T value) {
        if (value == null) {
            return null;
        }
        return value.toString();
    }
}
