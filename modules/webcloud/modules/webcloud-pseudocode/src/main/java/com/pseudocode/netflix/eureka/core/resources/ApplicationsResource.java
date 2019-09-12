package com.pseudocode.netflix.eureka.core.resources;

import com.pseudocode.netflix.eureka.client.appinfo.EurekaAccept;
import com.pseudocode.netflix.eureka.core.EurekaServerConfig;
import com.pseudocode.netflix.eureka.core.EurekaServerContext;
import com.pseudocode.netflix.eureka.core.Version;
import com.pseudocode.netflix.eureka.core.registry.Key;
import com.pseudocode.netflix.eureka.core.registry.Key.KeyType;
import com.pseudocode.netflix.eureka.core.registry.PeerAwareInstanceRegistry;
import com.pseudocode.netflix.eureka.core.registry.ResponseCache;
import com.pseudocode.netflix.eureka.core.registry.ResponseCacheImpl;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;


@Path("/{version}/apps")
@Produces({"application/xml", "application/json"})
public class ApplicationsResource {
    private static final String HEADER_ACCEPT = "Accept";
    private static final String HEADER_ACCEPT_ENCODING = "Accept-Encoding";
    private static final String HEADER_CONTENT_ENCODING = "Content-Encoding";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_GZIP_VALUE = "gzip";
    private static final String HEADER_JSON_VALUE = "json";

    private final EurekaServerConfig serverConfig;
    private final PeerAwareInstanceRegistry registry;
    private final ResponseCache responseCache;

    ApplicationsResource(EurekaServerContext eurekaServer) {
        this.serverConfig = eurekaServer.getServerConfig();
        this.registry = eurekaServer.getRegistry();
        this.responseCache = registry.getResponseCache();
    }

    public ApplicationsResource() {
        this(EurekaServerContextHolder.getInstance().getServerContext());
    }

    @Path("{appId}")
    public ApplicationResource getApplicationResource(
            @PathParam("version") String version,
            @PathParam("appId") String appId) {
        CurrentRequestVersion.set(Version.toEnum(version));
        return new ApplicationResource(appId, serverConfig, registry);
    }

    //全量获取注册应用请求
    @GET
    public Response getContainers(@PathParam("version") String version,
                                  @HeaderParam(HEADER_ACCEPT) String acceptHeader,
                                  @HeaderParam(HEADER_ACCEPT_ENCODING) String acceptEncoding,
                                  @HeaderParam(EurekaAccept.HTTP_X_EUREKA_ACCEPT) String eurekaAccept,
                                  @Context UriInfo uriInfo,
                                  @QueryParam("regions") String regionsStr) {

        boolean isRemoteRegionRequested = null != regionsStr && !regionsStr.isEmpty();
        String[] regions = null;
        if (!isRemoteRegionRequested) {
            EurekaMonitors.GET_ALL.increment();
        } else {
            regions = regionsStr.toLowerCase().split(",");
            Arrays.sort(regions); // So we don't have different caches for same regions queried in different order.
            EurekaMonitors.GET_ALL_WITH_REMOTE_REGIONS.increment();
        }

        //判断是否可以访问
        if (!registry.shouldAllowAccess(isRemoteRegionRequested)) {
            return Response.status(Status.FORBIDDEN).build();
        }
        //API版本
        CurrentRequestVersion.set(Version.toEnum(version));

        //返回数据格式
        KeyType keyType = Key.KeyType.JSON;
        String returnMediaType = MediaType.APPLICATION_JSON;
        if (acceptHeader == null || !acceptHeader.contains(HEADER_JSON_VALUE)) {
            keyType = Key.KeyType.XML;
            returnMediaType = MediaType.APPLICATION_XML;
        }

        //响应缓存键(KEY)
        Key cacheKey = new Key(Key.EntityType.Application, ResponseCacheImpl.ALL_APPS,
                keyType, CurrentRequestVersion.get(), EurekaAccept.fromString(eurekaAccept), regions
        );

        Response response;
        //从响应缓存读取全量注册信息
        if (acceptEncoding != null && acceptEncoding.contains(HEADER_GZIP_VALUE)) {
            response = Response.ok(responseCache.getGZIP(cacheKey))
                    .header(HEADER_CONTENT_ENCODING, HEADER_GZIP_VALUE)
                    .header(HEADER_CONTENT_TYPE, returnMediaType)
                    .build();
        } else {
            response = Response.ok(responseCache.get(cacheKey)).build();
        }
        return response;
    }

    @Path("delta")
    @GET
    public Response getContainerDifferential(
            @PathParam("version") String version,
            @HeaderParam(HEADER_ACCEPT) String acceptHeader,
            @HeaderParam(HEADER_ACCEPT_ENCODING) String acceptEncoding,
            @HeaderParam(EurekaAccept.HTTP_X_EUREKA_ACCEPT) String eurekaAccept,
            @Context UriInfo uriInfo,
            @QueryParam("regions") String regionsStr) {

        boolean isRemoteRegionRequested = null != regionsStr && !regionsStr.isEmpty();

        // If the delta flag is disabled in discovery or if the lease expiration
        // has been disabled, redirect clients to get all instances
        if ((serverConfig.shouldDisableDelta()) || (!registry.shouldAllowAccess(isRemoteRegionRequested))) {
            return Response.status(Status.FORBIDDEN).build();
        }

        String[] regions = null;
        if (!isRemoteRegionRequested) {
            EurekaMonitors.GET_ALL_DELTA.increment();
        } else {
            regions = regionsStr.toLowerCase().split(",");
            Arrays.sort(regions); // So we don't have different caches for same regions queried in different order.
            EurekaMonitors.GET_ALL_DELTA_WITH_REMOTE_REGIONS.increment();
        }

        CurrentRequestVersion.set(Version.toEnum(version));
        KeyType keyType = Key.KeyType.JSON;
        String returnMediaType = MediaType.APPLICATION_JSON;
        if (acceptHeader == null || !acceptHeader.contains(HEADER_JSON_VALUE)) {
            keyType = Key.KeyType.XML;
            returnMediaType = MediaType.APPLICATION_XML;
        }

        Key cacheKey = new Key(Key.EntityType.Application, ResponseCacheImpl.ALL_APPS_DELTA,
                keyType, CurrentRequestVersion.get(), EurekaAccept.fromString(eurekaAccept), regions
        );

        if (acceptEncoding != null && acceptEncoding.contains(HEADER_GZIP_VALUE)) {
            return Response.ok(responseCache.getGZIP(cacheKey))
                    .header(HEADER_CONTENT_ENCODING, HEADER_GZIP_VALUE)
                    .header(HEADER_CONTENT_TYPE, returnMediaType)
                    .build();
        } else {
            return Response.ok(responseCache.get(cacheKey)).build();
        }
    }
}
