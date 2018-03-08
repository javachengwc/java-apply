package com.solr7.index.model;

import com.solr7.index.annotation.SolrField;
import org.apache.commons.lang.builder.ToStringBuilder;

public class IndexModel {

    @SolrField("id")
    private String id;

    @SolrField("city")
    private String city;

    @SolrField("name")
    private String name;

    @SolrField("img")
    private String img;

    @SolrField("create_time")
    private String createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
