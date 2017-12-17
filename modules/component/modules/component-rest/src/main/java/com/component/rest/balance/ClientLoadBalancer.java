package com.component.rest.balance;

import com.component.rest.ResourceLocator;
import com.component.rest.enums.RouteResultEnum;
import com.component.rest.filter.BalanceClientFilter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientRequestContext;
import java.net.URI;
import java.net.URISyntaxException;

@Component
@Profile("microservice")
public class ClientLoadBalancer {

    private static Logger logger = LoggerFactory.getLogger(ClientLoadBalancer.class);

    private static String DEFAULT_HOST = "localhost";
    private static Integer DEFAULT_PORT = 80;
    private static String APIGATEWAY_GROUP = "groupName";

    @Autowired
    private LoadBalancerClient loadBalancer;

    @Autowired
    private ResourceLocator resourceLocator;

    public ClientLoadBalancer() {
    }

    public RouteResultEnum execute(ClientRequestContext requestContext) {

        if (loadBalancer == null || requestContext == null) {
            logger.warn("ClientLoadBalancer execute loadBalancer is null, return route fail");
            return RouteResultEnum.RouteFail;
        }

        URI uri = requestContext.getUri();
        if (uri == null) {
            logger.warn("ClientLoadBalancer execute uri is null");
            return RouteResultEnum.RouteFail;
        }

        String serviceName = BalanceClientFilter.getServiceName(uri.getHost());
        if (StringUtils.isBlank(serviceName)) {
            logger.warn("ClientLoadBalancer execute serviceName is null");
            return RouteResultEnum.RouteFail;
        }
        logger.info("ClientLoadBalancer execute serviceName={},uri={}",serviceName,uri);

        URI newUri = null;
        ServiceInstance serverInstance = loadBalancer.choose(serviceName);
        if (serverInstance != null) {
            logger.info("ClientLoadBalancer execute loadBalancer choose serviceInfo serviceName={},host={},port={}",
                    serviceName,serverInstance.getHost(),serverInstance.getPort());

            String apiGatewayGroup = serverInstance.getMetadata().get(APIGATEWAY_GROUP);
            if (!StringUtils.isBlank(apiGatewayGroup) && !apiGatewayGroup.equalsIgnoreCase(BalanceClientFilter.getApiGatewayGroup())) {
                //不同的服务组
                ServiceInstance apiGatewayInstance = loadBalancer.choose(apiGatewayGroup);
                newUri = reconstructUriByApiGateway(apiGatewayInstance, uri);
                logger.info("ClientLoadBalancer execute  reconstructUriByApiGateway serviceName={},gatewayInto gateway host={},port={} ",
                        serviceName,apiGatewayInstance.getHost(),apiGatewayInstance.getPort());
            }

            if (newUri == null) {
                newUri = loadBalancer.reconstructURI(serverInstance, uri);
            }
        }

        if (newUri == null) {
            URI localUri=reconstructLocalUri(uri);
            logger.info("ClientLoadBalancer execute routing not find micro service, try to by local," +
                    " serviceName={}, localUri={}",serviceName,localUri);
            requestContext.setUri(localUri);
            return RouteResultEnum.RouteByLocal;
        } else {
            requestContext.setUri(newUri);
            logger.info("ClientLoadBalancer execute routing by micro, serviceName={},newUri={}",serviceName,newUri);
            return RouteResultEnum.RouteByMicro;
        }
    }

    //从本地配置重构uri
    private URI reconstructLocalUri(URI uri) {
        String host = DEFAULT_HOST;
        int port = DEFAULT_PORT;
        String serviceName = uri.getHost();
        String localUrl = resourceLocator.locate(serviceName);

        logger.warn(String.format("ClientLoadBalancer reconstLocalUri serviceName={} found in local config file" +
                " with localUrl={}", serviceName,localUrl));
        try {
            URI tmpUri = new URI(localUrl);
            host = tmpUri.getHost();
            port = tmpUri.getPort();
        } catch (URISyntaxException e) {
            logger.warn("ClientLoadBalancer reconstLocalUri expand localUrl error,localUrl={}",localUrl,e);
        }

        try {
            URI rtUri= new URI("http", uri.getUserInfo(), host, port, uri.getPath(), uri.getQuery(),uri.getFragment());
            return rtUri;
        } catch (URISyntaxException e) {
            logger.error("ClientLoadBalancer reconstLocalUri reconst error,uri={}",uri, e);
            return uri;
        }
    }

    //从apiGateway重构uri
    public URI reconstructUriByApiGateway(ServiceInstance server, URI orglUri) {
        String host = server.getHost();
        int port = server .getPort();
        if (host.equals(orglUri.getHost())  && port == orglUri.getPort()) {
            return orglUri;
        }
        String scheme = orglUri.getScheme();
        if (scheme == null) {
            return null;
        }
        try {
            StringBuilder sb = new StringBuilder();
            sb.append(scheme).append("://");
            if (!StringUtils.isBlank(orglUri.getRawUserInfo())) {
                sb.append(orglUri.getRawUserInfo()).append("@");
            }
            sb.append(host);
            if (port >= 0) {
                sb.append(":").append(port);
            }
            sb.append("/");
            sb.append(orglUri.getHost().toLowerCase());
            sb.append(orglUri.getRawPath());

            String queryStr=orglUri.getRawQuery();
            if (!StringUtils.isBlank(queryStr)) {
                sb.append("?").append(queryStr);
            }

            String fragment=orglUri.getRawFragment();
            if (!StringUtils.isBlank(fragment)) {
                sb.append("#").append(fragment);
            }

            URI newURI = new URI(sb.toString());
            return newURI;
        } catch (URISyntaxException e) {
            logger.error("ClientLoadBalancer reconstUriByApiGateway error,orglUri={}", orglUri, e);
            throw new RuntimeException(e);
        }
    }

}
