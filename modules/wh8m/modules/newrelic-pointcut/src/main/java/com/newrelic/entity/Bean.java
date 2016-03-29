package com.newrelic.entity;

/**
 * xml文件配置中的bean节点
 */
public class Bean {

    private String id;

    private String clazz;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bean bean = (Bean) o;

        if (clazz != null ? !clazz.equals(bean.clazz) : bean.clazz != null) return false;
        if (id != null ? !id.equals(bean.id) : bean.id != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (clazz != null ? clazz.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Bean{" +
                "id='" + id + '\'' +
                ", clazz='" + clazz + '\'' +
                '}';
    }
}
