package com.pseudocode.cloud.zuul.filters.route;


import com.pseudocode.netflix.hystrix.core.HystrixExecutable;
import org.springframework.http.client.ClientHttpResponse;

public interface RibbonCommand extends HystrixExecutable<ClientHttpResponse> {

}
