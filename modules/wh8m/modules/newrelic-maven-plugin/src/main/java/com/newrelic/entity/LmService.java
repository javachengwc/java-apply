package com.newrelic.entity;

/**
 * xml文件配置中的lm.service节点
 */
public class LmService {

    private String id;

    private String ref;

    private String api;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LmService that = (LmService) o;

        if (api != null ? !api.equals(that.api) : that.api != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        result = 31 * result + (api != null ? api.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LmService{" +
                "id='" + id + '\'' +
                ", ref='" + ref + '\'' +
                ", api='" + api + '\'' +
                '}';
    }
}
