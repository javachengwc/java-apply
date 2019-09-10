package com.pseudocode.cloud.zuul.filters;

public interface RefreshableRouteLocator extends RouteLocator {

    void refresh();

}
