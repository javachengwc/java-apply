package com.solr.util;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;

import java.util.HashMap;
import java.util.Map;

public class SolrServerFactory {

    private static Map<String, SolrServer> serverMap = new HashMap<String, SolrServer>();

    public static SolrServer getSolrServerInstance(String url) {
        HttpSolrServer server = (HttpSolrServer) serverMap.get(url);
        if (server == null) {
            server = new HttpSolrServer(url);
            // 5 seconds to establish TCP
            server.setConnectionTimeout(5000);
            server.setSoTimeout(3000); // socket read timeout
            server.setDefaultMaxConnectionsPerHost(128);
            server.setMaxTotalConnections(512);
            server.setFollowRedirects(false); // defaults to false
            // allowCompression defaults to false.
            // Server side must support gzip or deflate for this to have any effect.
            server.setAllowCompression(true);
            //System.out.println(server.);
            serverMap.put(url, server);
        }
        return server;
    }

}
