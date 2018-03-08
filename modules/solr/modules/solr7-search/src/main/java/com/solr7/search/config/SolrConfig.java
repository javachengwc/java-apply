package com.solr7.search.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@ConfigurationProperties(prefix = "solr.collect")
@Component
public class SolrConfig {

    public static ConcurrentHashMap<String,String> collectMap= new ConcurrentHashMap<String,String>();

    private SolrProp demo;

    public SolrProp getDemo() {
        return demo;
    }

    public void setDemo(SolrProp demo) {
        this.demo = demo;
    }

    public String getDemoCollectName() {
       String name= demo.getName();
       return name;
    }

    public String getDemoSolrUrl() {
        String url= demo.getUrl();
        return url;
    }

    public String getSolrUrlByName(String collect) {
        String url =collectMap.get(collect);
        if(StringUtils.isBlank(url)) {
            String demoCollect =getDemoCollectName();
            if(demoCollect.equalsIgnoreCase(collect)) {
                url=getDemoSolrUrl();
                collectMap.putIfAbsent(demoCollect,url);
            }
        }
        return url;
    }

    public static class SolrProp {

        private String name;

        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
