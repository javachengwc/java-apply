package com.solr7.search.solr;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;

import java.util.concurrent.ConcurrentHashMap;

public class SolrClientFactory {

    private static ConcurrentHashMap<String, SolrClient> solrClientMap = new ConcurrentHashMap<String, SolrClient>();

    public static SolrClient getSolrClient(String url) {
        SolrClient solrClient = solrClientMap.get(url);
        if (solrClient == null) {
            //solrClient= new HttpSolrClient.Builder(url).build();
            HttpSolrClient httpSolrClient = new HttpSolrClient.Builder(url).
                    withConnectionTimeout(5000).
                    withSocketTimeout(5000).build();
            solrClient=httpSolrClient;
            solrClientMap.putIfAbsent(url, solrClient);
        }
        return solrClient;
    }

}
