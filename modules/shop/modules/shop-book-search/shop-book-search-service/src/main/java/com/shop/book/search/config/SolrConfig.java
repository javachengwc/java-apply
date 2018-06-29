package com.shop.book.search.config;

import org.apache.commons.lang.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@ConfigurationProperties(prefix = "solr.collect")
@Component
public class SolrConfig {

    public static ConcurrentHashMap<String,String> collectMap= new ConcurrentHashMap<String,String>();

    private SolrProp book;

    public SolrProp getBook() {
        return book;
    }

    public void setBook(SolrProp book) {
        this.book = book;
    }

    public String getBookCollect() {
       String collect= book.getName();
       return collect;
    }

    public String getBookSolrUrl() {
        String url= book.getUrl();
        return url;
    }

    public String getSolrUrlByCollect(String collect) {
        String url =collectMap.get(collect);
        if(StringUtils.isBlank(url)) {
            String bookCollect =getBookCollect();
            if(bookCollect.equalsIgnoreCase(collect)) {
                url=getBookSolrUrl();
                collectMap.putIfAbsent(bookCollect,url);
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
