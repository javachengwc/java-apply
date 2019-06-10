package com.pseudocode.cloud.ribbon;


import java.util.Arrays;
import java.util.Objects;
import com.pseudocode.cloud.context.named.NamedContextFactory.Specification;

public class RibbonClientSpecification implements Specification {
    private String name;
    private Class<?>[] configuration;

    public RibbonClientSpecification() {
    }

    public RibbonClientSpecification(String name, Class<?>[] configuration) {
        this.name = name;
        this.configuration = configuration;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Class<?>[] getConfiguration() {
        return this.configuration;
    }

    public void setConfiguration(Class<?>[] configuration) {
        this.configuration = configuration;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            RibbonClientSpecification that = (RibbonClientSpecification)o;
            return Arrays.equals(this.configuration, that.configuration) && Objects.equals(this.name, that.name);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.configuration, this.name});
    }

    public String toString() {
        return "RibbonClientSpecification{" + "name='" + this.name + "', " + "configuration=" + Arrays.toString(this.configuration) + "}";
    }
}
