package com.pseudocode.cloud.zuul.filters;


import java.util.Collection;
import java.util.List;

public interface RouteLocator {

    Collection<String> getIgnoredPaths();

    List<Route> getRoutes();

    Route getMatchingRoute(String path);

}
