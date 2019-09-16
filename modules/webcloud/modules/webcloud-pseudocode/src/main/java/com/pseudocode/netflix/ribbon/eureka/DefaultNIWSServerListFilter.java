package com.pseudocode.netflix.ribbon.eureka;

import com.pseudocode.netflix.ribbon.loadbalancer.server.Server;
import com.pseudocode.netflix.ribbon.loadbalancer.zone.ZoneAffinityServerListFilter;

//NIWSServerListFilter过滤器，此过滤器跟ZoneAffinityServerListFilter作用一样
public class DefaultNIWSServerListFilter<T extends Server> extends ZoneAffinityServerListFilter<T> {

}

