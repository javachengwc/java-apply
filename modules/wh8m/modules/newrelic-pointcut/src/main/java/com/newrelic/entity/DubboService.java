package com.newrelic.entity;

/**
 * xml文件配置中的dubbo.service节点
 */
public class DubboService {

    private String face;

    private String ref;

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DubboService that = (DubboService) o;

        if (face != null ? !face.equals(that.face) : that.face != null) return false;
        if (ref != null ? !ref.equals(that.ref) : that.ref != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = face != null ? face.hashCode() : 0;
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "DubboService{" +
                "face='" + face + '\'' +
                ", ref='" + ref + '\'' +
                '}';
    }
}
